package com.yzg.toutiao.controller;

import com.yzg.toutiao.model.Feed;
import com.yzg.toutiao.model.HostHolder;
import com.yzg.toutiao.model.Result;
import com.yzg.toutiao.model.User;
import com.yzg.toutiao.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yzg
 * @create 2019/7/30
 */
@Controller
@RequestMapping(value = "feed")
public class FeedController {
    @Autowired
    FeedService feedService;
    @Autowired
    HostHolder hostHolder;

    @ResponseBody
    @RequestMapping(value = "/follow", method = RequestMethod.GET)
    public Result getFeeds(@RequestParam(defaultValue = "1",required = false) int offset,
                           @RequestParam(defaultValue = "10",required = false) int limit) {
        User user = hostHolder.getUser();
        List<Feed> feeds = new ArrayList<>();
        if (user != null) {
            feeds = feedService.getFeeds(user.getId(), offset, limit);
        }
        return new Result().success().data(feeds);
    }
}
