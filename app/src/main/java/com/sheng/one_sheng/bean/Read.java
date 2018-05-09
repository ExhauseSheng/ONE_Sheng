package com.sheng.one_sheng.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/8.
 */

public class Read  {

    public String title;                //文章标题
    public String forward;              //文章引言
    public int id;                      //文章id号

    @SerializedName("item_id")
    public int itemId;                  //文章详细id号,获取单个文章内容时使用
    @SerializedName("img_url")
    public String imageUrl;             //图片Url
    @SerializedName("like_count")
    public int likeCount;               //收藏数量
    @SerializedName("last_update_date")
    public String updateDate;           //时间
    @SerializedName("author")
    public List<Author> authorList;     //作者信息的集合

    @SerializedName("content_id")
    public int contentId;               //同itemId
    @SerializedName("hp_title")
    public String essayTitle;           //详细文章标题
    @SerializedName("hp_author")
    public String essayAuthor;          //详细文章作者
    @SerializedName("wb_img_url")
    public String wbImgUrl;             //详细文章图片
    @SerializedName("guide word")
    public String guideWord;            //详细文章引言
    @SerializedName("hp_content")
    public String content;              //文章主体
    @SerializedName("next_id")
    public int nextId;                  //下一篇文章的id
    @SerializedName("previous_id")
    public int preId;                   //上一篇文章的id
    @SerializedName("praisenum")
    public int praiseNum;               //点赞数量
    @SerializedName("sharenum")
    public int shareNum;                //分享数量
    @SerializedName("commentnum")
    public int commentNum;              //评论数量
}
