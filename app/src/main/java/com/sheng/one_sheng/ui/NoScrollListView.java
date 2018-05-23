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
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}
