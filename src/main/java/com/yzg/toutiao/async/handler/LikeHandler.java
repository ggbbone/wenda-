package com.yzg.toutiao.async.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yzg.toutiao.async.EventHandler;
import com.yzg.toutiao.async.EventModel;
import com.yzg.toutiao.async.EventType;
import com.yzg.toutiao.model.CommentUser;
import com.yzg.toutiao.model.Message;
import com.yzg.toutiao.model.Question;
import com.yzg.toutiao.service.*;
import com.yzg.toutiao.utils.RedisKeyUtils;
import com.yzg.toutiao.utils.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author yzg
 * @create 2019/7/21
 * <p>
 * 点赞相关handler
 */
@Component
public class LikeHandler implements EventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(LikeHandler.class);
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    QuestionService questionService;
    @Autowired
    JedisAdapter jedisAdapter;

    @Override
    public void doHandle(EventModel model) {
        //点赞后发送消息
        if (1 == model.getEntityType()) {
            //回答的点赞
            int commentId = model.getEntityId();
            //获取点赞目标评论
            CommentUser comment = commentService.selectCommentById(commentId);
            if (comment != null) {
                //从redis获取点赞目标问题
                String key = RedisKeyUtils.getQuestionById(comment.getEntityId());
                String json = jedisAdapter.get(key);
                Question question = JSON.parseObject(json, Question.class);
                String title = question.getTitle();
                if (title.length() > 20) {
                    title = title.substring(0, 20) + "...";
                }
                Message message = new Message();
                message.setFromId(model.getActorId());
                message.setFromName(comment.getUserName());
                message.setToId(comment.getUserId());
                message.setContent("赞了你的回答");
                message.setEntityId(model.getEntityId());
                message.setEntityType((byte) model.getEntityType());
                message.setEntityTitle(title);
                message.setEntityUrl("http://127.0.0.1/wenda/quesions/" +
                        question.getId() + "/answer/" + comment.getId());
                messageService.insertMessage(message);
            }
        }else if (2 == model.getEntityType()){
            //评论的点赞
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Collections.singletonList(EventType.LIKE);
    }
}
