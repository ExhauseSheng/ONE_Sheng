package com.sheng.one_sheng.bean;


import java.util.List;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/8.
 */

public class Read  extends Base {

    private String contentId;               //同itemId
    private String essayTitle;           //详细文章标题
    private String wbImgUrl;             //详细文章图片
    private String guideWord;            //详细文章引言
    private String content;              //文章主体
    private String nextId;                  //下一篇文章的id
    private String preId;                   //上一篇文章的id
    private int praiseNum;               //点赞数量
    private int shareNum;                //分享数量
    private int commentNum;              //评论数量

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
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

    public String getNextId() {
        return nextId;
    }

    public void setNextId(String nextId) {
        this.nextId = nextId;
    }

    public String getPreId() {
        return preId;
    }

    public void setPreId(String preId) {
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
