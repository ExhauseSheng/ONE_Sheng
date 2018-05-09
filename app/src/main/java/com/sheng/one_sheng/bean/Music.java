package com.sheng.one_sheng.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/8.
 */

public class Music {

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
    public List<Author> authorList;   //艺人信息的集合
    @SerializedName("story_author")
    public List<Author> storyAuthorList;     //文章作者信息的集合

    @SerializedName("id")
    public int songId;          //歌曲id
    @SerializedName("title")
    public String songTitle;    //歌曲名称

    public String cover;        //专辑封面

    @SerializedName("story_title")
    public String storyTitle;   //文章名称
    @SerializedName("story")
    public String story;        //文章主体
    @SerializedName("story_summary")
    public String summary;      //引言

    public String lyric;        //歌词纯文本

    public String album;        //专辑名

    public String info;         //歌曲信息
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
