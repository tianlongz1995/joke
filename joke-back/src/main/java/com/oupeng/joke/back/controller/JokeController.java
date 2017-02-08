package com.oupeng.joke.back.controller;

import com.alibaba.fastjson.JSONObject;
import com.oupeng.joke.back.service.JokeService;
import com.oupeng.joke.back.service.MailService;
import com.oupeng.joke.back.service.SourceService;
import com.oupeng.joke.back.util.FormatUtil;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.Joke;
import com.oupeng.joke.domain.response.Failed;
import com.oupeng.joke.domain.response.Result;
import com.oupeng.joke.domain.response.Success;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping(value="/joke")
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

    /**
     * 验证码收件人
     */
    private String recipient;
    @PostConstruct
    public void init(){
        String re = env.getProperty("joke.delete.recipient");
        if(org.apache.commons.lang3.StringUtils.isNotBlank(re)){
            recipient = re;
        }
    }

	/**
	 * 待审核段子列表
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
	@RequestMapping(value="/list")
	public String getJokeListForVerify(@RequestParam(value="status",required=false)Integer status,
									   @RequestParam(value="type",required=false)Integer type,
									   @RequestParam(value="source",required=false)Integer source,
									   @RequestParam(value="startDay",required=false)String startDay,
									   @RequestParam(value="endDay",required=false)String endDay,
									   @RequestParam(value="pageNumber",required=false)Integer pageNumber,
									   @RequestParam(value="pageSize",required=false)Integer pageSize,
									    Model model){
		pageNumber = pageNumber == null ? 1 : pageNumber;//当前页数
		pageSize = pageSize == null ? 10 : pageSize;//每页显示条数
		int pageCount = 0;//总页数
		int offset = 0 ;//开始条数index
		List<Joke> list = null;
		//	获取总条数
		int count = jokeService.getJokeListForVerifyCount(type, status, source, startDay, endDay);
		if(count > 0){
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
		model.addAllAttributes(jokeService.getJokeVerifyInfoByUser(username));
		return "/joke/list";
	}

	/**
	 * 修改审核状态
	 * @param ids
	 * @param status
	 * @return
	 */
	@RequestMapping(value="/verify", produces = {"application/json"})
	@ResponseBody
	public Result verify(@RequestParam(value="ids")String ids,
						 @RequestParam(value="status")Integer status){
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		jokeService.verifyJoke(status, ids, username);
		return new Success();
	}

	/**
	 * 转到编辑页
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
	 * @param id
	 * @param title
	 * @param content
	 * @param img
	 * @param gif
	 * @param width
	 * @param height
	 * @return
	 */
	@RequestMapping(value="/update")
	@ResponseBody
	public Result update(@RequestParam(value = "id") Integer id,
						 @RequestParam(value = "title", required = false) String title,
						 @RequestParam(value = "content", required = false) String content,
						 @RequestParam(value = "img", required = false) String img,
						 @RequestParam(value = "gif", required = false) String gif,
						 @RequestParam(value = "width") Integer width,
						 @RequestParam(value = "height") Integer height,
						 @RequestParam(value = "weight",required = false) Integer weight) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		boolean result = jokeService.updateJoke(id, title, img, gif,width,height,content, weight,username);
		if(result){
			return new Success();
		}else{
			return new Failed("更新失败");
		}
	}
	
	@RequestMapping(value="/search")
	public String searchJokeList(@RequestParam(value="jokeid",required=false)Integer jokeid,
			                     @RequestParam(value="content",required=false)String content,
								 Model model){
		model.addAttribute("list", jokeService.getJokeListForSearch(jokeid, content));
		model.addAttribute("jokeid", jokeid);
		model.addAttribute("content", content);
		return "/joke/search";
	}

	/**
	 * 段子发布规则页面
	 * @return
	 */
	@RequestMapping(value = "publish")
	public String publishRole(Model model){
		String textRole = jokeService.getPublishRole(10041);
		String qutuRole = jokeService.getPublishRole(10042);
		String recommendRole = jokeService.getPublishRole(10043);
		if (!StringUtils.isEmpty(textRole)) {
			JSONObject textRoleJson = JSONObject.parseObject(textRole);
			model.addAttribute("trole",textRoleJson.get("role"));
			model.addAttribute("textNum",textRoleJson.get("textNum"));
		}
		if (!StringUtils.isEmpty(qutuRole)) {
			JSONObject qutuRoleJson = JSONObject.parseObject(qutuRole);
			model.addAttribute("qrole",qutuRoleJson.get("role"));
			model.addAttribute("qImageNum",qutuRoleJson.get("imageNum"));
			model.addAttribute("qGiftNum",qutuRoleJson.get("gifNum"));
			model.addAttribute("qGiftWeight",qutuRoleJson.get("gifWeight"));
			model.addAttribute("qImageWeight",qutuRoleJson.get("imageWeight"));

		}
		if (!StringUtils.isEmpty(recommendRole)) {
			JSONObject recommendRoleJson = JSONObject.parseObject(recommendRole);
			model.addAttribute("rrole",recommendRoleJson.get("role"));
			model.addAttribute("rTextNum",recommendRoleJson.get("textNum"));
			model.addAttribute("rImageNum",recommendRoleJson.get("imageNum"));
			model.addAttribute("rGiftNum",recommendRoleJson.get("gifNum"));
			model.addAttribute("rTextWeight",recommendRoleJson.get("textWeight"));
			model.addAttribute("rImageWeight",recommendRoleJson.get("imageWeight"));
			model.addAttribute("rGiftWeight",recommendRoleJson.get("gifWeight"));

		}
		return "/joke/publish";
	}

    /**
     *添加发布规则

     * @param role
     * @param textNum
     * @param type 1 纯文 10041，2 趣图 10042， 3 推荐 10043
     * @param imageNum
     * @param giftNum
	 * @param giftWeight
	 * @param imageWeight
	 * @param textWeight
     * @return
     */
	@RequestMapping(value = "addPublishRole")
	@ResponseBody
	public Result addPublishRole( @RequestParam(value = "code")   String code,
                                  @RequestParam(value = "role")   String role,
                                  @RequestParam(value = "type")   Integer type,
								  @RequestParam(value = "textNum",required = false) Integer textNum,
								  @RequestParam(value = "imageNum",required = false)  Integer imageNum,
								  @RequestParam(value = "giftNum",required = false)  Integer giftNum,
								  @RequestParam(value = "textWeight",required = false) Integer textWeight,
								  @RequestParam(value = "imageWeight",required = false)Integer imageWeight,
								  @RequestParam(value = "giftWeight",required = false)Integer giftWeight){

        String username = getUserName();
        if(username == null){
            return new Failed("登录信息失效,请重新登录!");
        }
	    //验证cron表达式
		if(!CronExpression.isValidExpression(role)){
			return new Failed("发布时间验证不通过!");
		}else{
            String vCode = jedisCache.get(JedisKey.VALIDATION_CODE_PREFIX + username);
            if(vCode != null && code.equals(vCode)){
                //			删除验证码缓存
                jedisCache.del(JedisKey.VALIDATION_CODE_PREFIX + username);
                jokeService.addPublishRole(type,role,textNum,imageNum,giftNum,textWeight,imageWeight,giftWeight);
                return new Success("添加成功");
            } else {
                return new Failed("验证码异常!");
            }
		}
	}

    /**
     * 获取验证码
     * @return
     */
    @RequestMapping(value="/getValidationCode", produces = {"application/json"})
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result getValidationCode(){
        String username = getUserName();
        if(username == null){
            return new Failed("登录信息失效,请重新登录!");
        }
        String code = FormatUtil.getRandomValidationCode();
        jedisCache.setAndExpire(JedisKey.VALIDATION_CODE_PREFIX + username, code, 60 * 5);
        mailService.sendMail(recipient, "段子后台验证码", "验证码:【"+code+"】;您正在使用段子后台修改数据。");
        log.info("用户[{}]使用段子后台发送验证码, 收件人:[{}]", username, recipient);
        return new Success("验证码发送成功!");
    }

    /**
     * 获取当前登录用户名
     * @return
     */
    private String getUserName() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(userDetails != null && userDetails.getUsername() != null){
            return userDetails.getUsername();
        }
        return null;
    }
	/**
	 * 新增评论数量记录
	 * @param jid
	 * @return
	 */
	@RequestMapping(value="/incrementComment")
	@ResponseBody
	public Result incrementComment(@RequestParam(value="jid")Integer jid){
		if(jokeService.incrementComment(jid)){
			return new Success();
		}else{
			return new Failed();
		}
	}

}
