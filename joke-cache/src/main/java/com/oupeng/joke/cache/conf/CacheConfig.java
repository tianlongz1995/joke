package com.oupeng.joke.cache.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com.oupeng.joke.cache"})
public class CacheConfig {
    
    @Value("${redis.write.host}")
    private String writeHost;
    
    @Value("${redis.read.host}")
    private String readHost;
    
    @Value("${redis.port}")
    private Integer port;
    
    @Value("${redis.timeout}")
    private Integer timeout;

    @Bean(name = "jedisWritePool")
    public JedisPool jedisWritePool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxWaitMillis(60000);
        config.setMaxTotal(1000);
        config.setMinIdle(50);
        config.setTestOnBorrow(true);
        config.setTestWhileIdle(true);
        return new JedisPool(config, writeHost, port, timeout);
    }
    
    @Bean(name = "jedisReadPool")
    public JedisPool jedisReadPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxWaitMillis(60000);
        config.setMaxTotal(1000);
        config.setMinIdle(50);
        config.setTestOnBorrow(true);
        config.setTestWhileIdle(true);
        return new JedisPool(config, readHost, port, timeout);
    }

}
