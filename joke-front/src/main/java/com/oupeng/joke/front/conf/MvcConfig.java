package com.oupeng.joke.front.conf;

import net.sf.ehcache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@EnableWebMvc
@EnableAspectJAutoProxy
@EnableScheduling
@ComponentScan(basePackages = {"com.oupeng.joke.front.controller", "com.oupeng.joke.front.service", "com.oupeng.joke.front.task"})
public class MvcConfig extends WebMvcConfigurerAdapter {
 
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public InternalResourceViewResolver jspViewResolver() {
        InternalResourceViewResolver bean = new InternalResourceViewResolver();
        bean.setViewClass(org.springframework.web.servlet.view.JstlView.class);
        bean.setPrefix("/WEB-INF/page/");
        bean.setSuffix(".jsp");
        return bean;
    }

    /**
     * 配置EhCacheCacheManager缓存管理器
     */
    @Bean
    public CacheManager getCacheManager() {
        return CacheManager.create(this.getClass().getClassLoader().getResourceAsStream("ehcache.xml"));
    }

}