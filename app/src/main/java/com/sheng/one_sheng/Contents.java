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
    public static final String MOVIE_MORE_URL = "http://v3.wufazhuce.com:8000/api/channel/movie/more/13722?platform=android";
    //Music更多内容的url地址
    public static final String MUSIC_MORE_URL = "http://v3.wufazhuce.com:8000/api/channel/music/more/12958?platform=android";
    //Read更多内容的url地址
    public static final String READ_MORE_URL = "http://v3.wufazhuce.com:8000/api/channel/reading/more/12644?channel=wdj&version=4.0.2&platform=android";
    //PaperId更多内容的url地址
    public static final String PAPER_MORE_URL = "http://v3.wufazhuce.com:8000/api/hp/idlist/2078?version=3.5.0&platform=android";

    //PaperActivity
    public static final int PAPER_ID = 1;
    public static final int PAPER_LIST = 2;
    public static final int PAPER_IMAGE = 3;
    public static final int FINISH_DELAY = 4;   //判断是否要退出程序

    //PaperDetail
    public static final String PAPER_DETAIL_PRE = "http://v3.wufazhuce.com:8000/api/hp/detail/";
    public static final String PAPER_DETAIL_NEXT = "?version=3.5.0&platform=android";

    //ReadDetail
    public static final String READ_DETAIL_PRE = "http://v3.wufazhuce.com:8000/api/essay/";
    public static final String READ_DETAIL_NEXT = "?platform=android";
    //MusicDtail
    public static final String MUSIC_DETAIL_PRE = "http://v3.wufazhuce.com:8000/api/music/detail/";
    public static final String MUSIC_DETAIL_NEXT = "?version=3.5.0&platform=android";
    //MovieDetail
    public static final String MOVIE_DETAIL_PRE = "http://v3.wufazhuce.com:8000/api/movie/";
    public static final String MOVIE_DETAIL_NEXT = "/story/1/0?platform=android";


    //ReadActivity
    public static final int READ_LIST = 0;
    public static final int READ_MORE = 1;

    //MusicActivity
    public static final int MUSIC_LIST = 0;
    public static final int MUSIC_MORE = 1;

    //MovieActivity
    public static final int MOVIE_LIST = 0;
    public static final int MOVIE_MORE = 1;

    //Comment
    public static final String READ_COMMENT_PRE = "http://v3.wufazhuce.com:8000/api/comment/praiseandtime/essay/";
    public static final String MUSIC_COMMENT_PRE = "http://v3.wufazhuce.com:8000/api/comment/praiseandtime/music/";
    public static final String MOVIE_COMMENT_PRE = "http://v3.wufazhuce.com:8000/api/comment/praiseandtime/movie/";
    public static final String COMMENT_NEXT = "/0?&platform=android";

    //RefreshListView
    public static final int DOWN_PULL_REFRESH = 0;    // 下拉刷新状态
    public static final int RELEASE_REFRESH = 1;      // 松开刷新
    public static final int REFRESHING = 2;           // 正在刷新中

    //轮播图睡眠时间
    public static final int VIEW_PAGER_DELAY = 3500;       //睡眠3.5s

    //开场动画缩放时间
    public static final int VIEW_START_DELAY = 4000;        //缩放4s

    //列表下拉刷新加载时间
    public static final int LIST_REFRESH_TIME = 2000;       //加载2s

    //列表上拉更多加载时间
    public static final int LIST_MORE_TIME = 3000;          //加载3s

}
