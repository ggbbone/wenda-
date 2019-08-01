package com.yzg.toutiao.controller;

import com.yzg.toutiao.annotation.LoginRequired;
import com.yzg.toutiao.async.EventModel;
import com.yzg.toutiao.async.EventProducer;
import com.yzg.toutiao.async.EventType;
import com.yzg.toutiao.model.EntityType;
import com.yzg.toutiao.model.HostHolder;
import com.yzg.toutiao.model.Result;
import com.yzg.toutiao.service.LikeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author yzg
 * @create 2019/7/21
 */
@Controller
public class LikeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LikeController.class);
    @Autowired
    LikeService likeService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    EventProducer eventProducer;


    /**
     * 点赞
     * @param entityId
     * @param entityType
     * @return
     */
    @LoginRequired
    @RequestMapping(value = "/like/{entityType}/{entityId}",method = RequestMethod.POST)
    @ResponseBody
    public Result like(@PathVariable(value = "entityId") int entityId,
                       @PathVariable(value = "entityType") String entityType){
        int type = EntityType.getTypeInt(entityType);

        if (type != -1){
            long likes = likeService.like(hostHolder.getUser().getId(), type, entityId);
            //点赞成功，添加事件
            LOGGER.info("点赞成功，添加事件");
            eventProducer.fireEvent(new EventModel(EventType.LIKE)
                    .setActorId(hostHolder.getUser().getId())
                    .setEntityId(entityId)
                    .setEntityType(type)
                    .setExt("fromName",hostHolder.getUser().getName()));

            return new Result().success().data(likes);
        }else {
          return new Result().fail();
        }
    }

    /**
     * 点餐
     * @param entityId
     * @param entityType
     * @return
     */
    @LoginRequired
    @RequestMapping(value = "/dislike/{entityType}/{entityId}",method = RequestMethod.POST)
    @ResponseBody
    public Result disLike(@PathVariable(value = "entityId") int entityId,
                          @PathVariable(value = "entityType") String entityType){
        int type = EntityType.getTypeInt(entityType);
        if (type != -1){
            long likes = likeService.disLike(hostHolder.getUser().getId(), type, entityId);
            return new Result().success().data(likes);
        }else {
            return new Result().fail();
        }
    }

    @LoginRequired
    @RequestMapping(value = "/delLike/{entityType}/{entityId}",method = RequestMethod.POST)
    @ResponseBody
    public Result delLike(@PathVariable(value = "entityId") int entityId,
                          @PathVariable(value = "entityType") String entityType){
        int type = EntityType.getTypeInt(entityType);
        if (type != -1){
            long likes = likeService.delLike(hostHolder.getUser().getId(), type, entityId);
            return new Result().success().data(likes);
        }else {
            return new Result().fail();
        }
    }

}
