package com.yzg.toutiao.service;

import com.github.pagehelper.PageHelper;
import com.yzg.toutiao.dao.CommentMapper;
import com.yzg.toutiao.dao.ReplyMapper;
import com.yzg.toutiao.dao.ReplyUserMapper;
import com.yzg.toutiao.model.Reply;
import com.yzg.toutiao.model.ReplyUser;
import com.yzg.toutiao.model.example.ReplyUserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author yzg
 * @create 2019/7/18
 */
@Service
@Transactional
public class ReplyService {
    @Autowired
    ReplyMapper replyMapper;
    @Autowired
    ReplyUserMapper replyUserMapper;
    @Autowired
    CommentMapper commentMapper;


    /**
     * 查询回复
     * @param commentId
     * @param offset
     * @param limit
     * @param orderBy
     * @param desc
     * @return
     */
    public List<ReplyUser> selectRepliesByCommentId(
            int commentId, int offset, int limit, String orderBy, int desc) {

        ReplyUserExample replyUserExample = new ReplyUserExample();
        if (1 == desc){
            orderBy += " desc";
        }
        replyUserExample.setOrderByClause(orderBy);
        if (0 != limit){
            PageHelper.offsetPage(offset,limit);
        }
        replyUserExample.createCriteria().andCommentIdEqualTo(commentId).andStateEqualTo((byte) 1);
        return replyUserMapper.selectByExample(replyUserExample);

    }

    /**
     * 插入一条回复
     * @param userId
     * @param airId
     * @param commentId
     * @param content
     * @return
     */
    public void insertReply(Integer userId, int airId, int commentId, String content) {
        Reply reply = new Reply();
        reply.setCreatedDate(new Date());
        reply.setUserId(userId);
        reply.setAirId(airId);
        reply.setCommentId(commentId);
        reply.setContent(content);

        replyMapper.insertSelective(reply);
        //修改评论的回复数量
        commentMapper.addCommentCount(1,commentId);

    }
}
