package com.oupeng.joke.back.task;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.oupeng.joke.back.BaseTest;

public class RecommendChannelTaskTest extends BaseTest {

	@Autowired
	private RecommendChannelTask recommendChannelTask;
	
	@Test
	public void testPublishRecommendChannelJoke() {
		recommendChannelTask.publishRecommendChannelJoke();
	}

}
