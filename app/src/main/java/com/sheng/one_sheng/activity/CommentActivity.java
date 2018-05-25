package com.sheng.one_sheng.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.sheng.one_sheng.GlobalContext;
import com.sheng.one_sheng.R;
import com.sheng.one_sheng.adapter.CommentListAdapter;
import com.sheng.one_sheng.adapter.MyPagerAdapter;
import com.sheng.one_sheng.bean.Comment;
import com.sheng.one_sheng.bean.Read;
import com.sheng.one_sheng.ui.NoScrollListView;
import com.sheng.one_sheng.util.HttpCallbackListener;
import com.sheng.one_sheng.util.HttpUtil;
import com.sheng.one_sheng.util.Utilty;

import java.util.List;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/25.
 */

/**
 * 在BaseActivity中追加获取评论列表的方法
 */
public class CommentActivity extends BaseActivity {

    /**
     * 根据itemId发送网络请求获取装有评论列表数据的对象
     */
    protected void requestCommentList(final String url){
        HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                final String responseText = response;
                final List<Comment> commentList = Utilty.handleCommentResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setCommentAdapter(commentList);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GlobalContext.getContext(), "获取评价失败", Toast.LENGTH_SHORT).show();
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
        CommentListAdapter adapter = new CommentListAdapter(GlobalContext.getContext(),
                R.layout.layout_item_comment, commentList);
        NoScrollListView listView = (NoScrollListView)  findViewById(R.id.lv_comment_list_view);
        listView.setAdapter(adapter);
    }
}
