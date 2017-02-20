package com.oupeng.joke.back.task;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.oupeng.joke.back.BaseTest;

public class DistributorTasTest extends BaseTest {

	@Autowired
	private DistributorTask distributorTask;
	
	@Test
	public void testUpdateSourceMonitor() {
        distributorTask.syncDistributorAdConfig();
	}

}
