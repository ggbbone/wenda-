package com.yzg.toutiao.model;

import java.util.Date;

public class ReplyUser {
    private Integer id;

    private Integer userId;

    private Date createdDate;

    private Integer commentId;

    private String content;

    private Integer likes;

    private Integer airId;

    private Byte state;

    private String userName;

    private String userHeadUrl;

    private String airName;

    private String airHeadUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getAirId() {
        return airId;
    }

    public void setAirId(Integer airId) {
        this.airId = airId;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUserHeadUrl() {
        return userHeadUrl;
    }

    public void setUserHeadUrl(String userHeadUrl) {
        this.userHeadUrl = userHeadUrl == null ? null : userHeadUrl.trim();
    }

    public String getAirName() {
        return airName;
    }

    public void setAirName(String airName) {
        this.airName = airName == null ? null : airName.trim();
    }

    public String getAirHeadUrl() {
        return airHeadUrl;
    }

    public void setAirHeadUrl(String airHeadUrl) {
        this.airHeadUrl = airHeadUrl == null ? null : airHeadUrl.trim();
    }
}