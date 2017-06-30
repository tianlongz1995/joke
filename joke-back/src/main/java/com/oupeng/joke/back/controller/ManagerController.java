package com.oupeng.joke.back.controller;

import com.oupeng.joke.back.service.MailService;
import com.oupeng.joke.back.service.ManagerService;
import com.oupeng.joke.back.util.FormatUtil;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.response.Failed;
import com.oupeng.joke.domain.response.Result;
import com.oupeng.joke.domain.response.Success;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 段子控制器
 *
 */
@Controller
@RequestMapping(value="/admin")
public class ManagerController {
    private static final Logger log = LoggerFactory.getLogger(DistributorsController.class);

    @Autowired
	private ManagerService managerService;
    @Autowired
    private JedisCache jedisCache;
    @Autowired
    private MailService mailService;

    /**
     * 验证码收件人
     */
    private String recipient = "shuangh@oupeng.com";

	/**
	 * 页面
	 * @return
	 */
	@RequestMapping(value = "manager")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
	public String manager(){
		return "/main/manager";
	}


    /**
     * 获取验证码
     * @return
     */
    @RequestMapping(value="/getValidationCode", produces = {"application/json"})
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result getValidationCode(@RequestParam(value="type")Integer type){
        String username = getUserName();
        if(username == null){
            return new Failed("登录信息失效,请重新登录!");
        }
        String code = FormatUtil.getRandomValidationCode();
        jedisCache.setAndExpire(JedisKey.VALIDATION_CODE_PREFIX + username + "." + type, code, 60 * 5);
        mailService.sendMail(recipient, "段子后台验证码", "验证码:【"+code+"】;您正在使用段子后台修改数据。");
        log.info("用户[{}]使用段子后台发送验证码, 收件人:[{}]", username, recipient);
        return new Success("验证码发送成功!");
    }

    /**
     * 精选切图
     * @param code
     * @return
     */
    @RequestMapping(value="/choiceCrop", produces = {"application/json"})
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result choiceCrop(@RequestParam(value="code")String code){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String key = JedisKey.VALIDATION_CODE_PREFIX + username + ".1";
        String vCode = jedisCache.get(key);
        if(vCode != null && code.equals(vCode)){
            //			删除验证码缓存
            jedisCache.del(key);
//            切图
            managerService.choiceCrop();
            return new Success("添加成功");
        } else {
            return new Failed("验证码异常!");
        }
    }


    /**
     * 段子头像补全
     * @param code
     * @return
     */
    @RequestMapping(value="/jokeAvatar", produces = {"application/json"})
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result jokeAvatar(@RequestParam(value="code")String code){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String key = JedisKey.VALIDATION_CODE_PREFIX + username + ".2";
        String vCode = jedisCache.get(key);
        if(vCode != null && code.equals(vCode)){
            //			删除验证码缓存
            jedisCache.del(key);
//            切图
            managerService.jokeAvatar();
            return new Success("添加成功");
        } else {
            return new Failed("验证码异常!");
        }
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


}
