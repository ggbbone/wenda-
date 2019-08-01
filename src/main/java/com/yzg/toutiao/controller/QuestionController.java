package com.yzg.toutiao.controller;

import com.yzg.toutiao.annotation.LoginRequired;
import com.yzg.toutiao.dao.QuestionMapper;
import com.yzg.toutiao.model.HostHolder;
import com.yzg.toutiao.model.Question;
import com.yzg.toutiao.model.Result;
import com.yzg.toutiao.model.User;
import com.yzg.toutiao.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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

    /**
     * 发表问题
     * @param question
     * @return
     */
    @LoginRequired
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

    /**
     * 访问问题详情页面
     * @param questionId
     * @param modelAndView
     * @return
     */
    @RequestMapping(value = "/{questionId}",method = RequestMethod.GET)
    public ModelAndView toQuestion(@PathVariable(value = "questionId") int questionId,
                             ModelAndView modelAndView){
        modelAndView.setViewName("question");
        modelAndView.addObject("location","question");
        Question question = questionService.getQuestionById(questionId);
        modelAndView.addObject(question);
        return modelAndView;
    }

    /**
     * 根据标题搜索问题
     * @param kw
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/search",method = RequestMethod.GET)
    public Result getQuestionByLikeTitle(@RequestParam String kw){
        List<Question> questions = questionService.getQuestionByTitle(kw);
        return new Result().success().data(questions);
    }
}
