package com.oupeng.joke.back.task;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.oupeng.joke.back.BaseTest;

public class TopicChannelTaskTest extends BaseTest {

	@Autowired
	private TopicChannelTask topicChannelTask;
	
	@Test
	public void testPublishTopicChannelJoke() {
		topicChannelTask.publishTopicChannelJoke();
	}

}
