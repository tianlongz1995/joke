package com.oupeng.joke.back.controller;

import com.alibaba.fastjson.JSONObject;
import com.oupeng.joke.back.service.JokeService;
import com.oupeng.joke.back.service.SourceService;
import com.oupeng.joke.domain.Joke;
import com.oupeng.joke.domain.response.Failed;
import com.oupeng.joke.domain.response.Result;
import com.oupeng.joke.domain.response.Success;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value="/joke")
public class JokeController {
	
	@Autowired
	private JokeService jokeService;
	@Autowired
	private SourceService sourceService;

	/**
	 * 待审核段子列表
	 * @param status
	 * @param type
	 * @param source
	 * @param startDay
	 * @param endDay
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/list")
	public String getJokeListForVerify(@RequestParam(value="status",required=false)Integer status,
									   @RequestParam(value="type",required=false)Integer type,
									   @RequestParam(value="source",required=false)Integer source,
									   @RequestParam(value="startDay",required=false)String startDay,
									   @RequestParam(value="endDay",required=false)String endDay,
									   @RequestParam(value="pageNumber",required=false)Integer pageNumber,
									   @RequestParam(value="pageSize",required=false)Integer pageSize,
									    Model model){
		pageNumber = pageNumber == null ? 1 : pageNumber;//当前页数
		pageSize = pageSize == null ? 10 : pageSize;//每页显示条数
		int pageCount = 0;//总页数
		int offset = 0 ;//开始条数index
		List<Joke> list = null;
		//	获取总条数
		int count = jokeService.getJokeListForVerifyCount(type, status, source, startDay, endDay);
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

			list = jokeService.getJokeListForVerify(type, status, source, startDay, endDay, offset, pageSize);
		}

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("list", list);
		model.addAttribute("sourceList", sourceService.getAllSourceList());
		model.addAttribute("status", status);
		model.addAttribute("type", type);
		model.addAttribute("source", source);
		model.addAttribute("startDay", startDay);
		model.addAttribute("endDay", endDay);
		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("pageCount", pageCount);
		model.addAllAttributes(jokeService.getJokeVerifyInfoByUser(username));
		return "/joke/list";
	}

	/**
	 * 修改审核状态
	 * @param ids
	 * @param status
	 * @return
	 */
	@RequestMapping(value="/verify", produces = {"application/json"})
	@ResponseBody
	public Result verify(@RequestParam(value="ids")String ids,
			@RequestParam(value="status")Integer status){
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		jokeService.verifyJoke(status, ids, username);
		return new Success();
	} 
	
	@RequestMapping(value="/edit")
	public String edit(@RequestParam(value="id")Integer id,Model model){
		model.addAttribute("joke", jokeService.getJokeById(id));
		return "/joke/edit";
	}

	/**
	 * 更新段子信息 - 默认通过审核
	 * @param id
	 * @param title
	 * @param content
	 * @param img
	 * @param gif
	 * @param width
	 * @param height
	 * @return
	 */
	@RequestMapping(value="/update")
	@ResponseBody
	public Result update(@RequestParam(value="id")Integer id,
			@RequestParam(value="title",required=false)String title,
			@RequestParam(value="content",required=false)String content,
			@RequestParam(value="img",required=false)String img,
			@RequestParam(value="gif",required=false)String gif,
			@RequestParam(value="width")Integer width,
			@RequestParam(value="height")Integer height){
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		boolean result = jokeService.updateJoke(id, title, img, gif,width,height,content, username);
		if(result){
			return new Success();
		}else{
			return new Failed("更新失败");
		}
	}
	
	@RequestMapping(value="/search")
	public String searchJokeList(@RequestParam(value="jokeid",required=false)Integer jokeid,
			@RequestParam(value="content",required=false)String content,
			Model model){
		model.addAttribute("list", jokeService.getJokeListForSearch(jokeid, content));
		model.addAttribute("jokeid", jokeid);
		model.addAttribute("content", content);
		return "/joke/search";
	}

	/**
	 * 段子发布规则页面
	 * @return
	 */
	@RequestMapping(value = "publish")
	public String publishRole(Model model){
		String textRole = jokeService.getPublishRole(10041);
		String qutuRole = jokeService.getPublishRole(10042);
		String recommendRole = jokeService.getPublishRole(10043);
		if (!StringUtils.isEmpty(textRole)) {
			JSONObject textRoleJson = JSONObject.parseObject(textRole);
			model.addAttribute("trole",textRoleJson.get("role"));
			model.addAttribute("textNum",textRoleJson.get("textNum"));
		}
		if (!StringUtils.isEmpty(qutuRole)) {
			JSONObject qutuRoleJson = JSONObject.parseObject(qutuRole);
			model.addAttribute("qrole",qutuRoleJson.get("role"));
			model.addAttribute("qImageNum",qutuRoleJson.get("imageNum"));
			model.addAttribute("qGiftNum",qutuRoleJson.get("gifNum"));
			model.addAttribute("qGiftWeight",qutuRoleJson.get("gifWeight"));
			model.addAttribute("qImageWeight",qutuRoleJson.get("imageWeight"));

		}
		if (!StringUtils.isEmpty(recommendRole)) {
			JSONObject recommendRoleJson = JSONObject.parseObject(recommendRole);
			model.addAttribute("rrole",recommendRoleJson.get("role"));
			model.addAttribute("rTextNum",recommendRoleJson.get("textNum"));
			model.addAttribute("rImageNum",recommendRoleJson.get("imageNum"));
			model.addAttribute("rGiftNum",recommendRoleJson.get("gifNum"));
			model.addAttribute("rTextWeight",recommendRoleJson.get("textWeight"));
			model.addAttribute("rImageWeight",recommendRoleJson.get("imageWeight"));
			model.addAttribute("rGiftWeight",recommendRoleJson.get("gifWeight"));

		}
		return "/joke/publish";
	}

    /**
     *添加发布规则

     * @param role
     * @param textNum
     * @param type 1 纯文 10041，2 趣图 10042， 3 推荐 10043
     * @param imageNum
     * @param giftNum
	 * @param giftWeight
	 * @param imageWeight
	 * @param textWeight
     * @return
     */
	@RequestMapping(value = "addPublishRole")
	@ResponseBody
	public Result addPublishRole( @RequestParam(value = "role")   String role,
                                  @RequestParam(value = "type")   Integer type,
								  @RequestParam(value = "textNum",required = false) Integer textNum,
								  @RequestParam(value = "imageNum",required = false)  Integer imageNum,
								  @RequestParam(value = "giftNum",required = false)  Integer giftNum,
								  @RequestParam(value = "textWeight",required = false) Integer textWeight,
								  @RequestParam(value = "imageWeight",required = false)Integer imageWeight,
								  @RequestParam(value = "giftWeight",required = false)Integer giftWeight){
		//验证cron表达式
		if(!CronExpression.isValidExpression(role)){
			return new Failed("发布时间验证不通过!");
		}else{
             jokeService.addPublishRole(type,role,textNum,imageNum,giftNum,textWeight,imageWeight,giftWeight);
			return new Success("添加成功");
		}
	}

	/**
	 * 新增评论数量记录
	 * @param jid
	 * @return
	 */
	@RequestMapping(value="/incrementComment")
	@ResponseBody
	public Result incrementComment(@RequestParam(value="jid")Integer jid){
		if(jokeService.incrementComment(jid)){
			return new Success();
		}else{
			return new Failed();
		}
	}

}
