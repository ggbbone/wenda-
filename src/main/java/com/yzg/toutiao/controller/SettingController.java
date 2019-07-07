package com.yzg.toutiao.controller;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author yzg
 * @create 2019/6/26
 */
@Controller
public class SettingController {

    @ResponseBody
    @RequestMapping(value = "setting")
    public String setting(){

        return "hello";
    }
}
