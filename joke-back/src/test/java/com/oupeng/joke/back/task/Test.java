package com.oupeng.joke.back.task;

import java.io.File;

/**
 * Created by rainy on 2017/1/4.
 */
public class Test {

    @org.junit.Test
    public void test(){
        File file= new File("C:/Users/rainy/joke");
        System.out.println(file.exists());
    }
}
