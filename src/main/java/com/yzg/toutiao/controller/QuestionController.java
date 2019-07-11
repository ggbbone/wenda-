package com.yzg.toutiao.controller;

import com.yzg.toutiao.dao.QuestionMapper;
import com.yzg.toutiao.model.HostHolder;
import com.yzg.toutiao.model.Question;
import com.yzg.toutiao.model.Result;
import com.yzg.toutiao.model.User;
import com.yzg.toutiao.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

/**
 * @author yzg
 * @create 2019/6/27
 */
@Controller
@RequestMapping(value = "questions")
public class QuestionController {

    @Autowired
    QuestionService questionService;
    @Autowired
    HostHolder hostHolder;

    /**
     * 查询问题列表
     * @param offset
     * @param limit
     * @param orderBy
     * @param desc
     * @param question
     * @return
     */
    @RequestMapping(value = "")
    @ResponseBody
    public Result getQuestions(@RequestParam(defaultValue = "0") int offset,
                               @RequestParam(defaultValue = "10") int limit,
                               @RequestParam(defaultValue = "created_date") String orderBy,
                               @RequestParam(defaultValue = "1") int desc,
                               Question question){
        List<Question> questions =
                questionService.getQuestions(null, offset, limit, orderBy, desc);
        return new Result().success().data(questions);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public Result insertQuestion(@RequestBody Question question){
        try {
            //获取当前登陆用户
            User user = hostHolder.getUser();
            if (user != null){
                questionService.insertQuestion(user, question.getTitle(), question.getContent());
                return new Result().success();
            }else {
                return new Result().code(403).message("请先登录");
            }
        }catch (Exception e){
            return new Result().code(1).message("未知错误:"+e.getMessage());
        }

    }

    @RequestMapping(value = "/{questionId}",method = RequestMethod.GET)
    public String toQuestion(@PathVariable(value = "questionId") int questionId){

        return "detail";
    }
}
