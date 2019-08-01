package com.yzg.toutiao;

import com.alibaba.fastjson.JSONObject;
import com.yzg.toutiao.dao.QuestionMapper;
import com.yzg.toutiao.dao.UserMapper;
import com.yzg.toutiao.model.Comment;
import com.yzg.toutiao.model.EntityType;
import com.yzg.toutiao.model.Question;
import com.yzg.toutiao.model.User;
import com.yzg.toutiao.service.CommentService;
import com.yzg.toutiao.service.JedisAdapter;
import com.yzg.toutiao.service.QuestionService;
import com.yzg.toutiao.service.UserService;
import com.yzg.toutiao.utils.RedisKeyUtils;
import com.yzg.toutiao.utils.WendaUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @author yzg
 * @create 2019/7/22
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
public class RedisTest {

    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    QuestionService questionService;
    @Autowired
    CommentService commentService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    JedisAdapter jedisAdapter;

    @Test
    public void test() {

        List<Question> questions = questionMapper.selectByExample(null);
        for (Question question : questions) {
            String value = JSONObject.toJSONString(question);
            String key = RedisKeyUtils.getQuestionById(question.getId());
            jedisAdapter.set(key, value);
        }
    }

    /**
     * 插入1000个用户
     */
    @Test
    public void test2() {
        User user = new User();
        for (int i = 10; i < 1000; i++) {
            user.setId(i);
            user.setName("测试用户" + i);
            user.setEmail("test" + i + "@wenda.com");
            String password = "123456";
            user.setState((byte) 1);
            user.setSalt(UUID.randomUUID().toString().substring(0, 5));
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",
                    new Random().nextInt(1000)));
            //加密密码
            user.setPassword(WendaUtil.MD5(password + user.getSalt()));
            userMapper.insert(user);
        }
    }

    /**
     * 问题数据插入
     */
    @Test
    public void test3(){
        for (int i=10;i<900;i+=5){
            User user = new User();
            user.setId(i);
            String title="问题的测试数据标题"+i+"?";
            String content="测试一下问题，测试一下问题, 测试一下问题"+i;
            questionService.insertQuestion(user,title,content);
        }
    }

    /**
     * 回答插入
     */
    @Test
    public void test4(){
        for (int questionId=500;questionId<900;questionId+=2){
            for (int userId = 10;userId<900;userId+=100){
                String content="问答测试"+UUID.randomUUID().toString();
                commentService.insertComment(EntityType.ANSWER,questionId,content,userId);
            }
        }
    }



}
