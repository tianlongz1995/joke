package com.oupeng.joke.back;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.oupeng.joke.back.conf.BackConfig;
import com.oupeng.joke.back.conf.MvcConfig;
import com.oupeng.joke.back.conf.SecurityConfig;
import com.oupeng.joke.cache.conf.CacheConfig;
import com.oupeng.joke.dao.conf.DatabaseConfig;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration 
/*@ContextHierarchy({
	@ContextConfiguration(classes=BackConfig.class),
	@ContextConfiguration(classes=DatabaseConfig.class),
	@ContextConfiguration(classes=CacheConfig.class),
	@ContextConfiguration(classes=SecurityConfig.class),
	@ContextConfiguration(classes=MvcConfig.class)
})*/
@ContextConfiguration(classes={BackConfig.class,DatabaseConfig.class,CacheConfig.class,SecurityConfig.class,MvcConfig.class})
public class BaseTest {
	
}
