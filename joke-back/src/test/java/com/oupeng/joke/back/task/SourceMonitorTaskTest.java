package com.oupeng.joke.back.task;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.oupeng.joke.back.BaseTest;

public class SourceMonitorTaskTest extends BaseTest {

	@Autowired
	private SourceMonitorTask sourceMonitorTask;
	
	@Test
	public void testUpdateSourceMonitor() {
		sourceMonitorTask.updateSourceMonitor();
	}

}
