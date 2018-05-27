package com.sheng.one_sheng.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.sheng.one_sheng.ui.OnRefreshListener;
import com.sheng.one_sheng.ui.RefreshListView;
import com.sheng.one_sheng.util.HttpCallbackListener;
import com.sheng.one_sheng.util.HttpUtil;
import com.sheng.one_sheng.util.SPUtil;
import com.sheng.one_sheng.util.Utilty;


import java.util.ArrayList;
import java.util.List;

import static com.sheng.one_sheng.Contents.LIST_MORE_TIME;
import static com.sheng.one_sheng.Contents.LIST_REFRESH_TIME;
import static com.sheng.one_sheng.Contents.READ_LIST;
import static com.sheng.one_sheng.Contents.READ_LIST_URL;
import static com.sheng.one_sheng.Contents.READ_MORE;
import static com.sheng.one_sheng.Contents.READ_MORE_URL;

public class ReadActivity extends BaseActivity implements OnRefreshListener {

    private LoadDialog mDialog;
    private List<Read> mReadList;
    private RefreshListView mListView;
    private ReadListAdapter mAdapter;
    private boolean isFirstLoadingMore = true;  //判断是不是第一次上拉加载更多

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
        changeStatusBar();
        //设置Toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");   //将原本的标题栏清空，而用一个新的TextView代替
        setSupportActionBar(mToolbar);
        //设置加载框
        mDialog = LoadDialog.showDialog(ReadActivity.this);
        mDialog.show();
        mListView = (RefreshListView) findViewById(R.id.read_list_view);
        mListView.setOnRefreshListener(this);
        mReadList = new ArrayList<>();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);      //显示返回按钮
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);               //显示返回图片
        }

        //检测是否有缓存
        String readListString = (String) SPUtil.getParam(MyApplication.getContext(), "read_list", "");
        if (readListString != null){
            //如果有缓存就直接解析
            mReadList = Utilty.handleReadListResponse(readListString);
            Log.d("ReadActivity", "成功取出缓存");
            setAdapter();
        } else {
           //如果没有缓存就从服务器中获取数据
            initRead(READ_LIST_URL);
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
    private void initRead(final String url){
        HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                final String responseText = response;
                final List<Read> reads = Utilty.handleReadListResponse(responseText);
                if (url.equals(READ_LIST_URL)){
                    //发送消息
                    handler.obtainMessage(READ_LIST, reads).sendToTarget();
                    //同时将数据缓存下来
                    SPUtil.setParam(MyApplication.getContext(), "read_list", responseText);

                } else if (url.equals(READ_MORE_URL)){  //如果加载更多
                    for (int i =0; i < reads.size(); i++){
                        for (int j = 0; j < mReadList.size(); j++){
                            if (reads.get(i).getId().equals(mReadList.get(j).getId())){     //根据id来判断有没有重复的内容
                                reads.remove(i);    //如果有重复的内容就删除掉
                            }
                        }
                    }
                    //发送消息
                    handler.obtainMessage(READ_MORE, reads).sendToTarget();
                    //同时将数据缓存下来
                    SPUtil.setParam(MyApplication.getContext(), "read_more", responseText);
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyApplication.getContext(), "获取阅读列表失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case READ_LIST:
                    mReadList = (List<Read>)msg.obj;
                    setAdapter();
                    break;
                case READ_MORE:
                    List<Read> readList = (List<Read>) msg.obj;
                    for (int i = 0; i < readList.size(); i++){
                        mReadList.add(readList.get(i));     //将新集合里面的内容赋到原有集合里面
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 阅读列表适配器的设置
     */
    private void setAdapter(){
        mAdapter = new ReadListAdapter(ReadActivity.this, R.layout.layout_card_read, mReadList);
        mListView.setAdapter(mAdapter);
        mDialog.dismiss();

        //给listView的每一项做监听
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReadDetailActivity.actionStart(MyApplication.getContext(),
                        mReadList.get(position - 1).getItemId());
            }
        });
    }

    /**
     * 下拉刷新的回调方法
     */
    @Override
    public void onDownPullRefresh() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(LIST_REFRESH_TIME);
                mReadList.clear();           //先将集合里面的内容清空重新收集一遍
                initRead(READ_LIST_URL);     //重新初始化阅读列表
                isFirstLoadingMore = true;   //重新变成第一次加载更多数据
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                mListView.hideHeaderView();         //同时隐藏头布局
            }
        }.execute(new Void[]{});
    }

    /**
     * 加载更多的回调方法
     */
    @Override
    public void onLoadingMore() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(LIST_MORE_TIME);  //加载时间
                if (isFirstLoadingMore) {      //如果这是第一次加载更多数据
                    //取出缓存
                    String readListString = (String) SPUtil.getParam(MyApplication.getContext(), "read_more", "");
                    if (readListString != null){
                        //如果有缓存就直接解析
                        List<Read> readList = Utilty.handleReadListResponse(readListString);
                        for (int i = 0; i < readList.size(); i++){
                            mReadList.add(readList.get(i));         //将新的内容添加到原来的集合里面
                        }
                    } else {
                        //如果没有缓存就从服务器中获取数据
                        initRead(READ_MORE_URL);
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (isFirstLoadingMore){            //如果这是第一次加载更多数据
                    mAdapter.notifyDataSetChanged();
                    isFirstLoadingMore = false;     //不再是第一次
                }else {
                    Toast.makeText(MyApplication.getContext(), "已无更多", Toast.LENGTH_SHORT).show();
                }
                // 控制脚布局隐藏
                mListView.hideFooterView();
            }
        }.execute(new Void[]{});
    }
}
