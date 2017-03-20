package com.oupeng.joke.spider.utils;

import redis.clients.jedis.Jedis;

import java.util.BitSet;

/**
 * Created by zongchao on 2017/3/17.
 */
public class RedisBitSet extends BitSet{
    private Jedis jedis;
    private String name;

    public RedisBitSet( Jedis jedis, String name) {
        this.jedis = jedis;
        this.name = name;
    }

    @Override
    public void set(int bitIndex, boolean value) {
        jedis.setbit(name,bitIndex, value);
    }

    @Override
    public boolean get(int bitIndex) {
        return jedis.getbit(name,bitIndex);
    }
}
