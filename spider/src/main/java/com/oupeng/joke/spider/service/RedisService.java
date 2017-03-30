package com.oupeng.joke.spider.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;


/**
 * Created by zongchao on 2017/3/17.
 */
@Component
public class RedisService {

    private String host;
    private Integer port;
    private Integer timeOut;

    private JedisPool jedisPool;

    @Autowired
    private Environment env;

    @PostConstruct
    public void init() {
        String h = env.getProperty("spider.redis.host");
        if (StringUtils.isNotBlank(h)) {
            host = h;
        }
        String p = env.getProperty("spider.redis.port");
        if (StringUtils.isNumeric(p)) {
            port = Integer.valueOf(p);
        }
        String t = env.getProperty("spider.redis.timeout");
        if (StringUtils.isNumeric(t)) {
            timeOut = Integer.valueOf(t.toString());
        }
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxWaitMillis(-1);

        jedisPool = new JedisPool(jedisPoolConfig, host, port, 0);
    }

    public Jedis getJedis() {
        return jedisPool.getResource();
    }


}
