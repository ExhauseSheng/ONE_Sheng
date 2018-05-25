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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.sheng.one_sheng.GlobalContext;
import com.sheng.one_sheng.R;
import com.sheng.one_sheng.adapter.MusicListAdapter;
import com.sheng.one_sheng.bean.Music;
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
import static com.sheng.one_sheng.Contents.MUSIC_LIST_URL;
import static com.sheng.one_sheng.Contents.MUSIC_MORE_URL;

public class MusicActivity extends BaseActivity implements OnRefreshListener {

    private LoadDialog mDialog;
    private List<Music> mMusicList;
    private RefreshListView mListView;
    private MusicListAdapter mAdapter;
    private boolean isFirstLoadingMore = true;  //判断是不是第一次上拉加载更多

    /**
     * 用于启动这个活动的方法
     * @param context
     */
    public static void actionStart(Context context){
        Intent intent = new Intent(context, MusicActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        setToolbar();
        changeStatusBar();
        mDialog = LoadDialog.showDialog(MusicActivity.this);
        mDialog.show();
        mListView = (RefreshListView) findViewById(R.id.music_list_view);
        mListView.setOnRefreshListener(this);
        mMusicList = new ArrayList<>();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);      //显示返回按钮
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);     //显示返回图片
        }

        //检测是否有缓存
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String musicListString = prefs.getString("music_list", null);
        if (musicListString != null){
            //如果有缓存就直接解析
            mMusicList = Utilty.handleMusicListResponse(musicListString);
            Log.d("MusicActivity", "成功取出缓存");
            setAdapter();
        } else {
            //如果没有缓存就从服务器中获取数据
            initMusic(MUSIC_LIST_URL);
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
     * 发送网络请求获取音乐列表数据
     */
    private void initMusic(final String url){
        HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                final String responseText = response;
                final List<Music> musics = Utilty.handleMusicListResponse(responseText);
                if (url.equals(MUSIC_LIST_URL)){
                    Message message = new Message();
                    message.what = 0;
                    message.obj = musics;
                    handler.sendMessage(message);   //将Message对象发送出去
                    //将数据缓存下来
                    SPUtil.setParam(GlobalContext.getContext(), "music_list", responseText);

                } else if (url.equals(MUSIC_MORE_URL)){  //如果加载更多
                    for (int i =0; i < musics.size(); i++){
                        for (int j = 0; j < mMusicList.size(); j++){
                            if (musics.get(i).getId().equals(mMusicList.get(j).getId())){//根据id来判断有没有重复的内容
                                musics.remove(i);    //如果有重复的内容就删除掉
                            }
                        }
                    }
                    Message message = new Message();
                    message.what = 1;
                    message.obj = musics;            //将删除之后新的集合发送出去
                    handler.sendMessage(message);   //将Message对象发送出去
                    //将数据缓存下来
                    SPUtil.setParam(GlobalContext.getContext(), "music_more", responseText);
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MusicActivity.this, "获取音乐列表失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 消息处理器
     */
    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mMusicList = (List<Music>)msg.obj;
                    setAdapter();   //设置适配器
                    break;
                case 1:
                    List<Music> musicList = (List<Music>) msg.obj;
                    for (int i = 0; i < musicList.size(); i++){
                        mMusicList.add(musicList.get(i));   //循环将新的集合内容加在原有集合里面
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 设置音乐列表的适配器
     */
    private void setAdapter(){
        mAdapter = new MusicListAdapter(GlobalContext.getContext(), R.layout.layout_card_music, mMusicList);
        mListView.setAdapter(mAdapter);
        mDialog.dismiss();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MusicDetailActivity.actionStart(GlobalContext.getContext(), mMusicList.get(position - 1).getItemId());
            }
        });
    }

    /**
     * 下拉刷新回调方法
     */
    @Override
    public void onDownPullRefresh() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(LIST_REFRESH_TIME);
                mMusicList.clear();           //先将集合里面的内容清空重新收集一遍
                initMusic(MUSIC_LIST_URL);     //重新初始化阅读列表
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
     * 上拉加载更多回调方法
     */
    @Override
    public void onLoadingMore() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(LIST_MORE_TIME);
                if (isFirstLoadingMore) {      //如果这是第一次加载更多数据
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(GlobalContext.getContext());
                    String musicListString = prefs.getString("music_more", null);
                    if (musicListString != null){
                        List<Music> musicList = Utilty.handleMusicListResponse(musicListString);
                        for (int i = 0; i < musicList.size(); i++){
                            mMusicList.add(musicList.get(i));   //循环将新的集合内容加在原有集合里面
                        }
                    } else {
                        initMusic(MUSIC_MORE_URL);  //发送网络请求获取更多内容
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (isFirstLoadingMore){            //如果这是第一次加载更多数据
                    mAdapter.notifyDataSetChanged();    //通知适配器更新数据
                    isFirstLoadingMore = false;     //这时候就不再是第一次加载更多了
                }else {
                    Toast.makeText(GlobalContext.getContext(), "已无更多", Toast.LENGTH_SHORT).show();
                }
                // 控制脚布局隐藏
                mListView.hideFooterView();
            }
        }.execute(new Void[]{});
    }
}
