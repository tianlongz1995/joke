package com.oupeng.joke.front.conf;

import javax.servlet.Filter;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.oupeng.joke.cache.conf.CacheConfig;

public class WebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { FrontConfig.class,CacheConfig.class};
	}

	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { MvcConfig.class };
	}

	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	protected Filter[] getServletFilters() {
		return null;
	}
}
