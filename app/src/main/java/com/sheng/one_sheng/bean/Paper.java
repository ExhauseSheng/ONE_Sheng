package com.sheng.one_sheng.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/8.
 */

public class Paper {

    public int[] data;           //含有十个插画id的字符串数组
    @SerializedName("hpcontent_id")
    public int id;                  //插画id
    @SerializedName("hp_title")
    public int title;               //插画标题
    @SerializedName("hp_img_url")
    public String imageUrl;         //插画url地址
    @SerializedName("hp_img_original_url")
    public String imgUrlOriginal;   //插画原图地址
    @SerializedName("hp_content")
    public String content;       //插画文章内容
    @SerializedName("last_update_date")
    public String updateDate;       //日期
    @SerializedName("hp_author")
    public String authorInfo;       //作者信息
    @SerializedName("image_authors")
    public String imgAuthor;        //图片作者
    @SerializedName("text_authors")
    public String textAuthor;       //文字作者
    @SerializedName("praisenum")
    public int praiseNum;           //点赞数量
    @SerializedName("sharenum")
    public int shareNum;            //分享数量
    @SerializedName("commentnum")
    public int commentNum;          //评论数量
}
