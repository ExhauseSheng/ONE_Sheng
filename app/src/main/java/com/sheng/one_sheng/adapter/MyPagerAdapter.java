package com.sheng.one_sheng.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/7.
 */

/**
 * 轮播图适配器
 */
public class MyPagerAdapter extends PagerAdapter {

    public static final int MAX_SCROLL_VALUE = 10000;   //展示10000张图片，不实际，让用户有一种以为是无限轮播的假象！

    private List<ImageView> mItems;
    private Context mContext;
    private LayoutInflater mInflater;

    public MyPagerAdapter(List<ImageView> items, Context context){
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mItems = items;
    }

    /**
     * @param container
     * @param position
     * @return 对position进行求模操作
     * 因为当用户向左滑时position有可能出现负值，所以必须进行处理
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View ret = null;

        //对ViewPager页号求模取出View列表中要显示的项
        position %= mItems.size();
        Log.d("Adapter", "instantiateItem: position: " + position);
        ret = mItems.get(position);
        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException异常
        ViewParent viewParent = ret.getParent();     //取出view的父布局
        if (viewParent != null){
            ViewGroup parent = (ViewGroup) viewParent;
            parent.removeView(ret);
        }
        container.addView(ret);

        return ret;
    }

    /**
     * 由于我们在上面一个方法中已经处理了remove的逻辑
     * 因此这里并不需要处理。实际上，实验表明这里如果加上了remove的调用
     * 则会出现ViewPager的内容为空的情况
     *
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    @Override
    public int getCount() {
        int ret = 0;
        if (mItems.size() > 0){
            ret = MAX_SCROLL_VALUE;     //最大滚动值
        }
        return ret;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (View) object;
    }
}
