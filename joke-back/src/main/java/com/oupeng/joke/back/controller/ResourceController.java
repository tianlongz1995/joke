package com.oupeng.joke.back.controller;

import com.alibaba.fastjson.JSON;
import com.oupeng.joke.back.service.DistributorService;
import com.oupeng.joke.back.service.ResourceService;
import com.oupeng.joke.back.service.SourceService;
import com.oupeng.joke.back.util.Constants;
import com.oupeng.joke.back.util.FormatUtil;
import com.oupeng.joke.back.util.StatisExportUtil;
import com.oupeng.joke.cache.JedisCache;
import com.oupeng.joke.cache.JedisKey;
import com.oupeng.joke.domain.*;
import com.oupeng.joke.domain.response.Failed;
import com.oupeng.joke.domain.response.Result;
import com.oupeng.joke.domain.response.Success;
import com.oupeng.joke.domain.statistics.SourceCrawlExport;
import com.oupeng.joke.domain.statistics.SourceQualityExport;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 内容源管理
 */
@Controller
@RequestMapping(value="/resource")
public class ResourceController {
	private static final Logger logger = LoggerFactory.getLogger(ResourceController.class);
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private JedisCache jedisCache;
	/**
	 * 内容源列表
	 * @param name 		名称
	 * @param url		URL
	 * @param status	内容源状态
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/index")
	public String index(@RequestParam(value="name",required=false)String name,
								@RequestParam(value="url",required=false)String url,
								@RequestParam(value="status",required=false)Integer status,
								@RequestParam(value="pageNumber",required=false)Integer pageNumber,
								@RequestParam(value="pageSize",required=false)Integer pageSize,
								Model model){

		IndexResource index = resourceService.getIndexResource();
		IndexResource back = resourceService.getIndexResourceBack();
		IndexResource test = resourceService.getIndexResourceTest();

		model.addAttribute("index", index);

		model.addAttribute("back", back);

		model.addAttribute("test", test);

		return "/resource/index";
	}
	@RequestMapping(value="/updateIndex")
	@ResponseBody
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Result updateIndex(@RequestParam(value="libJs")String libJs,
						@RequestParam(value="appJs")String appJs,
						@RequestParam(value="appCss")String appCss,
						@RequestParam(value="type")Integer type){
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		logger.debug("----------------------UserDetails:{}",userDetails.getUsername());
		if(userDetails.getUsername().equals("admin")){
			if(resourceService.updateIndex(libJs, appJs, appCss, type)){
				return new Success();
			}
			return new Failed("参数无效");
		}
		return new Failed("您没有权限修改!");
	}
}
