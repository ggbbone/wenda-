package com.yzg.toutiao.controller;

import com.yzg.toutiao.dao.QuestionMapper;
import com.yzg.toutiao.model.Question;
import com.yzg.toutiao.model.Result;
import com.yzg.toutiao.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DecimalFormat;
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
}
