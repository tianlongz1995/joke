package com.oupeng.joke.back.controller;

import com.oupeng.joke.back.service.ChannelService;
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
	@Autowired
	private ChannelService channelService;

	/**
	 * 渠道列表
	 * @param status
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list")
	public String getDistributorList(@RequestParam(value="status",required=false)Integer status,
									 @RequestParam(value="pageNumber",required=false)Integer pageNumber,
									 @RequestParam(value="pageSize",required=false)Integer pageSize,
									 Model model){
		try {
			pageNumber = pageNumber == null ? 1 : pageNumber;//当前页数
			pageSize = pageSize == null ? 10 : pageSize;//每页显示条数
			Distributor distributor = new Distributor();
			distributor.setStatus(status);
			List<Distributor> list = null;
			int pageCount = 0;//总页数
			int offset = 0 ;//开始条数index
			int count = distributorService.getDistributorListCount(distributor);//总条数
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
				distributor.setOffset(offset);
				distributor.setPageSize(pageSize);
				list = distributorService.getDistributorList(distributor);
			}
			model.addAttribute("count", count);
			model.addAttribute("pageNumber", pageNumber);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("pageCount", pageCount);

//			List<Distributor> list = distributorService.getDistributorList(status);
			model.addAttribute("list", list);
			model.addAttribute("channelList", channelService.getChannelList(1));
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
		model.addAttribute("channelList", channelService.getChannelStatusList(id));
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
			@RequestParam(value="status",required=true)Integer status,
			@RequestParam(value="channelIds",required=false)Integer[] channelIds){
		distributorService.updateDistributor(id, name, status, channelIds);
		return new Success();
	}

	/**
	 * 新增渠道
	 * @param name
	 * @param status
	 * @return
	 */
	@RequestMapping(value="/add", produces = {"application/json"})
	@ResponseBody
	public Result add(@RequestParam(value="addName",required=false)String name,
					  @RequestParam(value="addStatus",required=false)Integer status,
					  @RequestParam(value="channelIds",required=false)Integer[] channelIds){
		distributorService.insertDistributor(name, status, channelIds);
		return new Success();
	}

	/**
	 * 渠道广告配置缓存页面
	 * @param managerKey
	 * @return
	 */
	@RequestMapping(value="/dataManager")
	public String dataManager(@RequestParam(value="managerKey",required=true)String managerKey){
		boolean status = distributorService.checkManagerKey(managerKey);
		if(status){
			return "/distributor/manager";
		}else {
			return "/distributor/list";
		}
	}

	/**
	 * 更新渠道广告配置缓存
	 * @param managerKey
	 * @return
	 */
	@RequestMapping(value="/updateDistributorAdConfigCache", produces = {"application/json"})
	@ResponseBody
	public Result updateDistributorAdConfigCache(@RequestParam(value="managerKey",required=true)String managerKey){
		return distributorService.updateDistributorAdConfigCache(managerKey);
	}
}
