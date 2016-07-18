package com.oupeng.joke.back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oupeng.joke.back.service.JokeService;
import com.oupeng.joke.domain.response.Result;
import com.oupeng.joke.domain.response.Success;

@Controller
@RequestMapping(value="/joke")
public class JokeController {
	
	@Autowired
	private JokeService jokeService;
	
	@RequestMapping(value="/list")
	public String getJokeListForVerify(@RequestParam(value="status",required=false)Integer status,
			@RequestParam(value="type",required=false)Integer type,
			Model model){
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("list", jokeService.getJokeListForVerify(type, status));
		model.addAttribute("status", status);
		model.addAttribute("type", type);
		model.addAllAttributes(jokeService.getJokeVerifyInfoByUser(username));
		return "/joke/list";
	} 
	
	@RequestMapping(value="/verify")
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
		jokeService.updateJoke(id, title, img, gif,width,height,content, username);
		return new Success();
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
}
