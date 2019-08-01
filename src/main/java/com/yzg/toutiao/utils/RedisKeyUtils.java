package com.yzg.toutiao.utils;

/**
 * @author yzg
 * @create 2019/7/20
 * 负责产生存入redis的key值
 */
public class RedisKeyUtils {

    private static String SPLIT = "_";
    //点赞用户
    private static String BIZ_LIKE = "LIKE";
    //点踩用户
    private static String BIZ_DISLIKE = "DISLIKE";
    //事件队列
    private static String BIZ_EVENT_QUEUE = "EVENT_QUEUE";
    //问题
    private static String BIZ_QUESTION = "QUESTION";
    //邮件注册验证码
    private static String BIZ_REGISTER_CODE = "REGISTER_CODE";
    //关注他的人
    private static String BIZ_FOLLOWER = "FOLLOWER";
    //他关注的的人
    private static String BIZ_FOLLOWEE = "FOLLOWEE";
    //登陆token
    private static String BIZ_LOGIN_TOKEN = "LOGIN_TOKEN";
    /**
     * 获取实体点赞列表key值
     * @param entityType
     * @param entityId
     * @return
     */
    public static String getLikeKey(int entityType, int entityId){
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) +
                SPLIT + String.valueOf(entityId);
    }

    /**
     * 获取实体点踩列表key值
     * @param entityType
     * @param entityId
     * @return
     */
    public static String getDisLikeKey(int entityType, int entityId){
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) +
                SPLIT + String.valueOf(entityId);
    }

    /**
     * 获取事件队列key
     * @return
     */
    public static String getEventQueue(){
        return BIZ_EVENT_QUEUE;
    }

    /**
     * 获取问题id获取key
     * @param id
     * @return
     */
    public static String getQuestionById(int id){
        return BIZ_QUESTION + SPLIT + String.valueOf(id);
    }


    /**
     * 根据邮箱获取验证码
     * @param email
     * @return
     */
    public static String getRegisterCodeByUserId(String email){

        return BIZ_REGISTER_CODE + SPLIT + email;
    }

    /**
     * 获取所有关注某个实体的人
     * @return
     */
    public static String getFollowerKey(int entityType, int entityId){
        return BIZ_FOLLOWER + SPLIT + String.valueOf(entityType) +
                SPLIT + String.valueOf(entityId);
    }

    /**
     * 获取某人关注某类实体的信息
     * @param userId
     * @param entityType
     * @return
     */
    public static String getFolloweeKey(int userId, int entityType){
        return BIZ_FOLLOWEE + SPLIT +
                String.valueOf(userId) + SPLIT + String.valueOf(entityType);
    }

}
