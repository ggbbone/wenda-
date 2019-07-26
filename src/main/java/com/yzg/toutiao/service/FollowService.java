package com.yzg.toutiao.service;

import com.yzg.toutiao.utils.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author yzg
 * @create 2019/7/23
 * <p>
 * 关注功能
 */
@Service
public class FollowService {
    @Autowired
    JedisAdapter jedisAdapter;

    /**
     * 关注
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean follow(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtils.getFollowerKey(entityType, entityId);
        String followeeKey = RedisKeyUtils.getFolloweeKey(userId, entityType);
        Date date = new Date();
        Jedis jedis = jedisAdapter.getJedis();
        Transaction transaction = jedisAdapter.multi(jedis);
        transaction.zadd(followerKey, date.getTime(), String.valueOf(userId));
        transaction.zadd(followeeKey, date.getTime(), String.valueOf(entityId));
        List<Object> list = jedisAdapter.exec(transaction, jedis);
        return list.size() == 2 && (long) list.get(0) > 0 && (long) list.get(1) > 0;
    }

    /**
     * 取消关注
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean unfollow(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtils.getFollowerKey(entityType, entityId);
        String followeeKey = RedisKeyUtils.getFolloweeKey(userId, entityType);
        Jedis jedis = jedisAdapter.getJedis();
        Transaction transaction = jedisAdapter.multi(jedis);
        transaction.zrem(followerKey, String.valueOf(userId));
        transaction.zrem(followeeKey, String.valueOf(entityId));
        List<Object> list = jedisAdapter.exec(transaction, jedis);
        return list.size() == 2 && (long) list.get(0) > 0 && (long) list.get(1) > 0;
    }

    /**
     * 获取关注他的用户列表
     *
     * @param entityType
     * @param entityId
     * @param offset
     * @param end
     * @return List(userId)
     */
    public List<Integer> getFollowers(int entityType, int entityId, int offset, int end) {
        String followerKey = RedisKeyUtils.getFollowerKey(entityType, entityId);
        return getIntegersFromString(jedisAdapter.zrange(followerKey, offset, end));

    }

    /**
     * 获取他对某类实体的关注列表,按时间从后往前
     *
     * @param entityType 实体类型
     * @param userId     用户id
     * @param offset     开始数
     * @param end        结束数
     * @return
     */
    public List<Integer> getFollowees(int entityType, int userId, int offset, int end) {
        String followeeKey = RedisKeyUtils.getFolloweeKey(userId, entityType);
        return getIntegersFromString(jedisAdapter.zrevrange(followeeKey, offset, end));
    }

    /**
     * 统计用户对某类实体的关注数量
     *
     * @param userId
     * @param entityType
     * @return
     */
    public long getFolloweeCounts(int userId, int entityType) {
        String followeeKey = RedisKeyUtils.getFolloweeKey(userId, entityType);
        return jedisAdapter.zcard(followeeKey);
    }


    /**
     * 统计关注某个实体的人数
     *
     * @param entityType
     * @param entityId
     * @return
     */
    public long getFollowerCounts(int entityType, int entityId) {
        String followerKey = RedisKeyUtils.getFollowerKey(entityType, entityId);
        return jedisAdapter.zcard(followerKey);
    }

    private List<Integer> getIntegersFromString(Set<String> strings) {
        List<Integer> integers = new ArrayList<>();
        for (String s : strings) {
            integers.add(Integer.parseInt(s));
        }
        return integers;
    }


    /**
     * 判断是否关注某个实体
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean isFollower(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtils.getFollowerKey(entityType, entityId);
        Double zscore = jedisAdapter.zscore(followerKey, String.valueOf(userId));
        return zscore != null;
    }
}
