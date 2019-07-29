package com.yzg.toutiao.service;

import com.yzg.toutiao.aspect.LogAspect;
import com.yzg.toutiao.async.EventModel;
import com.yzg.toutiao.async.EventProducer;
import com.yzg.toutiao.async.EventType;
import com.yzg.toutiao.dao.LoginTicketMapper;
import com.yzg.toutiao.dao.UserMapper;
import com.yzg.toutiao.model.*;
import com.yzg.toutiao.model.example.LoginTicketExample;
import com.yzg.toutiao.model.example.UserExample;
import com.yzg.toutiao.utils.RedisKeyUtils;
import com.yzg.toutiao.utils.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author yzg
 * @create 2019/6/27
 */
@Service
@Transactional
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    UserMapper userMapper;
    @Autowired
    LoginTicketMapper loginTicketMapper;
    @Autowired
    FollowService followService;
    @Autowired
    JedisAdapter jedisAdapter;
    @Autowired
    EventProducer eventProducer;

    /**
     * 根据id'查询用户详细信息
     *
     * @param id
     * @return
     */
    public User getUserById(int id) {
        return userMapper.selectByPrimaryKey(id);
    }


    /**
     * 用户注册
     *
     * @return
     */
    public void register(User user) {
        String username = user.getName();
        String password = user.getPassword();
        String email = user.getEmail();
        //设置未激活状态
        user.setState((byte) 1);

        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",
                new Random().nextInt(1000)));
        //加密密码
        user.setPassword(WendaUtil.MD5(password + user.getSalt()));
        userMapper.insert(user);

    }

    /**
     * 生成ticket到数据库并返回
     *
     * @param userId
     * @return
     */
    public String addLoginTicket(int userId) {
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date now = new Date();
        now.setTime(3600 * 24 * 14 * 1000 + now.getTime());
        ticket.setExpired(now);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicketMapper.insertSelective(ticket);
        return ticket.getTicket();
    }


    /**
     * 根据用户名查询用户
     *
     * @param username
     * @return
     */
    public User getUserByName(String username) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNameEqualTo(username).andStateEqualTo((byte) 1);
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() > 0) {
            return users.get(0);
        } else {
            return null;
        }
    }

    /**
     * 根据多个id查询多个用户同时排序
     *
     * @param ids
     * @return
     */
    public List<User> getUsersByIds(List<Integer> ids) {
        List<User> users = new ArrayList<>();
        UserExample userExample = new UserExample();
        if (ids.size() == 0) {
            return users;
        }
        //使用in查询的同时排序
        StringBuilder orderBy = new StringBuilder(" FIELD(id,");
        for (int id : ids) {
            orderBy.append(String.valueOf(id)).append(",");
        }
        orderBy.deleteCharAt(orderBy.length() - 1);
        orderBy.append(")");
        userExample.setOrderByClause(String.valueOf(orderBy));
        userExample.createCriteria().andIdIn(ids).andStateEqualTo((byte) 1);
        return userMapper.selectByExample(userExample);
    }


    /**
     * 用户登录
     *
     * @param user
     * @param password
     * @param rememberMe
     * @return
     */
    public Result login(User user, String password, boolean rememberMe) {
        if (!user.getPassword().equals(WendaUtil.MD5(password + user.getSalt()))) {
            return new Result().code(2).message("密码错误");
        }
        String ticket = addLoginTicket(user.getId());
        return new Result().success().data(ticket);
    }

    public Result logout(String ticket, HttpSession session) {
        session.removeAttribute("user");
        LoginTicketExample loginTicketExample = new LoginTicketExample();
        loginTicketExample.createCriteria().andTicketEqualTo(ticket);
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setStatus(1);
        loginTicketMapper.updateByExampleSelective(loginTicket, loginTicketExample);
        return new Result().success();
    }

    /**
     * 根据邮箱查询用户
     *
     * @param email
     * @return
     */
    public User getUserByEmail(String email) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andEmailEqualTo(email);
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() > 0) {
            return users.get(0);
        } else {
            return null;
        }
    }

    /**
     * 根据用户id获取用户公共信息(UserInfo)
     *
     * @return
     */
    public UserInfo getUserInfoById(int id) {
        UserInfo userInfo = userMapper.getUserInfoById(id);
        //查询关注他的人数
        userInfo.setFollowers((int) followService.getFollowerCounts(EntityType.USER, userInfo.getId()));
        //查询他关注的人数
        userInfo.setFollowees((int) followService.getFolloweeCounts(userInfo.getId(),EntityType.USER));

        return userInfo;
    }


    /**
     * 根据用户id批量获取用户公共信息(UserInfo)
     *
     * @param ids
     * @return
     */
    public List<UserInfo> getUserInfoByIds(List<Integer> ids) {

        List<UserInfo> users = new ArrayList<>();
        if (ids.size() == 0) {
            return users;
        }
        List<User> usersByIds = getUsersByIds(ids);
        for (User user : usersByIds) {
            UserInfo userInfo = getUserInfoById(user.getId());
            users.add(userInfo);
        }
        return users;
    }
}
