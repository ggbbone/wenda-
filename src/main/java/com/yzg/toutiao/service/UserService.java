package com.yzg.toutiao.service;

import com.yzg.toutiao.aspect.LogAspect;
import com.yzg.toutiao.dao.LoginTicketMapper;
import com.yzg.toutiao.dao.UserMapper;
import com.yzg.toutiao.model.LoginTicket;
import com.yzg.toutiao.model.Result;
import com.yzg.toutiao.model.User;
import com.yzg.toutiao.model.example.LoginTicketExample;
import com.yzg.toutiao.model.example.UserExample;
import com.yzg.toutiao.utils.WendaUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @author yzg
 * @create 2019/6/27
 */
@Service
@Transactional
public class UserService {
    private static final Logger LOGGER=LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    UserMapper userMapper;
    @Autowired
    LoginTicketMapper loginTicketMapper;

    public User getUserById(int id) {
        return userMapper.selectByPrimaryKey(id);
    }

    /**
     * 用户注册
     * @param username
     * @param password
     * @return
     */
    public Result register(String username, String password) {
        //数据校验
        if (StringUtils.isBlank(username)) {
            return new Result().code(1).message("用户名不能为空");
        }
        if (StringUtils.isBlank(password)) {
            return new Result().code(2).message("密码不能为空");
        }
        //用户名是否存在
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNameEqualTo(username);
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() > 0) {
            return new Result().code(3).message("用户名已存在");
        }
        //注册用户
        User user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",
                new Random().nextInt(1000)));
        //加密密码
        user.setPassword(WendaUtil.MD5(password+user.getSalt()));
        userMapper.insert(user);

        return new Result().success();
    }

    public String addLoginTicket(int userId){
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date now = new Date();
        now.setTime(3600*24*14*1000 + now.getTime());
        ticket.setExpired(now);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketMapper.insertSelective(ticket);
        return ticket.getTicket();
    }


    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    public User getUserByName(String username){
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNameEqualTo(username);
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size()>0){
            return users.get(0);
        }else {
            return null;
        }
    }



    /**
     * 用户登录
     * @param user
     * @param password
     * @param rememberMe
     * @return
     */
    public Result login(User user, String password, boolean rememberMe) {
        if (!user.getPassword().equals(WendaUtil.MD5(password + user.getSalt()))){
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
        loginTicketMapper.updateByExampleSelective(loginTicket,loginTicketExample);
        return new Result().success();
    }
}
