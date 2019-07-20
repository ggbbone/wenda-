package com.yzg.toutiao.service;

import com.github.pagehelper.PageHelper;
import com.yzg.toutiao.aspect.LogAspect;
import com.yzg.toutiao.dao.*;
import com.yzg.toutiao.model.*;
import com.yzg.toutiao.model.example.CommentUserExample;
import com.yzg.toutiao.model.example.ReplyUserExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author yzg
 * @create 2019/7/9
 */
@Service
@Transactional
public class CommentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    CommentMapper commentMapper;
    @Autowired
    CommentUserMapper commentUserMapper;
    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    ReplyUserMapper replyUserMapper;

    /**
     * 插入一条回答或评论
     *
     * @param entityId
     * @param content
     * @param userId
     */
    public void insertComment(int entityType, int entityId, String content, int userId) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUserId(userId);
        comment.setCreatedDate(new Date());
        comment.setEntityId(entityId);
        comment.setEntityType(entityType);
        commentMapper.insertSelective(comment);
        if (entityType == 1) {
            //更改问题的回答数量
            questionMapper.addCommentCount(1, entityId);
        } else if (entityType == 2) {
            //更改回答的评论数量
            commentMapper.addCommentCount(1, entityId);
        }
    }

    /**
     * 查询评论
     *
     * @param entityType
     * @param entityId
     * @param offset
     * @param limit
     * @param orderBy
     * @return
     */
    public List<CommentUser> getComments(int entityType, int entityId, int offset,
                                         int limit, String orderBy, int desc) {

        CommentUserExample commentUserExample = new CommentUserExample();
        if (desc == 1) {
            orderBy += " desc";
        }
        commentUserExample.setOrderByClause(orderBy);
        commentUserExample.createCriteria().andEntityIdEqualTo(entityId)
                .andEntityTypeEqualTo(entityType).andStateEqualTo((byte) 1);

        PageHelper.offsetPage(offset, limit);

        List<CommentUser> comments = commentUserMapper.selectByExample(commentUserExample);
        //当查询问题下的评论时,同时查出2条该评论的回复
        if (entityType == 2) {
            for (CommentUser comment : comments) {
                ReplyUserExample replyUserExample = new ReplyUserExample();
                replyUserExample.setOrderByClause("created_date");
                replyUserExample.createCriteria().andCommentIdEqualTo(comment.getId()).andStateEqualTo((byte) 1);
                PageHelper.offsetPage(0, 2);
                List<ReplyUser> replies = replyUserMapper.selectByExample(replyUserExample);
                comment.setReplies(replies);
            }
        }

        return comments;
    }
}
