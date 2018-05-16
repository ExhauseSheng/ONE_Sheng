package com.sheng.one_sheng.ui;

import android.widget.ListView;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/16.
 */

/**
 * 重写ListView，设置为不滚动，用于评价列表
 */
public class MyListView extends ListView {
    public MyListView(android.content.Context context,android.util.AttributeSet attrs){
        super(context, attrs);
    }
    /**
     * 设置不滚动
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}
