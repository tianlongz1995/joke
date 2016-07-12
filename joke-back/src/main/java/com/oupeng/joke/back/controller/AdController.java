package com.oupeng.joke.back.controller;

import com.oupeng.joke.back.service.AdService;
import com.oupeng.joke.back.service.DistributorService;
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
	@Autowired
	private DistributorService distributorService;

	/**
	 * 广告列表
	 * @param distributorId 渠道编号
	 * @param pos		广告位置
	 * @param status	广告状态
	 * @param slotId	广告位ID
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list")
	public String getAdList(@RequestParam(value="distributorId",required=false)Integer distributorId,
							@RequestParam(value="pos",required=false)Integer pos,
							@RequestParam(value="status",required=false)Integer status,
							@RequestParam(value="slotId",required=false)String slotId,
							@RequestParam(value="pageNumber",required=false)Integer pageNumber,
							@RequestParam(value="pageSize",required=false)Integer pageSize,
							Model model){
		try {
			pageNumber = pageNumber == null ? 1 : pageNumber;//当前页数
			pageSize = pageSize == null ? 10 : pageSize;//每页显示条数
			Ad ad = new Ad();
			ad.setStatus(status);
			ad.setDid(distributorId);
			ad.setPos(pos);
			ad.setSlotId(slotId);
			List<Ad> list = null;
			int pageCount = 0;//总页数
			int offset = 0 ;//开始条数index
			int count = adService.getAdListCount(ad);//总条数
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
				ad.setOffset(offset);
				ad.setPageSize(pageSize);
				list = adService.getAdList(ad);
			}

			model.addAttribute("count", count);
			model.addAttribute("pageNumber", pageNumber);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("pageCount", pageCount);
			model.addAttribute("list", list);
			model.addAttribute("dList", distributorService.getAllDistributorList());
			model.addAttribute("status", status);
			model.addAttribute("pos", pos);
			model.addAttribute("slotId", slotId);
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
	 * 获取广告信息并进入修改页面
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/modify")
	public String modify(@RequestParam(value="id",required=true)Integer id,Model model){
		try{
			model.addAttribute("ad", adService.getAdById(id));
			model.addAttribute("dList", distributorService.getAllDistributorList());
		}catch (Exception e){
			logger.error(e.getMessage(), e);
		}
		return "/ad/edit";
	}

	/**
	 * 更新广告信息
	 * @param id
	 * @param slotId
	 * @param pos
	 * @param slide
	 * @param did
	 * @param status
	 * @return
	 */
	@RequestMapping(value="/update")
	@ResponseBody
	public Result update(@RequestParam(value="id",required=true)Integer id,
						 @RequestParam(value="slotId",required=false)String slotId,
						 @RequestParam(value="pos",required=false)Integer pos,
						 @RequestParam(value="slide",required=true)Integer slide,
						 @RequestParam(value="did",required=false)Integer did,
						 @RequestParam(value="status",required=true)Integer status){
		adService.updateAd(id, slotId, pos, slide, did,status);
		return new Success();
	}

	/**
	 * 新增广告
	 * @param ad
	 * @return
	 */
	@RequestMapping(value="/add")
	@ResponseBody
	public Result add(Ad ad){
		try {
			adService.insertAd(ad);
		}catch (Exception e){
			logger.error(e.getMessage(), e);
		}
		return new Success();
	} 
}
