package com.sheng.one_sheng.bean;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/8.
 */

public class Paper {

    private int[] data;           //含有十个插画id的字符串数组
    private int id;                  //插画id
    private String title;               //插画标题
    private String imageUrl;         //插画url地址
    private String imgUrlOriginal;   //插画原图地址
    private String content;       //插画文章内容
    private String updateDate;       //日期
    private String authorInfo;       //作者信息
    private String imgAuthor;        //图片作者
    private String textAuthor;       //文字作者
    private int praiseNum;           //点赞数量
    private int shareNum;            //分享数量
    private int commentNum;          //评论数量

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[] getData() {
        return data;
    }

    public void setData(int[] data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImgUrlOriginal() {
        return imgUrlOriginal;
    }

    public void setImgUrlOriginal(String imgUrlOriginal) {
        this.imgUrlOriginal = imgUrlOriginal;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getAuthorInfo() {
        return authorInfo;
    }

    public void setAuthorInfo(String authorInfo) {
        this.authorInfo = authorInfo;
    }

    public String getImgAuthor() {
        return imgAuthor;
    }

    public void setImgAuthor(String imgAuthor) {
        this.imgAuthor = imgAuthor;
    }

    public String getTextAuthor() {
        return textAuthor;
    }

    public void setTextAuthor(String textAuthor) {
        this.textAuthor = textAuthor;
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
