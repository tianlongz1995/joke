package com.oupeng.joke.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class JedisCache {


    @Autowired
    private JedisPool jedisWritePool;

    @Autowired
    private JedisPool jedisReadPool;

    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisWritePool.getResource();
            jedis.set(key, value);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 保存并设置过期时间
     *
     * @param key
     * @param value
     * @param seconds 单位:秒
     */
    public void setAndExpire(String key, String value, int seconds) {
        Jedis jedis = null;
        try {
            jedis = jedisWritePool.getResource();
            jedis.set(key, value);
            jedis.expire(key, seconds);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public List<String> mget(String... keys) {
        Jedis jedis = null;
        try {
            jedis = jedisReadPool.getResource();
            return jedis.mget(keys);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void mset(String... keysvalues) {
        Jedis jedis = null;
        try {
            jedis = jedisWritePool.getResource();
            jedis.mset(keysvalues);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisReadPool.getResource();
            return jedis.get(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void del(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisWritePool.getResource();
            jedis.del(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void hset(String key, String field, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisWritePool.getResource();
            jedis.hset(key, field, value);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void hmset(String key, Map<String, String> map) {
        Jedis jedis = null;
        try {
            jedis = jedisWritePool.getResource();
            jedis.hmset(key, map);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Set<String> hkeys(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisReadPool.getResource();
            return jedis.hkeys(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void hdel(String key, String... fields) {
        Jedis jedis = null;
        try {
            jedis = jedisWritePool.getResource();
            jedis.hdel(key, fields);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public String hget(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = jedisReadPool.getResource();
            return jedis.hget(key, field);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Map<String, String> hgetAll(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisReadPool.getResource();
            return jedis.hgetAll(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void zadd(String key, double score, String member) {
        Jedis jedis = null;
        try {
            jedis = jedisWritePool.getResource();
            jedis.zadd(key, score, member);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Double zscore(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = jedisReadPool.getResource();
            return jedis.zscore(key, member);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void zrem(String key, String... members) {
        Jedis jedis = null;
        try {
            jedis = jedisWritePool.getResource();
            jedis.zrem(key, members);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


    public void srem(String key, String... members) {
        Jedis jedis = null;
        try {
            jedis = jedisWritePool.getResource();
            jedis.srem(key, members);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void zadd(String key, Map<String, Double> value) {
        Jedis jedis = null;
        try {
            jedis = jedisWritePool.getResource();
            jedis.zadd(key, value);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Set<String> zrevrange(String key, Long start, Long end) {
        Jedis jedis = null;
        try {
            jedis = jedisReadPool.getResource();
            return jedis.zrevrange(key, start, end);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Set<String> zrange(String key, Integer start, Integer end) {
        Jedis jedis = null;
        try {
            jedis = jedisReadPool.getResource();
            return jedis.zrange(key, start, end);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Double zmaxScore(String key) {
        Jedis jedis = null;
        Double score = 0d;
        try {
            jedis = jedisReadPool.getResource();
            Set<Tuple> tupleSet = jedis.zrevrangeWithScores(key, 0, 0);
            for (Tuple tuple : tupleSet) {
                score = tuple.getScore();
            }
            return score;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Set<String> smembers(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisReadPool.getResource();
            return jedis.smembers(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void sadd(String key, String... members) {
        Jedis jedis = null;
        try {
            jedis = jedisWritePool.getResource();
            jedis.sadd(key, members);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void lpush(String key, String... values) {
        Jedis jedis = null;
        try {
            jedis = jedisWritePool.getResource();
            jedis.lpush(key, values);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public List<String> brpop(String key, int timeout) {
        Jedis jedis = null;
        try {
            jedis = jedisWritePool.getResource();
            return jedis.brpop(timeout, key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void delAndSet(String key, String[] values) {
        Jedis jedis = null;
        try {
            jedis = jedisWritePool.getResource();
            Transaction transaction = jedis.multi();
            transaction.del(key);
            transaction.sadd(key, values);
            transaction.exec();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void delAndSetSortedSet(String key, Map<String, Double> map) {
        Jedis jedis = null;
        try {
            jedis = jedisWritePool.getResource();
            Transaction transaction = jedis.multi();
            transaction.del(key);
            transaction.zadd(key, map);
            transaction.exec();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }

    public Set<String> keys(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisReadPool.getResource();
            return jedis.keys(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void del(String... keys) {
        Jedis jedis = null;
        try {
            jedis = jedisWritePool.getResource();
            jedis.del(keys);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public List<String> srandmember(String key, int count) {
        Jedis jedis = null;
        try {
            jedis = jedisReadPool.getResource();
            return jedis.srandmember(key, count);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Long zrevrank(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = jedisReadPool.getResource();
            return jedis.zrevrank(key, member);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 获取有序集合元素个数
     *
     * @param key
     * @return
     */
    public Long zcard(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisReadPool.getResource();
            return jedis.zcard(key);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 删除置顶区间内的成员
     *
     * @param key
     * @param start
     * @param end
     */
    public void zremrangebyrank(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = jedisWritePool.getResource();
            jedis.zremrangeByRank(key, start, end);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 获取集合中元素数量
     *
     * @param setKey
     */
    public Long scard(String setKey) {
        Jedis jedis = null;
        try {
            jedis = jedisReadPool.getResource();
            return jedis.scard(setKey);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 获取有序集合中元素-按score从大到小顺序
     * @param key
     * @return
     */
    public Set<String> zrevrangebyscore(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisReadPool.getResource();
            return jedis.zrevrangeByScore(key, Integer.MAX_VALUE, Integer.MIN_VALUE);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}
