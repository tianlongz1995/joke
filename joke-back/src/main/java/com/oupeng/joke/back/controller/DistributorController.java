package com.oupeng.joke.back.controller;

import com.oupeng.joke.back.service.DistributorService;
import com.oupeng.joke.domain.Distributor;
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
 * 渠道管理
 */
@Controller
@RequestMapping(value="/distributor")
public class DistributorController {
	private static final Logger logger = LoggerFactory.getLogger(DistributorController.class);
	@Autowired
	private DistributorService distributorService;

	/**
	 * 渠道列表
	 * @param status
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list")
	public String getDistributorList(@RequestParam(value="status",required=false)Integer status,Model model){
		try {
			List<Distributor> list = distributorService.getDistributorList(status);
			model.addAttribute("list", list);
			model.addAttribute("status", status);
		}catch (Exception e){
			logger.error(e.getMessage(), e);
		}
		return "/distributor/list";
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
		distributorService.updateDistributorStatus(id, status);
		return new Success();
	}

	/**
	 * 修改渠道
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/edit")
	public String edit(@RequestParam(value="id")Integer id,Model model){
		model.addAttribute("distributor", distributorService.getDistributorById(id));
		return "/distributor/edit";
	}

	/**
	 * 更新渠道信息
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
		distributorService.updateDistributor(id, name, status);
		return new Success();
	}

	/**
	 * 新增渠道
	 * @param name
	 * @param status
	 * @return
	 */
	@RequestMapping(value="/add")
	@ResponseBody
	public Result searchJokeList(@RequestParam(value="name",required=false)String name,
			@RequestParam(value="status",required=true)Integer status){
		distributorService.insertDistributor(name, status);
		return new Success();
	} 
}
