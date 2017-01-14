package com.oupeng.joke.back.controller;

import com.oupeng.joke.back.service.MailService;
import com.oupeng.joke.back.service.ResourceService;
import com.oupeng.joke.back.util.FormatUtil;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.IndexResource;
import com.oupeng.joke.domain.response.Failed;
import com.oupeng.joke.domain.response.Result;
import com.oupeng.joke.domain.response.Success;
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

/**
 * 内容源管理
 */
@Controller
@RequestMapping(value="/resource")
public class ResourceController {
	private static final Logger log = LoggerFactory.getLogger(ResourceController.class);
	@Autowired
	private ResourceService resourceService;
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
	 * 内容源列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/index")
	public String index(Model model){

		IndexResource index = resourceService.getIndexResource();
		IndexResource back = resourceService.getIndexResourceBack();
		IndexResource test = resourceService.getIndexResourceTest();
		model.addAttribute("index", index);
		model.addAttribute("back", back);
		model.addAttribute("test", test);

		return "/resource/index";
	}

	/**
	 * 更新
	 * @param libJs
	 * @param appJs
	 * @param appCss
	 * @param type
	 * @return
	 */
	@RequestMapping(value="/updateIndex")
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Result updateIndex(@RequestParam(value="code")String code,
	                          @RequestParam(value="libJs")String libJs,
						      @RequestParam(value="appJs")String appJs,
						      @RequestParam(value="appCss")String appCss,
						      @RequestParam(value="type")Integer type){
        String username = getUserName();
        if(username == null){
            return new Failed("登录信息失效,请重新登录!");
        }
        String vCode = jedisCache.get(JedisKey.VALIDATION_CODE_PREFIX + username);
        if(vCode != null && code.equals(vCode)){
            //			删除验证码缓存
            jedisCache.del(JedisKey.VALIDATION_CODE_PREFIX + username);
		    if(resourceService.updateIndex(libJs, appJs, appCss, type)){
				return new Success("修改首页配置完成!");
			}
			return new Failed("参数无效");
		}
		return new Failed("验证码异常!");
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
}
