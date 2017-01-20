package com.oupeng.joke.back.task;

import com.oupeng.joke.back.util.HttpUtil;
import com.oupeng.joke.back.util.ImgRespDto;

/**
 * Created by rainy on 2017/1/4.
 */
public class Test {

    @org.junit.Test
    public void test(){
        ImgRespDto imgRespDto = HttpUtil.handleImg("http://172.18.100.56:3000/upload","http://jokeback.bj.oupeng.com/resources/image/1484639960704_9e88b33d-2826-4711-bf24-3343114be2c0.jpg", false);
        System.out.println(imgRespDto.getImgUrl());
        System.out.println(imgRespDto.getWidth());
//        System.out.print("/r");
    }
}
