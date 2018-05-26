package com.sheng.one_sheng.ui;

import android.widget.ListView;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/23.
 */

/**
 * 重写ListView，将其设置为不滚动，防止其放在ScrollLayout中会出问题
 */
public class NoScrollListView extends ListView {

    public NoScrollListView(android.content.Context context,android.util.AttributeSet attrs){
        super(context, attrs);
    }

    /**
     * 设置不滚动
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
//        一个MeasureSpec封装了父布局传递给子布局的布局要求，每个MeasureSpec代表了一组宽度和高度的要求
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);   //AT_MOST —— 至多达到指定大小的值
        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}
