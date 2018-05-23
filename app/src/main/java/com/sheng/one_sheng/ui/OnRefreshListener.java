package com.sheng.one_sheng.ui;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/23.
 */

/**
 * ListView滑动监听回调接口
 */
public interface OnRefreshListener {

    /**
     * 下拉刷新
     */
    void onDownPullRefresh();

    /**
     * 上拉加载更多
     */
    void onLoadingMore();
}
