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
import com.sheng.one_sheng.adapter.MusicListAdapter;
import com.sheng.one_sheng.bean.Music;
import com.sheng.one_sheng.ui.LoadDialog;
import com.sheng.one_sheng.ui.NoScrollListView;
import com.sheng.one_sheng.util.HttpCallbackListener;
import com.sheng.one_sheng.util.HttpUtil;
import com.sheng.one_sheng.util.SPUtil;
import com.sheng.one_sheng.util.Utilty;


import java.util.List;

import static com.sheng.one_sheng.Contents.MUSIC_LIST_URL;

public class MusicActivity extends BaseActivity {

    private SwipeRefreshLayout mSlRefresh;
    private LoadDialog mDialog;

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

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);      //显示返回按钮
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);     //显示返回图片
        }

        //添加刷新操作，并对刷新做监听
        mSlRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSlRefresh.setColorSchemeResources(R.color.colorPrimary);
        mSlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉刷新的时候会回调这个方法
                //重新从服务器获取数据
                initMusic();
                mDialog.show();
            }
        });

        //检测是否有缓存
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String musicListString = prefs.getString("musics", null);
        if (musicListString != null){
            //如果有缓存就直接解析
            List<Music> musicList = Utilty.handleMusicListResponse(musicListString);
            Log.d("MusicActivity", "成功取出缓存：大小为：" + musicList.size() + "");
            setAdapter(musicList);
        } else {
            //如果没有缓存就从服务器中获取数据
            initMusic();
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
    private void initMusic(){
        HttpUtil.sendHttpRequest(MUSIC_LIST_URL, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                final String responseText = response;
                final List<Music> musicList = Utilty.handleMusicListResponse(responseText);
                Log.d("MusicActivity", "集合2的大小为：" + musicList.size() + "");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //将服务器返回的数据缓存下来
                        SPUtil.setParam(MyApplication.getContext(), "musics", responseText);
                        setAdapter(musicList);
                    }
                });

            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MusicActivity.this, "获取音乐列表失败", Toast.LENGTH_SHORT).show();
                        mSlRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    /**
     * 设置音乐列表的适配器
     * @param musicList
     */
    private void setAdapter(final List<Music> musicList){

        NoScrollListView listView = (NoScrollListView) findViewById(R.id.music_list_view);
        MusicListAdapter adapter = new MusicListAdapter
                (MusicActivity.this, R.layout.layout_card_music, musicList);
        listView.setAdapter(adapter);
        mSlRefresh.setRefreshing(false);
        mDialog.dismiss();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MusicDetailActivity.actionStart(MyApplication.getContext(), musicList.get(position).getItemId());
            }
        });
    }
}
