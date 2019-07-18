package com.yzg.toutiao.controller;

import com.github.pagehelper.PageInfo;
import com.yzg.toutiao.annotation.LoginRequired;
import com.yzg.toutiao.aspect.LogAspect;
import com.yzg.toutiao.model.CommentUser;
import com.yzg.toutiao.model.HostHolder;
import com.yzg.toutiao.model.Result;
import com.yzg.toutiao.model.User;
import com.yzg.toutiao.service.CommentService;
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
    private static final Logger LOGGER=LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    CommentService commentService;
    @Autowired
    HostHolder hostHolder;


    /**
     * 查询评论
     * @param entityType 评论类型
     *                   1：问题下的回答
     *                   2：回答下的评论
     *                   3：评论下的回复
     * @param entityId   目标
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
                entityType,entityId, offset, limit, orderBy, desc);
        PageInfo<CommentUser> pageInfo = new PageInfo<>(comments);
        return new Result().success().data(pageInfo);
    }

    /**
     *
     * @param entityType 评论类型
     *                   1：问题下的回答
     *                   2：回答下的评论
     *                   3：评论下的回复
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
            commentService.insertComment(entityType,entityId, content, hostHolder.getUser().getId());
        }
        return new Result().success();
    }
}
