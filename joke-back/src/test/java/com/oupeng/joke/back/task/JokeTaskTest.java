package com.oupeng.joke.back.task;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.oupeng.joke.back.BaseTest;

public class JokeTaskTest extends BaseTest{

	@Autowired
	private JokeTask jokeTask;
	
	@Test
	public void testPublishJoke() {
		jokeTask.publishJoke();
	}
}
