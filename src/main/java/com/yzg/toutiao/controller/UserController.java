package com.yzg.toutiao.controller;

import com.yzg.toutiao.annotation.LoginRequired;
import com.yzg.toutiao.aspect.LogAspect;
import com.yzg.toutiao.model.HostHolder;
import com.yzg.toutiao.model.Result;
import com.yzg.toutiao.model.User;
import com.yzg.toutiao.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yzg
 * @create 2019/6/27
 * 登陆注册相关
 */
@Controller
@RequestMapping("users")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;

    @LoginRequired
    @RequestMapping(value = "/{userId}",method = RequestMethod.GET)
    public String userInfo(@PathVariable(value = "userId") int userId){

        return "userinfo";
    }

    @LoginRequired
    @RequestMapping(value = "/id",method = RequestMethod.GET)
    @ResponseBody
    public Result getUserName(){
        Map<String, String> userInfo = new HashMap<>();
        if (hostHolder.getUser() != null) {
            userInfo.put("id", hostHolder.getUser().getId().toString());
            userInfo.put("name", hostHolder.getUser().getName());
            userInfo.put("headUrl", hostHolder.getUser().getHeadUrl());
        }
        return new Result().success().data(userInfo);
    }
}
