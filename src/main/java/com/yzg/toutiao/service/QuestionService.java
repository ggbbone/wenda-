package com.yzg.toutiao.service;

import com.github.pagehelper.PageHelper;
import com.yzg.toutiao.aspect.LogAspect;
import com.yzg.toutiao.dao.QuestionMapper;
import com.yzg.toutiao.dao.UserMapper;
import com.yzg.toutiao.model.Question;
import com.yzg.toutiao.model.example.QuestionExample;
import com.yzg.toutiao.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author yzg
 * @create 2019/6/27
 */
@Service
@Transactional
public class QuestionService {

    private static final Logger LOGGER=LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    public List<Question> getQuestions(Question question, int offset,
                                       int limit, String orderBy,int desc) {

        QuestionExample example = new QuestionExample();
        if (desc==1){//按条件倒序排序
            orderBy=orderBy+" desc";
        }
        example.setOrderByClause(orderBy);
        PageHelper.offsetPage(offset, limit);
        List<Question> questions = questionMapper.selectByExample(example);
        //查询提问用户
        for (Question question1 : questions){
            User user = userMapper.selectByPrimaryKey(question1.getUserId());
            if (user!=null){
                user.setPassword(null);
                user.setSalt(null);
                question1.setUser(user);
            }
        }
        return questions;
    }
}
