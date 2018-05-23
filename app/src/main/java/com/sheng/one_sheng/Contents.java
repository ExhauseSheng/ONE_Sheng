package com.sheng.one_sheng;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/22.
 */

/**
 * 常量类 -- 放置所有常量
 */
public class Contents {

    //Movie最新的列表的url地址
    public static final String MOVIE_LIST_URL = "http://v3.wufazhuce.com:8000/api/channel/movie/more/0?platform=android";
    //Music最新的列表的url地址
    public static final String MUSIC_LIST_URL = "http://v3.wufazhuce.com:8000/api/channel/music/more/0?platform=android";
    //Read最新的列表的url地址
    public static final String READ_LIST_URL = "http://v3.wufazhuce.com:8000/api/channel/reading/more/0?channel=wdj&version=4.0.2&platform=android";
    //PaperId最新的列表的url地址
    public static final String PAPER_LIST_URL = "http://v3.wufazhuce.com:8000/api/hp/idlist/0?version=3.5.0&platform=android";


    //Movie更多内容的url地址
    public static final String MOVIE_MORE_URL = "http://v3.wufazhuce.com:8000/api/channel/movie/more/13758?platform=android";
    //Music更多内容的url地址
    public static final String MUSIC_MORE_URL = "http://v3.wufazhuce.com:8000/api/channel/music/more/13046?platform=android";
    //Read更多内容的url地址
    public static final String READ_MORE_URL = "http://v3.wufazhuce.com:8000/api/channel/reading/more/12808?channel=wdj&version=4.0.2&platform=android";
    //PaperId更多内容的url地址
    public static final String PAPER_MORE_URL = "http://v3.wufazhuce.com:8000/api/hp/idlist/2065?version=3.5.0&platform=android";

    //插画的三个属性：插画id，插画列表，插画图片
    public static final int PAPER_ID = 1;
    public static final int PAPER_LIST = 2;
    public static final int PAPER_IMAGE = 3;

    //轮播图睡眠时间
    public static final int VIEW_PAGER_DELAY = 3500;       //睡眠3.5s

    //开场动画缩放时间
    public static final int VIEW_START_DELAY = 4000;        //缩放4s

    //判断是否要退出程序
    public static final int FINISH_DELAY = 4;
}
