package com.yzg.toutiao.model;

/**
 * @author yzg
 * @create 2019/7/21
 * <p>
 * 实体类型
 */
public class EntityType {

    /**
     * 回复
     */
    public static int REPLY = 3;
    /**
     * 评论
     */
    public static int COMMENT = 2;
    /**
     * 问题
     */
    public static int QUESTION = 4;
    /**
     * 回答
     */
    public static int ANSWER = 1;
    /**
     * 用户
     */
    public static int USER = 5;

    public static int getTypeInt(String entityType){
        int type;
        if ("comment".equals(entityType)){
            type = COMMENT;
        }else if ("answer".equals(entityType)){
            type = ANSWER;
        }else if ("question".equals(entityType)){
            type = QUESTION;
        }else if ("reply".equals(entityType)){
            type = REPLY;
        }else if ("user".equals(entityType)){
            type = USER;
        }
        else {
            type = -1;
        }
        return type;
    }
}
