package com.yzg.toutiao.service;

import com.github.pagehelper.PageHelper;
import com.yzg.toutiao.dao.CommentMapper;
import com.yzg.toutiao.model.Comment;
import com.yzg.toutiao.model.example.CommentExample;
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

    @Autowired
    CommentMapper commentMapper;

    /**
     * 插入一条回答
     *
     * @param entityId
     * @param content
     * @param userId
     */
    public void insertComment(int entityId, String content, int userId) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUserId(userId);
        comment.setCreadtedDate(new Date());
        comment.setEntityId(entityId);
        //评论类型，1表示问题的回答
        comment.setEntityType(1);
        commentMapper.insertSelective(comment);
    }

    /**
     * 查询某个问题的回答
     *
     * @param questionId
     * @param offset
     * @param limit
     * @return
     */
    public List<Comment> getCommentsByQuestionId(int questionId, int offset, int limit) {

        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andEntityIdEqualTo(questionId)
                .andEntityTypeEqualTo(1);
        if (limit != 0){
            PageHelper.offsetPage(offset,limit);
        }
        return commentMapper.selectByExample(commentExample);
    }
}
