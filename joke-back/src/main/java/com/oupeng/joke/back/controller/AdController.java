package com.oupeng.joke.back.controller;

import com.oupeng.joke.back.service.AdService;
import com.oupeng.joke.domain.Ad;
import com.oupeng.joke.domain.response.Result;
import com.oupeng.joke.domain.response.Success;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 广告管理
 */
@Controller
@RequestMapping(value="/ad")
public class AdController {
	private static final Logger logger = LoggerFactory.getLogger(AdController.class);
	@Autowired
	private AdService adService;

	/**
	 * 广告列表
	 * @param distributorId 渠道编号
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list")
	public String getAdList(@RequestParam(value="distributorId",required=false)Integer distributorId,Model model){
		try {
			List<Ad> list = adService.getAdList(distributorId);
			model.addAttribute("list", list);
			model.addAttribute("distributorId", distributorId);
		}catch (Exception e){
			logger.error(e.getMessage(), e);
		}
		return "/ad/list";
	}

	/**
	 *	修改状态
	 * @param id
	 * @param status
	 * @return
	 */
	@RequestMapping(value="/modifyStatus")
	@ResponseBody
	public Result modifyStatus(@RequestParam(value="id")Integer id,
			@RequestParam(value="status")Integer status){
		adService.updateAdStatus(id, status);
		return new Success();
	}

	/**
	 * 修改广告
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/edit")
	public String edit(@RequestParam(value="id")Integer id,Model model){
		model.addAttribute("ad", adService.getAdById(id));
		return "/ad/edit";
	}

	/**
	 * 更新广告信息
	 * @param id
	 * @param name
	 * @param status
	 * @return
	 */
	@RequestMapping(value="/update")
	@ResponseBody
	public Result update(@RequestParam(value="id",required=true)Integer id,
			@RequestParam(value="name",required=false)String name,
			@RequestParam(value="status",required=true)Integer status){
		adService.updateAd(id, name, status);
		return new Success();
	}

	/**
	 * 新增广告
	 * @param name
	 * @param status
	 * @return
	 */
	@RequestMapping(value="/add")
	@ResponseBody
	public Result searchJokeList(@RequestParam(value="name",required=false)String name,
			@RequestParam(value="status",required=true)Integer status){
		adService.insertAd(name, status);
		return new Success();
	} 
}
