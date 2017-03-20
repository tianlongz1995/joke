package com.oupeng.joke.spider.task;


import com.oupeng.joke.spider.service.URLBloomFilterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



/**
 * Created by zongchao on 2017/3/18.
 */
@Controller
public class RedisTest {
    private static final Logger logger= LoggerFactory.getLogger(RedisTest.class);
    @Autowired
    private URLBloomFilterService urlBloomFilterService;

    @RequestMapping("/redis")
    public String init(){
        int count=0;
        for(int i=0;i<160000;i++){
            String src="http://www.baidu.com/detail_"+i+".html";
            if(!urlBloomFilterService.contains(src)) {
                urlBloomFilterService.add(src);
            }
        }
        for(int j=0;j<160000;j++){
            String src="http://www.baidu.com/detail_"+j+".html";
            if(!urlBloomFilterService.contains(src)){
                count++;
            }
        }
        System.out.println("count:"+count);
        return "index";
    }

}
