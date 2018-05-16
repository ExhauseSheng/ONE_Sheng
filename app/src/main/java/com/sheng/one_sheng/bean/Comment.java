package com.sheng.one_sheng.bean;

import android.content.ContentValues;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/16.
 */

public class Comment {
    private int count;          //评论数量
    private String quote;       //转发别人评论的内容
    private String content;     //自己评论的内容
    private int praiseNum;      //评论点赞数量
    private String createTime;  //评论创建时间
    private String userName;    //评论用户名
    private String touserName;  //转发用户名

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTouserName() {
        return touserName;
    }

    public void setTouserName(String touserName) {
        this.touserName = touserName;
    }
}
