package com.yzg.toutiao.service;

import com.yzg.toutiao.utils.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yzg
 * @create 2019/7/20
 * 赞踩功能
 */
@Service
public class LikeService {
    @Autowired
    JedisAdapter jedisAdapter;


    /**
     * 点赞
     * @param userId 点赞用户
     * @param entityType 点赞目标类型
     * @param entityId 点赞目标id
     * @return
     */
    public long like(int userId, int entityType, int entityId){
        //添加到点赞集合
        String likeKey = RedisKeyUtils.getLikeKey(entityType, entityId);
        jedisAdapter.sadd(likeKey, String.valueOf(userId));
        //从踩集合中移除
        String disLikeKey = RedisKeyUtils.getDisLikeKey(entityType, entityId);
        jedisAdapter.srem(disLikeKey,String.valueOf(userId));
        //返回当前点赞人数
        return jedisAdapter.scard(likeKey);

    }

    /**
     * 点踩
     * @param userId 点赞用户
     * @param entityType 点赞目标类型
     * @param entityId 点赞目标
     * @return
     */
    public long disLike(int userId, int entityType, int entityId){
        //添加到点踩集合
        String disLikeKey = RedisKeyUtils.getDisLikeKey(entityType, entityId);
        jedisAdapter.sadd(disLikeKey,String.valueOf(userId));
        //从点赞集合中移除
        String likeKey = RedisKeyUtils.getLikeKey(entityType, entityId);
        jedisAdapter.srem(likeKey, String.valueOf(userId));
        //返回当前点赞人数
        return jedisAdapter.scard(likeKey);

    }

    /**
     * 取消点赞或点踩
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public long delLike(int userId, int entityType, int entityId){

        String likeKey = RedisKeyUtils.getLikeKey(entityType, entityId);
        jedisAdapter.srem(likeKey, String.valueOf(userId));
        String disLikeKey = RedisKeyUtils.getDisLikeKey(entityType, entityId);
        jedisAdapter.srem(disLikeKey,String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }

    /**
     * 用户对某个entity的赞踩状态
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public int getLikeStatus(int userId, int entityType, int entityId){
        String likeKey = RedisKeyUtils.getLikeKey(entityType, entityId);
        if (jedisAdapter.sismember(likeKey,String.valueOf(userId))){
            //赞
            return 1;
        }
        String disLikeKey = RedisKeyUtils.getDisLikeKey(entityType, entityId);
        //踩返回 -1，不赞不踩返回0
        return jedisAdapter.sismember(disLikeKey,String.valueOf(userId) ) ? -1 : 0;
    }

    /**
     * 点赞人数
     * @param entityType
     * @param entityId
     * @return
     */
    public long getLikeCount(int entityType, int entityId){
        String likeKey = RedisKeyUtils.getLikeKey(entityType, entityId);
        return jedisAdapter.scard(likeKey);
    }

}
