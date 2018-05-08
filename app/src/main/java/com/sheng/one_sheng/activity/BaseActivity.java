package com.sheng.one_sheng.activity;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

import com.sheng.one_sheng.R;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/9.
 */

public class BaseActivity extends AppCompatActivity     //作为所有活动的父类
{
    protected Toolbar toolbar;    //定制toolbar

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    //更改系统栏的属性
    protected void changeStatusBar(){
        //更改系统栏的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //因为不是所有的系统都可以设置颜色的，在4.4以下就不可以。。有的说4.1，所以在设置的时候要检查一下系统版本是否是4.1以上
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
    }

    //设置toolbar，代替AvtionBar
    protected void setToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");   //将原本的标题栏清空，而用一个新的TextView代替
        setSupportActionBar(toolbar);
    }
}
