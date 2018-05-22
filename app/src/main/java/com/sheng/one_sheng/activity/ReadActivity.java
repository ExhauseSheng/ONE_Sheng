package com.sheng.one_sheng.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.sheng.one_sheng.MyApplication;
import com.sheng.one_sheng.R;
import com.sheng.one_sheng.adapter.ReadListAdapter;
import com.sheng.one_sheng.bean.Read;
import com.sheng.one_sheng.ui.LoadDialog;
import com.sheng.one_sheng.ui.NoScrollListView;
import com.sheng.one_sheng.util.HttpCallbackListener;
import com.sheng.one_sheng.util.HttpUtil;
import com.sheng.one_sheng.util.SPUtil;
import com.sheng.one_sheng.util.Utilty;


import java.util.List;

import static com.sheng.one_sheng.Contents.READ_LIST_URL;

public class ReadActivity extends BaseActivity {

    private SwipeRefreshLayout mSlRefresh;
    private LoadDialog mDialog;

    /**
     * 用于启动这个活动的方法
     * @param context
     */
    public static void actionStart(Context context){
        Intent intent = new Intent(context, ReadActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        setToolbar();
        changeStatusBar();
        initRead();     //初始化Read（测试）
        mDialog = LoadDialog.showDialog(ReadActivity.this);
        mDialog.show();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);      //显示返回按钮
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);               //显示返回图片
        }

        //添加刷新操作，并对刷新做监听
        mSlRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSlRefresh.setColorSchemeResources(R.color.colorPrimary);
        mSlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉刷新的时候会回调这个方法
                initRead();
                mDialog.show();
            }
        });

        //检测是否有缓存
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String readListString = prefs.getString("reads", null);
        if (readListString != null){
            //如果有缓存就直接解析
            List<Read> readList = Utilty.handleReadListResponse(readListString);
            Log.d("ReadActivity", "成功取出缓存：大小为：" + readList.size() + "");
            setAdapter(readList);
        } else {
            //如果没有缓存就从服务器中获取数据
            initRead();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 发送网络请求获取阅读列表数据
     */
    private void initRead(){
        HttpUtil.sendHttpRequest(READ_LIST_URL, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                final String responseText = response;
                final List<Read> readList = Utilty.handleReadListResponse(responseText);
                Log.d("ReadActivity", "集合2的大小为：" + readList.size() + "");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SPUtil.setParam(MyApplication.getContext(), "reads", responseText);
                        setAdapter(readList);
                    }
                });

            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyApplication.getContext(), "获取阅读列表失败", Toast.LENGTH_SHORT).show();
                        mSlRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    /**
     * 阅读列表适配器的设置
     * @param readList
     */
    private void setAdapter(final List<Read> readList){
        NoScrollListView listView = (NoScrollListView) findViewById(R.id.read_list_view);
        ReadListAdapter adapter = new ReadListAdapter
                (ReadActivity.this, R.layout.layout_card_read, readList);
        listView.setAdapter(adapter);
        mSlRefresh.setRefreshing(false);      //结束刷新事件
        mDialog.dismiss();
        //给listView的每一项做监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReadDetailActivity.actionStart(MyApplication.getContext(), readList.get(position).getItemId());
            }
        });
    }
}
