package com.yzg.toutiao.controller;

import com.yzg.toutiao.aspect.LogAspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author yzg
 * @create 2019/6/26
 */
@Controller
public class HomeController {
    private static final Logger LOGGER=LoggerFactory.getLogger(LogAspect.class);

    @RequestMapping(value = {"/", "index"})
    public String index() {
        return "index";
    }


}
