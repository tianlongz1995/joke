package com.oupeng.joke.back.controller;

import com.oupeng.joke.back.service.SourceService;
import com.oupeng.joke.back.service.DistributorService;
import com.oupeng.joke.domain.Distributor;
import com.oupeng.joke.domain.Source;
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
 * 内容源管理
 */
@Controller
@RequestMapping(value="/source")
public class SourceController {
	private static final Logger logger = LoggerFactory.getLogger(SourceController.class);
	@Autowired
	private SourceService sourceService;
	@Autowired
	private DistributorService distributorService;

	/**
	 * 内容源列表
	 * @param name 		名称
	 * @param url		URL
	 * @param status	内容源状态
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list")
	public String getSourceList(@RequestParam(value="name",required=false)String name,
							@RequestParam(value="url",required=false)String url,
							@RequestParam(value="status",required=false)Integer status,
							@RequestParam(value="pageNumber",required=false)Integer pageNumber,
							@RequestParam(value="pageSize",required=false)Integer pageSize,
							Model model){
		try {
			pageNumber = pageNumber == null ? 1 : pageNumber;//当前页数
			pageSize = pageSize == null ? 10 : pageSize;//每页显示条数
			Source source = new Source();
			source.setStatus(status);
			source.setName(name);
			source.setUrl(url);
			List<Source> list = null;
			int pageCount = 0;//总页数
			int offset = 0 ;//开始条数index
			int count = sourceService.getSourceListCount(source);//总条数
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
				source.setOffset(offset);
				source.setPageSize(pageSize);
				list = sourceService.getSourceList(source);
			}

			model.addAttribute("count", count);
			model.addAttribute("pageNumber", pageNumber);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("pageCount", pageCount);
			model.addAttribute("list", list);
			model.addAttribute("status", status);
			model.addAttribute("name", name);
			model.addAttribute("url", url);
		} catch (Exception e){
			logger.error(e.getMessage(), e);
		}
		return "/source/list";
	}

	/**
	 *	删除（逻辑删除）
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/del")
	@ResponseBody
	public Result del(@RequestParam(value="id", required = true)Integer id){
		sourceService.del(id);
		return new Success();
	}

	/**
	 * 获取内容源信息并进入修改页面
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/modify")
	public String modify(@RequestParam(value="id",required=true)Integer id,Model model){
		try{
			model.addAttribute("source", sourceService.getSourceById(id));
			Distributor distributor = new Distributor();
			distributor.setStatus(1);
			model.addAttribute("dList", distributorService.getAllDistributorList());
		} catch (Exception e){
			logger.error(e.getMessage(), e);
		}
		return "/source/edit";
	}

	/**
	 * 更新内容源信息
	 * @param id
	 * @param name
	 * @param url
	 * @param status
	 * @return
	 */
	@RequestMapping(value="/update")
	@ResponseBody
	public Result update(@RequestParam(value="id",required=true)Integer id,
						 @RequestParam(value="name",required=false)String name,
						 @RequestParam(value="url",required=false)String url,
						 @RequestParam(value="status",required=true)Integer status){
		sourceService.updateSource(id, name, url, status);
		return new Success();
	}

	/**
	 * 新增内容源
	 * @param source
	 * @return
	 */
	@RequestMapping(value="/add", produces = {"application/json"})
	@ResponseBody
	public Result add(Source source){
		try {
			sourceService.insertSource(source);
		} catch (Exception e){
			logger.error(e.getMessage(), e);
		}
		return new Success();
	}

	/**
	 * 查看内容源抓取信息列表
	 * @param crawlDate 抓取日期 yyyy-MM-dd
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/monitor")
	public String monitor(@RequestParam(value="crawlDate",required=false)String crawlDate,
						  @RequestParam(value="status",required=false)Integer status,
						  Model model){
			model.addAttribute("list", sourceService.getSourceMonitorList(crawlDate, status));
			model.addAttribute("crawlDate", crawlDate);
			model.addAttribute("status", status);
		return "/source/monitor";
	}
}
