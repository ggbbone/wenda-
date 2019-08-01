package com.yzg.toutiao.controller;

import com.yzg.toutiao.model.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    HostHolder hostHolder;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String toMessages(Model model){

        if (hostHolder.getUser() == null){
            return "redirect:/login";
        }
        model.addAttribute("location","messages");
        return "message";
    }
}
