package com.yzg.toutiao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author yzg
 * @create 2019/7/20
 */
@Controller
@RequestMapping("messages")
public class MessageController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String toMessages(Model model){
        model.addAttribute("location","messages");
        return "message";
    }
}
