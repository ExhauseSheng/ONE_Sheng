package com.sheng.one_sheng.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/8.
 */

public class Movie {

    public int id;                  //文章id号
    public String title;            //文章标题
    public String forward;          //文章引言

    @SerializedName("item_id")
    public int itemId;               //文章详细id号,获取单个文章内容时使用
    @SerializedName("img_url")
    public String imageUrl;          //图片Url
    @SerializedName("like_count")
    public int likeCount;            //收藏数量
    @SerializedName("last_update_date")
    public String updateDate;         //时间
    @SerializedName("author")
    public List<Author> authorList;   //作者信息的集合
    @SerializedName("subtitle")
    public String subTitle;           //电影名称

    public int count;                 //数组size
    public List<data> dataList;       //数据集合
}
