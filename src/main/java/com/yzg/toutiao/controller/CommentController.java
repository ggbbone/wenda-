package com.yzg.toutiao.controller;

import com.github.pagehelper.PageInfo;
import com.yzg.toutiao.annotation.LoginRequired;
import com.yzg.toutiao.aspect.LogAspect;
import com.yzg.toutiao.model.*;
import com.yzg.toutiao.service.CommentService;
import com.yzg.toutiao.service.ReplyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yzg
 * @create 2019/7/9
 * <p>
 * 评论中心
 */
@Controller
@RequestMapping(value = "comments")
public class CommentController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    CommentService commentService;
    @Autowired
    ReplyService replyService;
    @Autowired
    HostHolder hostHolder;

    /**
     * 查询评论下的回复
     *
     * @param commentId
     * @param offset
     * @param limit
     * @param orderBy
     * @param desc
     * @return
     */
    @RequestMapping(value = "/{commentId}/reply")
    @ResponseBody
    public Result getReplies(
            @PathVariable(value = "commentId") int commentId,
            @RequestParam(defaultValue = "0", required = false) int offset,
            @RequestParam(defaultValue = "5", required = false) int limit,
            @RequestParam(defaultValue = "created_date", required = false) String orderBy,
            @RequestParam(defaultValue = "0", required = false) int desc) {

        List<ReplyUser> replies = replyService.selectRepliesByCommentId(commentId, offset,
                limit, orderBy, desc);
        return new Result().success().data(replies);
    }

    /**
     * 插入1条回复
     * @param commentId
     * @param airId
     * @param content
     * @return
     */
    @LoginRequired
    @RequestMapping(value = "/{commentId}/reply/{airId}", method = RequestMethod.POST)
    @ResponseBody
    public Result insertReply(@PathVariable(value = "commentId") int commentId,
                              @PathVariable(value = "airId") int airId,
                              @RequestParam String content){
        User user = hostHolder.getUser();
        replyService.insertReply(user.getId(),airId,commentId,content);

        return new Result().success();
    }

    /**
     * 查询评论
     *
     * @param entityType 评论类型
     *                   1：问题下的回答
     *                   2：回答下的评论
     * @param entityId   评论来源id(如查询问题下的回答,则是问题的id)
     * @param offset
     * @param limit
     * @return
     */
    @RequestMapping(value = "/{entityType}/{entityId}", method = RequestMethod.GET)
    @ResponseBody
    public Result getCommentsByQuestionId(
            @PathVariable(value = "entityId") int entityId,
            @PathVariable(value = "entityType") int entityType,
            @RequestParam(defaultValue = "0", required = false) int offset,
            @RequestParam(defaultValue = "5", required = false) int limit,
            @RequestParam(defaultValue = "created_date", required = false) String orderBy,
            @RequestParam(defaultValue = "1", required = false) int desc) {
        List<CommentUser> comments = commentService.getComments(
                entityType, entityId, offset, limit, orderBy, desc);
        PageInfo<CommentUser> pageInfo = new PageInfo<>(comments);
        return new Result().success().data(pageInfo);
    }

    /**
     * @param entityType 评论类型
     *                   1：问题下的回答
     *                   2：回答下的评论
     * @param entityId   目标id
     * @param content
     * @return
     */
    @LoginRequired
    @RequestMapping(value = "/{entityType}/{entityId}", method = RequestMethod.POST)
    @ResponseBody
    public Result insertCommentWithQuestion(
            @PathVariable(value = "entityType") int entityType,
            @PathVariable(value = "entityId") int entityId,
            @RequestParam String content) {
        if (hostHolder.getUser() != null) {
            commentService.insertComment(entityType, entityId, content, hostHolder.getUser().getId());
        }
        return new Result().success();
    }
}
