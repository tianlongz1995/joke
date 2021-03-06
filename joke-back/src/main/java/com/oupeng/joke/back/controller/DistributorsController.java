package com.oupeng.joke.back.controller;

import com.oupeng.joke.back.service.DistributorsService;
import com.oupeng.joke.back.service.MailService;
import com.oupeng.joke.back.util.FormatUtil;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.Distributor;
import com.oupeng.joke.domain.response.Failed;
import com.oupeng.joke.domain.response.Result;
import com.oupeng.joke.domain.response.Success;
import org.apache.commons.lang3.StringUtils;
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
 * 渠道管理
 */
@Controller
@RequestMapping(value = "/distributors")
public class DistributorsController {
    private static final Logger logger = LoggerFactory.getLogger(DistributorsController.class);
    @Autowired
    private DistributorsService distributorsService;
    @Autowired
    private MailService mailService;
    @Autowired
    private JedisCache jedisCache;
    @Autowired
    private Environment env;
    /**
     * 验证码收件人
     */
    private String recipient;

    @PostConstruct
    public void init() {
        String re = env.getProperty("joke.delete.recipient");
        if (StringUtils.isNotBlank(re)) {
            recipient = re;
        }
    }

    /**
     * 渠道列表
     *
     * @param status
     * @param model
     * @return
     */
    @RequestMapping(value = "/list")
    public String manager(@RequestParam(value = "status", required = false, defaultValue = "1") Integer status,
                          @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                          @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
                          Model model) {

        try {
            Distributor distributor = new Distributor();
            distributor.setStatus(status);
            List<Distributor> list = null;
            int pageCount = 0;//总页数
            int offset = 0;//开始条数index
            int count = distributorsService.getCount(distributor);//总条数
            if (count > 0) {
                if (count % limit == 0) {
                    pageCount = count / limit;
                } else {
                    pageCount = count / limit + 1;
                }

                if (pageNo > pageCount) {
                    pageNo = pageCount;
                }
                if (pageNo < 1) {
                    pageNo = 1;
                }
                offset = (pageNo - 1) * limit;
                distributor.setOffset(offset);
                distributor.setPageSize(limit);
                list = distributorsService.getList(distributor);
            }
            model.addAttribute("count", count);
            model.addAttribute("pageNumber", pageNo);
            model.addAttribute("pageSize", limit);
            model.addAttribute("pageCount", pageCount);
            model.addAttribute("channels", distributorsService.getChannels());
            model.addAttribute("list", list);
            model.addAttribute("status", status);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "/distributors/list";
    }

    /**
     * 新增渠道
     *
     * @param name
     * @param status
     * @param status
     * @return
     */
    @RequestMapping(value = "/add", produces = {"application/json"})
    @ResponseBody
    public Result add(@RequestParam(value = "id") Integer id,
                      @RequestParam(value = "name") String name,
                      @RequestParam(value = "status") Integer status,
                      @RequestParam(value = "limit_number", defaultValue = "30") Integer limit_number,
                      @RequestParam(value = "channelIds", required = false) Integer[] channelIds,
                      @RequestParam(value = "lc", required = false) Integer lc,
                      @RequestParam(value = "lb", required = false) Integer lb,
                      @RequestParam(value = "dt", required = false) Integer dt,
                      @RequestParam(value = "dc", required = false) Integer dc,
                      @RequestParam(value = "db", required = false) Integer db,
                      @RequestParam(value = "di", required = false) Integer di,
                      @RequestParam(value = "s", required = false) Integer s,
                      @RequestParam(value = "dm", required = false) Integer dm,
                      @RequestParam(value = "dms", required = false) Integer dms) {
        String username = getUserName();
        if (username == null) {
            return new Failed("登录信息失效,请重新登录!");
        }
        if (distributorsService.exist(id)) {
            return new Failed("渠道编号已存在!");
        }
        distributorsService.add(id, name, status, username, limit_number, channelIds, s, lc, lb, dt, dc, db, di, dm, dms);
        return new Success();
    }


    /**
     * 修改渠道页面
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/editPage")
    public String edit(@RequestParam(value = "id") Integer id, Model model) {
        model.addAttribute("distributors", distributorsService.getDistributors(id));
        model.addAttribute("channels", distributorsService.getChannelSelected(id));
        model.addAttribute("ads", distributorsService.getAds(id));
        return "/distributors/edit";
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
        logger.info("用户[{}]使用段子后台发送验证码, 收件人:[{}]", username, recipient);
        return new Success("验证码发送成功!");
    }

    /**
     * 修改上下线状态
     *
     * @param did
     * @param code
     * @param status
     * @return
     */
    @RequestMapping(value = "/editStatus", produces = {"application/json"})
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result editStatus(@RequestParam(value = "did") Integer did,
                             @RequestParam(value = "code") Integer code,
                             @RequestParam(value = "status") Integer status) {
        String username = getUserName();
        if (username == null) {
            return new Failed("登录信息失效,请重新登录!");
        }
        if (distributorsService.editStatus(did, String.valueOf(code), status, username)) {
            return new Success();
        } else {
            return new Failed("验证码已失效,请重新输入!");
        }
    }

    /**
     * 删除渠道
     *
     * @param did
     * @param code
     * @return
     */
    @RequestMapping(value = "/del", produces = {"application/json"})
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result editStatus(@RequestParam(value = "did") Integer did,
                             @RequestParam(value = "code") Integer code) {
        String username = getUserName();
        if (username == null) {
            return new Failed("登录信息无效,请重新登录!");
        }
        if (distributorsService.del(did, String.valueOf(code), username)) {
            return new Success();
        } else {
            return new Failed("验证码已失效,请重新输入!");
        }
    }

    /**
     * 更新渠道信息
     *
     * @param id
     * @param name
     * @param status
     * @return
     */
    @RequestMapping(value = "/edit", produces = {"application/json"})
    @ResponseBody
    public Result edit(@RequestParam(value = "id") Integer id,
                       @RequestParam(value = "status") Integer status,
                       @RequestParam(value = "name", required = false) String name,
                       @RequestParam(value = "limit_number", defaultValue = "30") Integer limit_number,
                       @RequestParam(value = "channelIds", required = false) Integer[] channelIds,
                       @RequestParam(value = "lc", required = false) Integer lc,
                       @RequestParam(value = "lb", required = false) Integer lb,
                       @RequestParam(value = "dt", required = false) Integer dt,
                       @RequestParam(value = "dc", required = false) Integer dc,
                       @RequestParam(value = "db", required = false) Integer db,
                       @RequestParam(value = "di", required = false) Integer di,
                       @RequestParam(value = "s", required = false) Integer s,
                       @RequestParam(value = "dr", required = false) Integer dr,
                       @RequestParam(value = "dm", required = false) Integer dm,
                       @RequestParam(value = "dms", required = false) Integer dms) {
        String username = getUserName();
        if (username == null) {
            return new Failed("登录信息失效,请重新登录!");
        }
        distributorsService.edit(id, name, status, username, limit_number, channelIds, s, lc, lb, dt, dc, db, di, dr, dm, dms);
        return new Success();
    }

    /**
     * 渠道横幅列表
     * @param did
     * @param model
     * @return
     */
    @RequestMapping(value="/banner")
    public String banner(@RequestParam(value="did")Integer did,
                         @RequestParam(value="status")Integer status,
                         @RequestParam(value="limit")Integer limit,
                         @RequestParam(value="cid", required = false, defaultValue = "3")Integer cid,
                         @RequestParam(value="pageNo")Integer pageNo,
                         Model model){
        model.addAttribute("list", distributorsService.getDistributorsBanners(did, cid));
        model.addAttribute("did", did);
        model.addAttribute("cid", cid);
        model.addAttribute("status", status);
        model.addAttribute("limit", limit);
        model.addAttribute("pageNo", pageNo);
        return "/distributors/banner";
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
}
