package com.yzg.toutiao.controller;

import com.yzg.toutiao.model.Comment;
import com.yzg.toutiao.model.HostHolder;
import com.yzg.toutiao.model.Result;
import com.yzg.toutiao.service.CommentService;
import org.apache.catalina.Host;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yzg
 * @create 2019/7/9
 */
@Controller
@RequestMapping(value = "comments")
public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    HostHolder hostHolder;

    @RequestMapping(value = "/question/{questionId}", method = RequestMethod.GET)
    @ResponseBody
    public Result getCommentsByQuestionId(
            @PathVariable(value = "questionId") int questionId,
            @RequestParam(defaultValue = "0", required = false) int offset,
            @RequestParam(defaultValue = "5", required = false) int limit) {
        List<Comment> comments = commentService.getCommentsByQuestionId(questionId, offset, limit);

        return new Result().success().data(comments);
    }

    @RequestMapping(value = "/question/{questionId}", method = RequestMethod.POST)
    @ResponseBody
    public Result insertCommentWithQuestion(
            @PathVariable(value = "questionId") int questionId,
            @RequestParam String content) {
        if (hostHolder.getUser() != null) {
            commentService.insertComment(questionId, content, hostHolder.getUser().getId());
        }
        return new Result().success();
    }
}
