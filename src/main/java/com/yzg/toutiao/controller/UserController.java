package com.yzg.toutiao.controller;

import com.yzg.toutiao.annotation.LoginRequired;
import com.yzg.toutiao.model.*;
import com.yzg.toutiao.service.FollowService;
import com.yzg.toutiao.service.JedisAdapter;
import com.yzg.toutiao.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yzg
 * @create 2019/6/27
 * 用户信息
 */
@Controller
@RequestMapping("users")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;
    @Autowired
    FollowService followService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    JedisAdapter jedisAdapter;


    /**
     * 访问用户主页
     * @param userId
     * @param model
     * @return
     */
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public String userInfo(@PathVariable(value = "userId") int userId, Model model) {

        if (hostHolder.getUser() == null) {
            return "redirect:/login";
        }
        UserInfo userInfo = userService.getUserInfoById(userId);
        if (userInfo == null){
            return "redirect:/404";
        }
        userInfo.setFollow(followService.isFollower(hostHolder.getUser().getId(),EntityType.USER,userInfo.getId()));

        model.addAttribute("userInfo", userInfo);
        return "people";
    }

    @LoginRequired
    @RequestMapping(value = "/id", method = RequestMethod.GET)
    @ResponseBody
    public Result getUserName() {
        Map<String, String> userInfo = new HashMap<>();
        if (hostHolder.getUser() != null) {
            userInfo.put("id", hostHolder.getUser().getId().toString());
            userInfo.put("name", hostHolder.getUser().getName());
            userInfo.put("headUrl", hostHolder.getUser().getHeadUrl());
        }
        return new Result().success().data(userInfo);
    }

}
