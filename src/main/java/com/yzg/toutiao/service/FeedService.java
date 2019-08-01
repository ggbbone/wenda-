package com.yzg.toutiao.service;

import com.github.pagehelper.PageHelper;
import com.yzg.toutiao.dao.FeedMapper;
import com.yzg.toutiao.model.EntityType;
import com.yzg.toutiao.model.Feed;
import com.yzg.toutiao.model.example.FeedExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yzg
 * @create 2019/7/29
 */
@Service
@Transactional
public class FeedService {
    @Autowired
    FollowService followService;
    @Autowired
    FeedMapper feedMapper;

    /**
     * 获取关注的内容
     *
     * @param offset
     * @param limit
     * @return
     */
    public List<Feed> getFeeds(int userId, int offset, int limit) {
        List<Feed> feeds = new ArrayList<>();
        //获取用户关注的人
        long followeeCounts = followService.getFolloweeCounts(userId, EntityType.USER);
        List<Integer> ids = followService.getFollowees(EntityType.USER, userId, 0, (int) followeeCounts);

        //查询他关注的人产生的feed
        if (ids.size() > 0) {
            FeedExample feedExample = new FeedExample();
            feedExample.setOrderByClause("created_date desc");
            feedExample.createCriteria().andUserIdIn(ids).andStateEqualTo((byte) 1);
            PageHelper.offsetPage(offset, limit);
            feeds = feedMapper.selectByExample(feedExample);
        }

        return feeds;
    }

    /**
     * 插入一条feed
     *
     * @param feed
     */
    public void insertFeed(Feed feed) {
        feed.setCreatedDate(new Date());
        feed.setState((byte) 1);
        feedMapper.insertSelective(feed);
    }
}
