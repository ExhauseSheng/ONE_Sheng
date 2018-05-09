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
    public int itemId;               //文章详细id号,获取单个文章内容时使用
    public String imageUrl;          //图片Url
    public int likeCount;            //收藏数量
    public String updateDate;         //时间
    public List<Author> authorList;   //作者信息的集合
    public String subTitle;           //电影名称
    public int count;                 //数组size
    public List<data> dataList;       //数据集合
}
