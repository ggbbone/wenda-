package com.yzg.toutiao.service;

import com.github.pagehelper.PageHelper;
import com.yzg.toutiao.aspect.LogAspect;
import com.yzg.toutiao.dao.QuestionMapper;
import com.yzg.toutiao.dao.UserMapper;
import com.yzg.toutiao.model.Question;
import com.yzg.toutiao.model.example.QuestionExample;
import com.yzg.toutiao.model.User;
import com.yzg.toutiao.model.example.UserExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
        for (Question question1 : questions){
            //查询提问用户
            User user = userMapper.selectByPrimaryKey(question1.getUserId());
            if (user!=null){
                user.setPassword(null);
                user.setSalt(null);
                question1.setUser(user);
            }
            //设置问题内容长度
            String content = question1.getContent();
            if (content.length()>200){
                question1.setContent(content.substring(0, 200)+"...");
            }
        }
        return questions;
    }

    /**
     * 插入一条问题数据
     * @param user
     * @param title
     * @param content
     */
    public void insertQuestion(User user, String title, String content) {
        Question question = new Question();
        question.setContent(content);
        question.setUserId(user.getId());
        question.setCommentCount(0);
        question.setTitle(title);
        question.setCreatedDate(new Date());

        questionMapper.insertSelective(question);
    }

    /**
     * 根据id查询问题详细信息
     * @param id
     * @return
     */
    public Question getQuestionById(int id){
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question != null){
            User user = new User();
            User user1 = userMapper.selectByPrimaryKey(question.getUserId());
            user.setId(user1.getId());
            user.setHeadUrl(user1.getHeadUrl());
            user.setName(user1.getName());
            question.setUser(user);
        }
        return question;
    }
}
