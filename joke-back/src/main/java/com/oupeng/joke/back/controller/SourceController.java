package com.oupeng.joke.back.controller;

import com.oupeng.joke.back.service.SourceService;
import com.oupeng.joke.back.service.DistributorService;
import com.oupeng.joke.back.util.Constants;
import com.oupeng.joke.back.util.FormatUtil;
import com.oupeng.joke.back.util.StatisExportUtil;
import com.oupeng.joke.domain.Distributor;
import com.oupeng.joke.domain.QueryParam;
import com.oupeng.joke.domain.Source;
import com.oupeng.joke.domain.SourceCrawl;
import com.oupeng.joke.domain.response.Result;
import com.oupeng.joke.domain.response.Success;
import com.oupeng.joke.domain.statistics.SourceCrawlExport;
import com.oupeng.joke.domain.statistics.SourceQualityExport;
import com.oupeng.joke.domain.statistics.TimeDetailExport;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
//			Distributor distributor = new Distributor();
//			distributor.setStatus(1);
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
	 * @param name
	 * @param url
	 * @param status
	 * @return
	 */
	@RequestMapping(value="/add", produces = {"application/json"})
	@ResponseBody
	public Result add(@RequestParam(value="name",required=true)String name,
					  @RequestParam(value="url",required=true)String url,
					  @RequestParam(value="status",required=true)Integer status){
		try {
			boolean result = sourceService.insertSource(name, url, status);
			if(!result){
				return new Result(2, "URL\""+url.trim()+"\"地址已存在或者不标准，请检查后再提交!", null);
			}
		} catch (Exception e){
			logger.error(e.getMessage(), e);
		}
		return new Success();
	}

	/**
	 * 查看内容源抓取信息列表
	 * @param queryParam
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/crawl")
	public String crawl(QueryParam queryParam, Model model){
        try {
            setQueryTime(queryParam);
        	model.addAttribute("startTime", queryParam.getStartTime());
            model.addAttribute("endTime", queryParam.getEndTime());
            List<SourceCrawl> list = null;
            int pageCount = 0;//总页数
            int offset = 0 ;//开始条数index
			queryParam.setStartTime(queryParam.getStartTime() == null ? String.valueOf(FormatUtil.getYesterday()) : queryParam.getStartTime().replaceAll("-",""));
			queryParam.setEndTime(queryParam.getEndTime() == null ? String.valueOf(FormatUtil.getYesterday()) : queryParam.getEndTime().replaceAll("-",""));
			int count = sourceService.getSourceCrawlListCount(queryParam);//总条数
            if(count > 0){
                if (count % queryParam.getPageSize() == 0) {
                    pageCount = count / queryParam.getPageSize();
                } else {
                    pageCount = count / queryParam.getPageSize() + 1;
                }
                if (queryParam.getPageNumber() > pageCount) {
                    queryParam.setPageNumber(pageCount);
                }
                if (queryParam.getPageNumber() < 1) {
                    queryParam.setPageNumber(1);
                }
                offset = (queryParam.getPageNumber() - 1) * queryParam.getPageSize();
                queryParam.setOffset(offset);
                list =  sourceService.getSourceCrawlList(queryParam);
            }

            model.addAttribute("list", list);
            model.addAttribute("name", queryParam.getName());
            model.addAttribute("type", queryParam.getType());
            model.addAttribute("pageNumber", queryParam.getPageNumber());
            model.addAttribute("pageSize", queryParam.getPageSize());
            model.addAttribute("pageCount", pageCount);
            model.addAttribute("count", count);
			model.addAttribute("sourceType", queryParam.getSourceType());
			model.addAttribute("sourceType", queryParam.getSourceType());
        } catch (Exception e){
            logger.error(e.getMessage(), e);
        }
		return "/source/crawl";
	}


	/**
	 * 内容源审核质量统计
	 * @param queryParam
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/quality")
	public String quality(QueryParam queryParam, Model model){
		try {
            setQueryTime(queryParam);
            model.addAttribute("startTime", queryParam.getStartTime());
            model.addAttribute("endTime", queryParam.getEndTime());
            List<SourceCrawl> list = null;
            int pageCount = 0;//总页数
            int offset = 0 ;//开始条数index
            queryParam.setStartTime(queryParam.getStartTime() == null ? null : queryParam.getStartTime().replaceAll("-",""));
            queryParam.setEndTime(queryParam.getEndTime() == null ? null : queryParam.getEndTime().replaceAll("-",""));
            int count = sourceService.getQualityListCount(queryParam);//总条数
            if(count > 0){
                if (count % queryParam.getPageSize() == 0) {
                    pageCount = count / queryParam.getPageSize();
                } else {
                    pageCount = count / queryParam.getPageSize() + 1;
                }
                if (queryParam.getPageNumber() > pageCount) {
                    queryParam.setPageNumber(pageCount);
                }
                if (queryParam.getPageNumber() < 1) {
                    queryParam.setPageNumber(1);
                }
                offset = (queryParam.getPageNumber() - 1) * queryParam.getPageSize();
                queryParam.setOffset(offset);
                list =  sourceService.getQualityList(queryParam);
            }

            model.addAttribute("list", list);
            model.addAttribute("name", queryParam.getName());
            model.addAttribute("type", queryParam.getType());
            model.addAttribute("pageNumber", queryParam.getPageNumber());
            model.addAttribute("pageSize", queryParam.getPageSize());
            model.addAttribute("pageCount", pageCount);
            model.addAttribute("count", count);
            model.addAttribute("sourceType", queryParam.getSourceType());
        } catch (Exception e){
            logger.error(e.getMessage(), e);
        }
        return "/source/quality";
	}

    /**
     * 设置查询时间
     * @param queryParam
     */
    private void setQueryTime(QueryParam queryParam) {
        if(StringUtils.isBlank(queryParam.getStartTime()) || StringUtils.isBlank(queryParam.getEndTime())){
            queryParam.setStartTime(FormatUtil.getYesterdayStr("yyyy-MM-dd"));
            queryParam.setEndTime(queryParam.getStartTime());
        }
    }


	/**
	 * 数据源抓取统计报告导出
	 * @param startTime
	 * @param endTime
	 * @param name
	 * @param sourceType
	 * @param type
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/crawlExport")
	public void crawlExport( @RequestParam(value="startTime")String startTime,
							 @RequestParam(value="endTime")String endTime,
							 @RequestParam(value="name",required=false)String name,
							 @RequestParam(value="sourceType")Integer sourceType,
							 @RequestParam(value="type",required=false)Integer type,
							 HttpServletResponse response) throws IOException {
		try{
			QueryParam queryParam = new QueryParam();
			queryParam.setStartTime(startTime);
			queryParam.setEndTime(endTime);
			queryParam.setName(name);
			queryParam.setSourceType(sourceType);
			queryParam.setType(type);
			List<SourceCrawlExport> exportList = sourceService.getSourceCrawlExport(queryParam);
			if(!CollectionUtils.isEmpty(exportList)){
				new StatisExportUtil<SourceCrawlExport>().exportExcel("段子抓取统计报告", Constants.STATIS_SOURCE_CRAWL, exportList, response);
			}
		}catch (Exception e){
			logger.error(e.getMessage(), e);
		}
	}
	/**
	 * 数据源审核质量统计报告导出
	 * @param startTime
	 * @param endTime
	 * @param name
	 * @param sourceType
	 * @param type
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/qualityExport")
	public void qualityExport( @RequestParam(value="startTime")String startTime,
							 @RequestParam(value="endTime")String endTime,
							 @RequestParam(value="name",required=false)String name,
							 @RequestParam(value="sourceType")Integer sourceType,
							 @RequestParam(value="type",required=false)Integer type,
							 HttpServletResponse response) throws IOException {
		try{
			QueryParam queryParam = new QueryParam();
			queryParam.setStartTime(startTime);
			queryParam.setEndTime(endTime);
			queryParam.setName(name);
			queryParam.setSourceType(sourceType);
			queryParam.setType(type);
			List<SourceQualityExport> exportList = sourceService.getSourceQualityExport(queryParam);
			if(!CollectionUtils.isEmpty(exportList)){
				new StatisExportUtil<SourceQualityExport>().exportExcel("段子审核质量统计报告", Constants.STATIS_SOURCE_QUALITY, exportList, response);
			}
		}catch (Exception e){
			logger.error(e.getMessage(), e);
		}
	}
}
