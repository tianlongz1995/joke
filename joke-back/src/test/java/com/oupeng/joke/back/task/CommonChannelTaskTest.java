package com.oupeng.joke.back.task;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.oupeng.joke.back.BaseTest;

public class CommonChannelTaskTest extends BaseTest{

	@Autowired
	private CommonChannelTask commonChannelTask;
	@Test
	public void testPublishCommonChannelJoke() {
		commonChannelTask.publishCommonChannelJoke();
	}

}
