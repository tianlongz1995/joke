package com.oupeng.joke.spider.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.BitSet;

/**
 * Created by zongchao on 2017/3/17.
 */
@Component
public class RedisBitSet extends BitSet{

    @Autowired
    private RedisService redisService;

    public void set(String name, int bitIndex, boolean value) {
//        jedis.setbit(name,bitIndex, value);
        redisService.setBit(name, bitIndex, value);
    }

//    @Override
    public boolean get(String name, int bitIndex) {
//        return jedis.getbit(name,bitIndex);
        return redisService.getBit(name, bitIndex);
    }
}
