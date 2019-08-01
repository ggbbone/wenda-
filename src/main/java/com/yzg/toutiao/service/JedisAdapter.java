package com.yzg.toutiao.service;

import com.yzg.toutiao.aspect.LogAspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author yzg
 * @create 2019/7/20
 * redis
 */
@Service
public class JedisAdapter implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    private JedisPool pool;

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("redis://localhost:6379");
    }


    /**
     * 向Set中添加value
     * @param key
     * @param value
     * @return
     */
    public long sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key, value);
        } catch (Exception e) {
            LOGGER.error("发生异常：" + e.getMessage());
        } finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    /**
     * 获取Set的元素数量
     * @param key
     * @return
     */
    public long scard(String key){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            LOGGER.error("发生异常：" + e.getMessage());
        } finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    /**
     * 判断Set中是否存在value
     * @param key
     * @param value
     * @return
     */
    public boolean sismember(String key, String value){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, value);
        } catch (Exception e) {
            LOGGER.error("发生异常：" + e.getMessage());
        } finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return false;
    }

    /**
     * 从Set中移除value
     * @param key
     * @param value
     * @return
     */
    public long srem(String key,String value){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key,value);
        } catch (Exception e) {
            LOGGER.error("发生异常：" + e.getMessage());
        } finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    /**
     * 插入List的左边第一位
     * @param key
     * @param value
     * @return
     */
    public long lpush(String key,String value){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key,value);
        } catch (Exception e) {
            LOGGER.error("发生异常：" + e.getMessage());
        } finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    /**
     * 阻塞读取List右侧
     * @param time
     * @param key
     * @return
     */
    public List<String> brpop(int time, String key){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(time, key);
        } catch (Exception e) {
            LOGGER.error("发生异常：" + e.getMessage());
        } finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return null;
    }

    /**
     * 插入一个key
     * @param key
     * @param value
     * @return
     */
    public String set(String key, String value){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.set(key, value);
        } catch (Exception e) {
            LOGGER.error("发生异常：" + e.getMessage());
        } finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return null;
    }

    /**
     * 插入key同时设置超时时间毫秒数
     * @param key
     * @param value
     * @param time
     * @return
     */
    public String set(String key, String value, long time){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key, value);
            jedis.pexpire(key, time);
        } catch (Exception e) {
            LOGGER.error("发生异常：" + e.getMessage());
        } finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return null;
    }

    /**
     * 插入key同时设置超时时间秒数
     * @param key
     * @param value
     * @param time
     * @return
     */
    public String set(String key, String value, int time){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key, value);
            jedis.expire(key, time);
        } catch (Exception e) {
            LOGGER.error("发生异常：" + e.getMessage());
        } finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return null;
    }

    /**
     * 读取key
     * @param key
     * @return
     */
    public String get(String key){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.get(key);
        } catch (Exception e) {
            LOGGER.error("发生异常：" + e.getMessage());
        } finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return null;
    }


    /**
     * 向zSet添加一个或多个成员，或者更新已存在成员的分数
     * @param key
     * @param score
     * @param value
     * @return
     */
    public long zadd(String key, double score, String value){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zadd(key, score, value);
        } catch (Exception e) {
            LOGGER.error("发生异常：" + e.getMessage());
        } finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    /**
     * 通过索引区间返回有序集合成指定区间内的成员，按分数从小到大
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> zrange(String key, long start, long end){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrange(key, start, end);
        } catch (Exception e) {
            LOGGER.error("发生异常：" + e.getMessage());
        } finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return null;
    }

    /**
     * 通过索引区间返回有序集合成指定区间内的成员，按分数从大到小
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> zrevrange(String key, long start, long end){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            LOGGER.error("发生异常：" + e.getMessage());
        } finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return null;
    }

    /**
     * 返回有序集中指定分数区间内的成员，分数从高到低排序
     * @param key
     * @param start
     * @param stop
     * @return
     */
    public Set<String> zrevrangeByScore(String key, double start, double stop){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrangeByScore(key, start, stop);
        } catch (Exception e) {
            LOGGER.error("发生异常：" + e.getMessage());
        } finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return null;
    }



    /**
     * 获取zSet数量
     * @param key
     * @return
     */
    public long zcard(String key){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zcard(key);
        } catch (Exception e) {
            LOGGER.error("发生异常：" + e.getMessage());
        } finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    /**
     * 返回zSet中某个member的分数值,
     * @param key
     * @return
     */
    public Double zscore(String key, String member){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zscore(key,member);
        } catch (Exception e) {
            LOGGER.error("发生异常：" + e.getMessage());
        } finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return null;
    }


    public Jedis getJedis(){
        return pool.getResource();
    }

    /**
     * redis事务
     * @param jedis
     * @return
     */
    public Transaction multi(Jedis jedis){
        try {
            return jedis.multi();
        } catch (Exception e){
            LOGGER.error("发生异常：" + e.getMessage());
        }
        return null;
    }
    public List<Object> exec(Transaction tx, Jedis jedis){
        try {
            return tx.exec();
        } catch (Exception e){
            LOGGER.error("发生异常：" + e.getMessage());
        } finally {
            if (tx != null){
                tx.close();
            }
            if (jedis != null){
                jedis.close();
            }
        }
        return null;
    }
}
