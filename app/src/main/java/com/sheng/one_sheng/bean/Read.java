package com.sheng.one_sheng.bean;


import java.util.List;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/8.
 */

public class Read  {

    private String title;                //文章标题
    private String forward;              //文章引言
    private int id;                      //文章id号
    private int itemId;                  //文章详细id号,获取单个文章内容时使用
    private String imageUrl;             //图片Url
    private int likeCount;               //收藏数量
    private String updateDate;           //时间
    private String userName;     //作者名称
    private String des;          //作家简介
    private int fansTotal;       //粉丝数量
    private int contentId;               //同itemId
    private String essayTitle;           //详细文章标题
    private String wbImgUrl;             //详细文章图片
    private String guideWord;            //详细文章引言
    private String content;              //文章主体
    private int nextId;                  //下一篇文章的id
    private int preId;                   //上一篇文章的id
    private int praiseNum;               //点赞数量
    private int shareNum;                //分享数量
    private int commentNum;              //评论数量

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getForward() {
        return forward;
    }

    public void setForward(String forward) {
        this.forward = forward;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public int getFansTotal() {
        return fansTotal;
    }

    public void setFansTotal(int fansTotal) {
        this.fansTotal = fansTotal;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public String getEssayTitle() {
        return essayTitle;
    }

    public void setEssayTitle(String essayTitle) {
        this.essayTitle = essayTitle;
    }

    public String getWbImgUrl() {
        return wbImgUrl;
    }

    public void setWbImgUrl(String wbImgUrl) {
        this.wbImgUrl = wbImgUrl;
    }

    public String getGuideWord() {
        return guideWord;
    }

    public void setGuideWord(String guideWord) {
        this.guideWord = guideWord;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getNextId() {
        return nextId;
    }

    public void setNextId(int nextId) {
        this.nextId = nextId;
    }

    public int getPreId() {
        return preId;
    }

    public void setPreId(int preId) {
        this.preId = preId;
    }

    public int getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }

    public int getShareNum() {
        return shareNum;
    }

    public void setShareNum(int shareNum) {
        this.shareNum = shareNum;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }
}
