package com.oupeng.joke.back.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Maps;
import com.oupeng.joke.back.util.Constants;
import com.oupeng.joke.dao.mapper.JokeMapper;
import com.oupeng.joke.domain.Joke;
import com.oupeng.joke.domain.JokeVerifyInfo;

@Service
public class JokeService {
	@Autowired
	private JokeMapper jokeMapper;
	
	public List<Joke> getJokeListForVerify(Integer type,Integer status){
		return jokeMapper.getJokeListForVerify(type, status);
	}
	
	public void verifyJoke(Integer status,String ids,String user){
		jokeMapper.verifyJoke(status, ids, user);
	}
	
	public Joke getJokeById(Integer id){
		return jokeMapper.getJokeById(id);
	}
	
	public void updateJoke(Integer id,String title,String img,String gif,String content,String user){
		Joke joke = new Joke();
		joke.setId(id);
		joke.setGif(gif);
		joke.setContent(content);
		joke.setImg(img);
		joke.setTitle(title);
		joke.setVerifyUser(user);
		if(StringUtils.isNotBlank(gif)){
			joke.setType(Constants.JOKE_TYPE_GIF);
		}else if(StringUtils.isNotBlank(img)){
			joke.setType(Constants.JOKE_TYPE_IMG);
		}else{
			joke.setType(Constants.JOKE_TYPE_TEXT);
		}
		jokeMapper.updateJoke(joke);
	}
	
	public Map<String,Integer> getJokeVerifyInfoByUser(String user){
		Map<String,Integer> map = Maps.newHashMap();
		List<JokeVerifyInfo> list = jokeMapper.getJokeVerifyInfoByUser(user);
		String textKey = "type0";
		String imgKey = "type1";
		String gifKey = "type2";
		if(!CollectionUtils.isEmpty(list)){
			for(JokeVerifyInfo jokeVerifyInfo : list){
				if(Constants.JOKE_TYPE_GIF == jokeVerifyInfo.getType()){
					map.put(gifKey, jokeVerifyInfo.getNum());
				}else if(Constants.JOKE_TYPE_IMG == jokeVerifyInfo.getType()){
					map.put(imgKey, jokeVerifyInfo.getNum());
				}else if(Constants.JOKE_TYPE_TEXT == jokeVerifyInfo.getType()){
					map.put(textKey, jokeVerifyInfo.getNum());
				}
			}
		}
		if(!map.containsKey(textKey)) map.put(textKey, 0);
		if(!map.containsKey(imgKey)) map.put(imgKey, 0);
		if(!map.containsKey(gifKey)) map.put(gifKey, 0);
		return map;
	}
}
