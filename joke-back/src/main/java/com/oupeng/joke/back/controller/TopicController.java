package com.oupeng.joke.back.controller;

import com.oupeng.joke.back.service.DistributorService;
import com.oupeng.joke.back.service.JokeService;
import com.oupeng.joke.back.service.TopicService;
import com.oupeng.joke.back.util.Constants;
import com.oupeng.joke.domain.Joke;
import com.oupeng.joke.domain.Topic;
import com.oupeng.joke.domain.response.Failed;
import com.oupeng.joke.domain.response.Result;
import com.oupeng.joke.domain.response.Success;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value="/topic")
public class TopicController {
	private static final Logger logger = LoggerFactory.getLogger(TopicController.class);
	@Autowired
	private TopicService topicService;
	@Autowired
	private DistributorService distributorService;
	@Autowired
	private JokeService jokeService;
	
	
	@RequestMapping(value="/list")
	public String list(@RequestParam(value="status",required=false)Integer status,
					   @RequestParam(value="pageNumber",required=false)Integer pageNumber,
					   @RequestParam(value="pageSize",required=false)Integer pageSize,
					   Model model){

		pageNumber = pageNumber == null ? 1 : pageNumber;//当前页数
		pageSize = pageSize == null ? 10 : pageSize;//每页显示条数
		int pageCount = 0;//总页数
		int offset;//开始条数index
		List<Topic> list = null;
		int count = topicService.getTopicListCount(status);//总条数
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

			list = topicService.getTopicList(status, offset, pageSize);
		}
		model.addAttribute("count", count);
		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("pageCount", pageCount);
		model.addAttribute("list", list);
		model.addAttribute("status", status);
		return "/topic/list";
	}

	/**
	 * 修改专题状态
	 * @param id
	 * @param status
	 * @return
	 */
	@RequestMapping(value="/verify")
	@ResponseBody
	public Result verify(@RequestParam(value="id")Integer id,
						 @RequestParam(value="status")Integer status){
		String result = topicService.updateTopicStatus(id, status);
		if(result == null){
			return new Success();
		}else{
			return new Failed(result);
		}
	}

	/**
	 * 进入修改页面
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/edit")
	public String edit(@RequestParam(value="id")Integer id,
					   @RequestParam(value="status",required=false)Integer status,
					   Model model){
		model.addAttribute("topic", topicService.getTopicById(id));
		model.addAttribute("status", status);
		return "/topic/edit";
	}

	/**
	 * 修改专题列表记录
	 * @param id
	 * @param title
	 * @param img
	 * @param content
	 * @param publishTime
	 * @return
	 */
	@RequestMapping(value="/update")
	@ResponseBody
	public Result update(@RequestParam(value="id")Integer id,
			@RequestParam(value="title",required=false)String title,
			@RequestParam(value="img",required=false)String img,
			@RequestParam(value="content")String content,
			@RequestParam(value="publishTime",required=false)String publishTime){
		boolean result = topicService.updateTopic(id, title, img, content, publishTime);
		if(result){
			return new Success();
		}else{
			return new Failed("更新失败!");
		}
	}

	/**
	 * 新增专题
	 * @param title
	 * @param img
	 * @param content
	 * @param publishTime
	 * @return
	 */
	@RequestMapping(value="/add")
	@ResponseBody
	public Result add(@RequestParam(value="title",required=false)String title,
					  @RequestParam(value="img",required=false)String img,
			          @RequestParam(value="content")String content,
					  @RequestParam(value="publishTime",required=false)String publishTime){
		boolean result = topicService.insertTopic(title, img, content, publishTime);
		if(result){
			return new Success();
		}else{
			return new Failed("更新失败!");
		}
	}

	/**
	 * 专题增加段子数据
	 * @param topicId
	 * @param type
	 * @param status		专题状态 - 返回专题列表时使用
	 * @param pageNumber
	 * @param pageSize
	 * @param pNumber		专题列表页码 - 返回专题列表时使用
	 * @param pSize			专题列表记录数 - 返回专题列表时使用
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/addjoke")
	public String getJokeList4Add(@RequestParam(value="topicId")Integer topicId,
								  @RequestParam(value="type",required=false)Integer type,
								  @RequestParam(value="pageNumber",required=false)Integer pageNumber,
								  @RequestParam(value="pageSize",required=false)Integer pageSize,
								  @RequestParam(value="status",required=false)Integer status,
								  @RequestParam(value="pNumber",required=false)Integer pNumber,
								  @RequestParam(value="pSize",required=false)Integer pSize,
								  Model model){
		pageNumber = pageNumber == null ? 1 : pageNumber;//当前页数
		pageSize = pageSize == null ? 10 : pageSize;//每页显示条数
		int pageCount = 0;//总页数
		int offset;//开始条数index
		List<Joke> list = null;
		int count = jokeService.getJokeListForTopicCount(type, Constants.JOKE_STATUS_VALID);//总条数 - 查询审核通过的段子数据
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

			list = jokeService.getJokeListForTopic(type, Constants.JOKE_STATUS_VALID, offset, pageSize);// - 查询审核通过的段子数据
		}
		model.addAttribute("count", count);
		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("pageCount", pageCount);
		model.addAttribute("list", list);
		model.addAttribute("topicId", topicId);
		model.addAttribute("type", type);
		model.addAttribute("status", status);
		model.addAttribute("pNumber", pNumber);
		model.addAttribute("pSize", pSize);

		return "/topic/addjoke";
	}

	/**
	 * 待删除专题段子列表
	 * @param topicId
	 * @param pageNumber
	 * @param pageSize
	 * @param status
	 * @param pNumber
	 * @param pSize
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/deljoke")
	public String deljoke(@RequestParam(value="topicId")Integer topicId,
						  @RequestParam(value="pageNumber",required=false)Integer pageNumber,
						  @RequestParam(value="pageSize",required=false)Integer pageSize,
						  @RequestParam(value="status",required=false)Integer status,
						  @RequestParam(value="pNumber",required=false)Integer pNumber,
						  @RequestParam(value="pSize",required=false)Integer pSize,
						  Model model){

		pageNumber = pageNumber == null ? 1 : pageNumber;//当前页数
		pageSize = pageSize == null ? 10 : pageSize;//每页显示条数
		int pageCount = 0;//总页数
		int offset;//开始条数index
		List<Joke> list = null;
		int count = topicService.getTopicJokeListCountByTopicId(topicId);//总条数
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

			list = topicService.getTopicJokeListByTopicId(topicId, offset, pageSize);
		}
		model.addAttribute("count", count);
		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("pageCount", pageCount);
		model.addAttribute("topicId", topicId);
    	model.addAttribute("list", list);

		model.addAttribute("status", status);
		model.addAttribute("pNumber", pNumber);
		model.addAttribute("pSize", pSize);
		return "/topic/deljoke";
	}

	/**
	 * 添加专题段子关联
	 * @param ids
	 * @param topicId
	 * @return
	 */
	@RequestMapping(value="/addBatchJoke")
	@ResponseBody
	public Result add(@RequestParam(value="ids",required=false)String ids,
			          @RequestParam(value="topicId")Integer topicId){
		topicService.addTopicJoke(ids, topicId);
		return new Success();
	}

	/**
	 * 删除专题段子关联
	 * @param ids
	 * @param topicId
	 * @return
	 */
	@RequestMapping(value="/delBatchJoke")
	@ResponseBody
	public Result del(@RequestParam(value="ids",required=false)String ids,
					  @RequestParam(value="topicId")Integer topicId){
		topicService.delTopicJoke(ids, topicId);
		return new Success();
	}

	/**
	 * 添加原创内容页面
	 * @param topicId
	 * @param type
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/addOriginal")
	public String addOriginal(@RequestParam(value="topicId")Integer topicId,
							  @RequestParam(value="type",required=false)Integer type,
							  Model model){
		model.addAttribute("topicId", topicId);
		model.addAttribute("type", type);
		return "/topic/addoriginal";
	}

	/**
	 * 存储原创内容
	 * @param topicId
	 * @param title
	 * @param img
	 * @param gif
	 * @param content
	 * @return
	 */
	@RequestMapping(value="/addOriginalContent", produces = {"application/json"})
	@ResponseBody
	public Result addOriginalContent(@RequestParam(value="topicId")Integer topicId,
									 @RequestParam(value="title",required=false)String title,
									 @RequestParam(value="img",required=false)String img,
									 @RequestParam(value="gif",required=false)String gif,
									 @RequestParam(value="content",required=false)String content){
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			if(StringUtils.isNotBlank(content)
					|| StringUtils.isNotBlank(img)
					|| StringUtils.isNotBlank(gif)){
				boolean status = topicService.addOriginalContent(title, img, gif, content, topicId, username);
				if (status) {
					return new Success();
				} else {
					return new Failed("存储图片失败！");
				}
			}else {
				return new Failed("内容或者图片不能全为空!");
			}
		}catch (Exception e){
			logger.error(e.getMessage(), e);
			return new Failed("存储图片异常！");
		}
	}

}
