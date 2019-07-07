package com.yzg.toutiao;

import com.yzg.toutiao.dao.QuestionMapper;
import com.yzg.toutiao.dao.UserMapper;
import com.yzg.toutiao.model.Question;
import com.yzg.toutiao.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
@Sql("/init-schema.sql")
public class DatabaseTest {

    @Autowired
    UserMapper userMapper;
    @Autowired
    QuestionMapper questionMapper;

    @Test
    public void database() {
        Random random = new Random();
        for (int i = 0; i < 11; i++) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",
                    random.nextInt(1000)));
            user.setName(String.format("USER%d", i));
            user.setPassword("123");
            user.setSalt("");
            userMapper.insert(user);

            Question question = new Question();
            question.setCommentCount(0);
            question.setCreatedDate(new Date());
            question.setContent(String.format("ahhahaha %d", i));
            question.setUserId(i + 1);
            question.setTitle(String.format("TITLE{%d}", i));
            questionMapper.insertSelective(question);

        }

    }

    @Test
    public void question() {

    }
}
