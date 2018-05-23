package com.sheng.one_sheng.activity;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.sheng.one_sheng.MyApplication;
import com.sheng.one_sheng.R;
import com.sheng.one_sheng.adapter.CommentListAdapter;
import com.sheng.one_sheng.bean.Comment;
import com.sheng.one_sheng.ui.RefreshListView;
import com.sheng.one_sheng.util.HttpCallbackListener;
import com.sheng.one_sheng.util.HttpUtil;
import com.sheng.one_sheng.util.Utilty;

import java.util.List;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/9.
 */

/**
 * 作为所有活动的父类，多个活动的共用方法都在这里
 */
public abstract class BaseActivity extends AppCompatActivity
{
    protected Toolbar mToolbar;    //定制toolbar

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

    /**
     * 更改系统状态栏的颜色
     */
    protected void changeStatusBar(){
        //更改系统栏的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //因为不是所有的系统都可以设置颜色的，在4.4以下就不可以。。有的说4.1，所以在设置的时候要检查一下系统版本是否是4.1以上
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
    }

    /**
     * 设置toolbar，代替AvtionBar，并添加返回按钮
     */
    protected void setToolbar(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");   //将原本的标题栏清空，而用一个新的TextView代替
        setSupportActionBar(mToolbar);
    }

    /**
     * 根据itemId发送网络请求获取装有评论列表数据的对象
     */
    protected void requestCommentList(final String url){
        HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                final String responseText = response;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<Comment> commentList = Utilty.handleCommentResponse(responseText);
                        setCommentAdapter(commentList);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyApplication.getContext(), "获取评论列表失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 评论列表适配器的设置
     * @param commentList
     */
    protected void setCommentAdapter(List<Comment> commentList){
        CommentListAdapter adapter = new CommentListAdapter(MyApplication.getContext(),
                R.layout.layout_item_comment, commentList);
        RefreshListView listView = (RefreshListView) findViewById(R.id.lv_comment_list_view);
        listView.setAdapter(adapter);
    }
}

