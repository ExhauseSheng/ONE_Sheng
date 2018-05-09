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
    public int itemId;                  //文章详细id号,获取单个文章内容时使用
    public String imageUrl;             //图片Url
    public int likeCount;               //收藏数量
    public String updateDate;           //时间
    public List<Author> authorList;     //作者信息的集合
    public int contentId;               //同itemId
    public String essayTitle;           //详细文章标题
    public String essayAuthor;          //详细文章作者
    public String wbImgUrl;             //详细文章图片
    public String guideWord;            //详细文章引言
    public String content;              //文章主体
    public int nextId;                  //下一篇文章的id
    public int preId;                   //上一篇文章的id
    public int praiseNum;               //点赞数量
    public int shareNum;                //分享数量
    public int commentNum;              //评论数量
}
