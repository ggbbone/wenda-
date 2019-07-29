package com.yzg.toutiao.controller;

import com.yzg.toutiao.annotation.LoginRequired;
import com.yzg.toutiao.async.EventModel;
import com.yzg.toutiao.async.EventProducer;
import com.yzg.toutiao.async.EventType;
import com.yzg.toutiao.model.*;
import com.yzg.toutiao.service.FollowService;
import com.yzg.toutiao.service.JedisAdapter;
import com.yzg.toutiao.service.UserService;
import com.yzg.toutiao.utils.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yzg
 * @create 2019/7/23
 * <p>
 * 关注/收藏/感兴趣等
 */
@Controller
public class FollowController {
    @Autowired
    FollowService followService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    EventProducer eventProducer;
    @Autowired
    UserService userService;


    /**
     * 关注
     *
     * @param entityType
     * @param entityId
     * @return 是否关注
     */
    @LoginRequired
    @ResponseBody
    @RequestMapping(value = "/follow/{entityType}/{entityId}", method = RequestMethod.POST)
    public Result follow(@PathVariable(value = "entityType") String entityType,
                         @PathVariable(value = "entityId") int entityId) {
        User user = hostHolder.getUser();
        int typeInt = EntityType.getTypeInt(entityType);
        if (typeInt > 0) {
            followService.follow(user.getId(), typeInt, entityId);
            //提交关注事件
            eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                    .setActorId(user.getId())
                    .setEntityType(typeInt)
                    .setEntityId(entityId));
            boolean follower = followService.isFollower(hostHolder.getUser().getId(), typeInt, entityId);
            return new Result().success().data(follower);
        }
        return new Result().fail();
    }

    /**
     * 取消关注
     *
     * @param entityType
     * @param entityId
     * @return
     */
    @LoginRequired
    @ResponseBody
    @RequestMapping(value = "/unfollow/{entityType}/{entityId}", method = RequestMethod.POST)
    public Result unfollow(@PathVariable(value = "entityType") String entityType,
                           @PathVariable(value = "entityId") int entityId) {
        int typeInt = EntityType.getTypeInt(entityType);
        if (typeInt > 0) {
            followService.unfollow(hostHolder.getUser().getId(), typeInt, entityId);
            boolean follower = followService.isFollower(hostHolder.getUser().getId(), typeInt, entityId);
            return new Result().success().data(follower);
        }
        return new Result().fail();
    }

    /**
     * 查询他关注的列表
     *
     * @param entityType
     * @param offset
     * @param limit
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/followees/{entityType}/{entityId}", method = RequestMethod.GET)
    public Result getFollwees(@PathVariable(value = "entityType") String entityType,
                              @PathVariable(value = "entityId") int entityId,
                              @RequestParam int offset,
                              @RequestParam int limit) {
        User loginUser = hostHolder.getUser();
        int typeInt = EntityType.getTypeInt(entityType);
        if (typeInt == EntityType.USER) {
            //从redis中取出他关注的用户列表
            List<Integer> followees = followService
                    .getFollowees(typeInt, entityId, offset - 1, limit + offset - 2);
            //获取列表用户信息
            List<UserInfo> users = userService.getUserInfoByIds(followees);
            //查询当前用户对这些用户的关注状态
            for (UserInfo userInfo : users) {
                userInfo.setFollow(followService.isFollower(loginUser.getId(), EntityType.USER, userInfo.getId()));
            }
            return new Result().success().data(users);
        }
        return new Result().fail();
    }


    /**
     * 查询关注他的列表
     *
     * @param entityType
     * @param entityId
     * @param offset
     * @param limit
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/followers/{entityType}/{entityId}", method = RequestMethod.GET)
    public Result getFollowers(@PathVariable(value = "entityType") String entityType,
                               @PathVariable(value = "entityId") int entityId,
                               @RequestParam int offset,
                               @RequestParam int limit) {

        int typeInt = EntityType.getTypeInt(entityType);
        if (typeInt > 0) {
            //从redis取出关注他的用户id
            List<Integer> ids = followService.getFollowers(typeInt, entityId, offset - 1, offset + limit);
            List<UserInfo> users = userService.getUserInfoByIds(ids);
            //设置我对他的关注状态
            for (UserInfo userInfo : users) {
                boolean follow = followService.isFollower(hostHolder.getUser().getId(), EntityType.USER, userInfo.getId());
                userInfo.setFollow(follow);
            }
            return new Result().success().data(users);
        }
        return new Result().fail();
    }
}
