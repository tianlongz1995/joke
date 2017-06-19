package com.oupeng.joke.back.controller;

import com.alibaba.fastjson.JSONObject;
import com.oupeng.joke.back.service.JokeService;
import com.oupeng.joke.back.service.MailService;
import com.oupeng.joke.back.service.SourceService;
import com.oupeng.joke.back.util.FormatUtil;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.dao.mapper.BlackManMapper;
import com.oupeng.joke.domain.Joke;
import com.oupeng.joke.domain.JokeTop;
import com.oupeng.joke.domain.response.Failed;
import com.oupeng.joke.domain.response.Result;
import com.oupeng.joke.domain.response.Success;
import com.oupeng.joke.domain.user.BlackMan;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 段子控制器
 */
@Controller
@RequestMapping(value = "/joke")
public class JokeController {
    private static final Logger log = LoggerFactory.getLogger(DistributorsController.class);

    @Autowired
    private JokeService jokeService;
    @Autowired
    private SourceService sourceService;
    @Autowired
    private JedisCache jedisCache;
    @Autowired
    private MailService mailService;
    @Autowired
    private Environment env;
    @Autowired
    private BlackManMapper blackManMapper;

    /**
     * 验证码收件人
     */
    private String recipient;

    private String imgPrefix = "http://joke2-img.oupeng.com/";

    @PostConstruct
    public void init() {
        String re = env.getProperty("joke.delete.recipient");
        if (StringUtils.isNoneBlank(re)) {
            recipient = re;
        }
        String url = env.getProperty("img.real.server.url");
        if (StringUtils.isNoneBlank(url)) {
            imgPrefix = url;
        }
    }

    /**
     * 段子列表
     *
     * @param status
     * @param type
     * @param source
     * @param startDay
     * @param endDay
     * @param pageNumber
     * @param pageSize
     * @param model
     * @return
     */
    @RequestMapping(value = "/list")
    public String list(@RequestParam(value = "status", required = false) Integer status,
                       @RequestParam(value = "type", required = false) Integer type,
                       @RequestParam(value = "source", required = false) Integer source,
                       @RequestParam(value = "startDay", required = false) String startDay,
                       @RequestParam(value = "endDay", required = false) String endDay,
                       @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                       @RequestParam(value = "pageSize", required = false) Integer pageSize,
                       Model model) {
        pageNumber = pageNumber == null ? 1 : pageNumber;//当前页数
        pageSize = pageSize == null ? 10 : pageSize;//每页显示条数
        int pageCount = 0;//总页数
        int offset = 0;//开始条数index
        List<Joke> list = null;
        //	获取总条数
        int count = jokeService.getJokeListForVerifyCount(type, status, source, startDay, endDay);
        if (count > 0) {
            if (count % pageSize == 0) {
                pageCount = count / pageSize;
            } else {
                pageCount = count / pageSize + 1;
            }

            if (pageNumber > pageCount) {
                pageNumber = pageCount;
            }
            if (pageNumber < 1) {
                pageNumber = 1;
            }
            offset = (pageNumber - 1) * pageSize;

            list = jokeService.getJokeListForVerify(type, status, source, startDay, endDay, offset, pageSize);
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("list", list);
        model.addAttribute("sourceList", sourceService.getAllSourceList());
        model.addAttribute("status", status);
        model.addAttribute("type", type);
        model.addAttribute("source", source);
        model.addAttribute("startDay", startDay);
        model.addAttribute("endDay", endDay);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("count", count);
        model.addAllAttributes(jokeService.getJokeVerifyInfoByUser(username));
        return "/joke/list";
    }

    /**
     * 修改审核状态
     *
     * @param ids       段子编号
     * @param status    段子待修改状态
     * @param allStatus 当前段子所在状态
     * @return
     */
    @RequestMapping(value = "/verify", produces = {"application/json"})
    @ResponseBody
    public Result verify(@RequestParam(value = "ids") String ids,
                         @RequestParam(value = "status") Integer status,
                         @RequestParam(value = "allStatus", required = false) Integer allStatus) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        jokeService.verifyJoke(status, ids, username, allStatus);
        return new Success();
    }

    /**
     * 转到编辑页
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/edit")
    public String edit(@RequestParam(value = "id") Integer id, Model model) {
        model.addAttribute("joke", jokeService.getJokeById(id));
        return "/joke/edit";
    }

    /**
     * 更新段子信息 - 默认通过审核
     *
     * @param id
     * @param title
     * @param content
     * @return
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Result update(@RequestParam(value = "id") Integer id,
                         @RequestParam(value = "title", required = false) String title,
                         @RequestParam(value = "content", required = false) String content,
                         @RequestParam(value = "weight", required = false) Integer weight) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean result = jokeService.updateJoke(id, title, content, weight, username);
        if (result) {
            return new Success();
        } else {
            return new Failed("更新失败");
        }
    }

    @RequestMapping(value = "/search")
    public String searchJokeList(@RequestParam(value = "jokeid", required = false) Integer jokeid,
                                 @RequestParam(value = "content", required = false) String content,
                                 Model model) {
        model.addAttribute("list", jokeService.getJokeListForSearch(jokeid, content));
        model.addAttribute("jokeid", jokeid);
        model.addAttribute("content", content);
        return "/joke/search";
    }

    /**
     * 段子发布规则页面
     *
     * @return
     */
    @RequestMapping(value = "/publish")
    public String publishRole(Model model) {
        String textRole = jokeService.getPublishRole(10041);
        String qutuRole = jokeService.getPublishRole(10042);
        String recommendRole = jokeService.getPublishRole(10043);
        if (!StringUtils.isEmpty(textRole)) {
            JSONObject textRoleJson = JSONObject.parseObject(textRole);
            model.addAttribute("trole", textRoleJson.get("role"));
            model.addAttribute("textNum", textRoleJson.get("textNum"));
        }
        if (!StringUtils.isEmpty(qutuRole)) {
            JSONObject qutuRoleJson = JSONObject.parseObject(qutuRole);
            model.addAttribute("qrole", qutuRoleJson.get("role"));
            model.addAttribute("qImageNum", qutuRoleJson.get("imageNum"));
            model.addAttribute("qGiftNum", qutuRoleJson.get("gifNum"));
            model.addAttribute("qGiftWeight", qutuRoleJson.get("gifWeight"));
            model.addAttribute("qImageWeight", qutuRoleJson.get("imageWeight"));

        }
        if (!StringUtils.isEmpty(recommendRole)) {
            JSONObject recommendRoleJson = JSONObject.parseObject(recommendRole);
            model.addAttribute("rrole", recommendRoleJson.get("role"));
            model.addAttribute("rTextNum", recommendRoleJson.get("textNum"));
            model.addAttribute("rImageNum", recommendRoleJson.get("imageNum"));
            model.addAttribute("rGiftNum", recommendRoleJson.get("gifNum"));
            model.addAttribute("rTextWeight", recommendRoleJson.get("textWeight"));
            model.addAttribute("rImageWeight", recommendRoleJson.get("imageWeight"));
            model.addAttribute("rGiftWeight", recommendRoleJson.get("gifWeight"));

        }
        return "/joke/publish";
    }

    /**
     * 添加发布规则
     *
     * @param role
     * @param textNum
     * @param type        1 纯文 10041，2 趣图 10042， 3 推荐 10043
     * @param imageNum
     * @param giftNum
     * @param giftWeight
     * @param imageWeight
     * @param textWeight
     * @return
     */
    @RequestMapping(value = "/addPublishRole")
    @ResponseBody
    public Result addPublishRole(@RequestParam(value = "code") String code,
                                 @RequestParam(value = "role") String role,
                                 @RequestParam(value = "type") Integer type,
                                 @RequestParam(value = "textNum", required = false) Integer textNum,
                                 @RequestParam(value = "imageNum", required = false) Integer imageNum,
                                 @RequestParam(value = "giftNum", required = false) Integer giftNum,
                                 @RequestParam(value = "textWeight", required = false) Integer textWeight,
                                 @RequestParam(value = "imageWeight", required = false) Integer imageWeight,
                                 @RequestParam(value = "giftWeight", required = false) Integer giftWeight) {

        String username = getUserName();
        if (username == null) {
            return new Failed("登录信息失效,请重新登录!");
        }
        //验证cron表达式
        if (!CronExpression.isValidExpression(role)) {
            return new Failed("发布时间验证不通过!");
        } else {
            String vCode = jedisCache.get(JedisKey.VALIDATION_CODE_PREFIX + username);
            if (vCode != null && code.equals(vCode)) {
                //			删除验证码缓存
                jedisCache.del(JedisKey.VALIDATION_CODE_PREFIX + username);
                jokeService.addPublishRole(type, role, textNum, imageNum, giftNum, textWeight, imageWeight, giftWeight);
                return new Success("添加成功");
            } else {
                return new Failed("验证码异常!");
            }
        }
    }

    /**
     * 获取验证码
     *
     * @return
     */
    @RequestMapping(value = "/getValidationCode", produces = {"application/json"})
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result getValidationCode() {
        String username = getUserName();
        if (username == null) {
            return new Failed("登录信息失效,请重新登录!");
        }
        String code = FormatUtil.getRandomValidationCode();
        jedisCache.setAndExpire(JedisKey.VALIDATION_CODE_PREFIX + username, code, 60 * 5);
        mailService.sendMail(recipient, "段子后台验证码", "验证码:【" + code + "】;您正在使用段子后台修改数据。");
        log.info("用户[{}]使用段子后台发送验证码, 收件人:[{}]", username, recipient);
        return new Success("验证码发送成功!");
    }

    /**
     * 获取当前登录用户名
     *
     * @return
     */
    private String getUserName() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails != null && userDetails.getUsername() != null) {
            return userDetails.getUsername();
        }
        return null;
    }

    /**
     * 新增评论数量记录
     *
     * @param jid
     * @return
     */
    @RequestMapping(value = "/incrementComment", produces = {"application/json"})
    @ResponseBody
    public Result incrementComment(@RequestParam(value = "jid") Integer[] jid) {
        if (jokeService.incrementComment(jid)) {
            return new Success();
        } else {
            return new Failed();
        }
    }

    /**
     * 减少评论数量记录
     *
     * @param jid
     * @return
     */
    @RequestMapping(value = "/decrementComment", produces = {"application/json"})
    @ResponseBody
    public Result decrementComment(@RequestParam(value = "jid") Integer[] jid) {
        if (jokeService.decrementComment(jid)) {
            return new Success();
        } else {
            return new Failed();
        }
    }

    /**
     * 首页置顶段子列表
     *
     * @param status
     * @param type
     * @param source
     * @param startDay
     * @param endDay
     * @param pageNumber
     * @param pageSize
     * @param model
     * @return
     */
    @RequestMapping(value = "/top")
    public String top(@RequestParam(value = "status", required = false) Integer status,
                      @RequestParam(value = "type", required = false) Integer type,
                      @RequestParam(value = "source", required = false) Integer source,
                      @RequestParam(value = "startDay", required = false) String startDay,
                      @RequestParam(value = "endDay", required = false) String endDay,
                      @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                      @RequestParam(value = "pageSize", required = false) Integer pageSize,
                      Model model) {
        pageNumber = pageNumber == null ? 1 : pageNumber;//当前页数
        pageSize = pageSize == null ? 10 : pageSize;//每页显示条数
        if (startDay == null || startDay.length() < 1 || endDay == null || endDay.length() < 1) {
            startDay = null;
            endDay = null;
        }
        int pageCount = 0;//总页数
        int offset;//开始条数index
        List<JokeTop> list = null;
        //	获取总条数
        int count = jokeService.getJokeTopListCount(type, status, source, startDay, endDay);
        if (count > 0) {
            if (count % pageSize == 0) {
                pageCount = count / pageSize;
            } else {
                pageCount = count / pageSize + 1;
            }

            if (pageNumber > pageCount) {
                pageNumber = pageCount;
            }
            if (pageNumber < 1) {
                pageNumber = 1;
            }
            offset = (pageNumber - 1) * pageSize;

            list = jokeService.getJokeTopList(type, status, source, startDay, endDay, offset, pageSize);
        }

        model.addAttribute("list", list);
        model.addAttribute("sourceList", sourceService.getAllSourceList());
        model.addAttribute("status", status);
        model.addAttribute("type", type);
        model.addAttribute("source", source);
        model.addAttribute("startDay", startDay);
        model.addAttribute("endDay", endDay);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("count", count);
        return "/joke/top";
    }

    /**
     * 发布推荐置顶段子
     *
     * @param ids
     * @param releaseTime
     * @return
     */
    @RequestMapping(value = "/releaseTopJoke")
    @ResponseBody
    public Result releaseTopJoke(@RequestParam(value = "ids") Integer[] ids,
                                 @RequestParam(value = "sorts") Integer[] sorts,
                                 @RequestParam(value = "releaseTime") String releaseTime) {
        String username = getUserName();
        if (username == null) {
            return new Failed("登录信息失效,请重新登录!");
        }
        return jokeService.releaseTopJoke(ids, sorts, releaseTime, username);
    }


    /**
     * 修改排序值
     *
     * @param id
     * @param sort
     * @return
     */
    @RequestMapping(value = "/editTopJokeSort")
    @ResponseBody
    public Result editTopJokeSort(@RequestParam(value = "id") Integer id,
                                  @RequestParam(value = "sort") Integer sort) {
        String username = getUserName();
        if (username == null) {
            return new Failed("登录信息失效,请重新登录!");
        }
        return jokeService.editTopJokeSort(id, sort, username);
    }

    /**
     * 批量修改排序值
     *
     * @param ids
     * @param sorts
     * @return
     */
    @RequestMapping(value = "/editTopJokeSorts")
    @ResponseBody
    public Result editTopJokeSorts(@RequestParam(value = "ids") Integer[] ids,
                                   @RequestParam(value = "sorts") Integer[] sorts) {
        String username = getUserName();
        if (username == null) {
            return new Failed("登录信息失效,请重新登录!");
        }
        return jokeService.editTopJokeSorts(ids, sorts, username);
    }

    /**
     * 置顶段子下线
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/topOffline", produces = {"application/json"})
    @ResponseBody
    public Result topOffline(@RequestParam(value = "ids") String ids) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        jokeService.topOffline(ids, username);
        return new Success();
    }

    /**
     * 添加段子页面
     *
     * @return
     */
    @RequestMapping(value = "/addJokePage")
    public String addJokePage() {
        return "joke/add";
    }


    //以下为拉黑管理内容

    /**
     * 跳到拉黑管理界面
     *
     * @return
     */
    @RequestMapping(value = "/blackManage")
    public String blackManage() {

        return "black/list";
    }


    /**
     * 查询一个拉黑用户
     *
     * @param uid   用户的uID
     * @param model
     * @return
     */
    @RequestMapping("/getBlackMan")
    public String getBlackMan(@RequestParam(value = "uid", required = false) String uid,
                              Model model) {
        List<BlackMan> blackManList = blackManMapper.getBlackMan(uid);
        model.addAttribute("list", blackManList);
        return "black/list";
    }

    /**
     * 恢复被拉黑的用户
     *
     * @param uid
     * @return
     */
    @RequestMapping("/retrieve")
    @ResponseBody
    public Result retrieve(@RequestParam(value = "uid", required = false) String uid) {
        try {
            boolean success = blackManMapper.deleteABlackMan(uid);
            jedisCache.hdel(JedisKey.BLACK_MAN, uid);
            return new Success();
        } catch (Exception e) {
            log.info(e.getMessage() + ":" + e.getStackTrace());
            return new Failed("恢复失败,请查看后台日志");
        }
    }

    /**
     * 请求一页拉黑用户的数据
     *
     * @param pageNumber
     * @param pageSize
     * @param model
     * @return
     */
    @RequestMapping("/listBlackMan")
    public String listBlackMan(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                               @RequestParam(value = "pageSize", required = false) Integer pageSize,
                               Model model) {
        List<BlackMan> blackManList = null;

        pageNumber = pageNumber == null ? 1 : pageNumber;//当前页数
        pageSize = pageSize == null ? 10 : pageSize;//每页显示条数
        int pageCount = 0;//总页数
        int offset = 0;//开始条数index
        //	获取总条数
        /**
         * count：数据库中记录总数
         * pageCount:分页后的页面总数
         * pageNumber:当前所在的页面号
         * pageSize:页面包含的记录行数量
         */
        int count = blackManMapper.countBlackMan();
        if (count > 0) {
            if (count % pageSize == 0) {
                pageCount = count / pageSize;
            } else {
                pageCount = count / pageSize + 1;
            }

            if (pageNumber > pageCount) {
                pageNumber = pageCount;
            }
            if (pageNumber < 1) {
                pageNumber = 1;
            }
            offset = (pageNumber - 1) * pageSize;

            log.info("准备查询当前页面的数据");
            blackManList = blackManMapper.listBlackManInRange(offset, pageSize);
            log.info("查询成功");
        }

        model.addAttribute("list", blackManList);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("count", count);

        return "black/list";
    }
    //拉黑管理内容结束


    /**
     * 新增段子
     *
     * @param title
     * @param type
     * @param image
     * @param content
     * @param weight
     * @return
     */
    @RequestMapping(value = "/addJoke", produces = {"application/json"})
    @ResponseBody
    public Result addJoke(@RequestParam(value = "title", required = false) String title,
                          @RequestParam(value = "type") Integer type,
                          @RequestParam(value = "image", required = false) String image,
                          @RequestParam(value = "content", required = false) String content,
                          @RequestParam(value = "weight", required = false) Integer weight) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Joke joke = jokeService.addJoke(title, type, image, content, weight, username);
        if (joke != null) {
            return new Success(imgPrefix + joke.getImg(), joke.getId().toString());
        } else {
            return new Failed("添加失败!");
        }
    }

    /**
     * 获取动图封面图的下一帧
     *
     * @param img
     * @param index
     * @return
     */
    @RequestMapping(value = "/nextFrame", produces = {"application/json"})
    @ResponseBody
    public Result nextFrame(@RequestParam(value = "img") String img,
                            @RequestParam(value = "index") int index) {
        Joke joke = jokeService.nextFrame(img, index);
        if (joke != null) {
            return new Success(joke.getImg());
        } else {
            return new Failed("添加失败!");
        }
    }

    /**
     * 确认动图封面图
     *
     * @param img
     * @param img
     * @return
     */
    @RequestMapping(value = "/submitImage", produces = {"application/json"})
    @ResponseBody
    public Result submitImage(@RequestParam(value = "id") Integer id,
                              @RequestParam(value = "img") String img) {
        boolean result = jokeService.submitImage(id, img);
        if (result) {
            return new Success();
        } else {
            return new Failed("添加失败!");
        }
    }


}
