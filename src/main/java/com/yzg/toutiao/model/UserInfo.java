package com.yzg.toutiao.model;

/**
 * @author yzg
 * @create 2019/7/23
 *
 * 用于显示给所有用户的信息
 */
public class UserInfo {
    private Integer id;

    private String name;

    private String headline;

    private String headUrl;

    private boolean follow;

    /**
     * 关注他的人数
     */
    private int followers=0;

    /**
     * 他关注的人数
     */
    private int followees=0;

    /**
     * 他发布的问题数量
     */
    private int questions=0;

    /**
     * 他写的回答数量
     */
    private int answers=0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowees() {
        return followees;
    }

    public void setFollowees(int followees) {
        this.followees = followees;
    }

    public int getQuestions() {
        return questions;
    }

    public void setQuestions(int questions) {
        this.questions = questions;
    }

    public int getAnswers() {
        return answers;
    }

    public void setAnswers(int answers) {
        this.answers = answers;
    }
}
