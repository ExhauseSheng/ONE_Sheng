package com.sheng.one_sheng.activity;

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
import android.widget.ListView;
import android.widget.Toast;

import com.sheng.one_sheng.MyApplication;
import com.sheng.one_sheng.R;
import com.sheng.one_sheng.adapter.MusicListAdapter;
import com.sheng.one_sheng.bean.Music;
import com.sheng.one_sheng.ui.MyDialog;
import com.sheng.one_sheng.ui.MyListView;
import com.sheng.one_sheng.util.HttpCallbackListener;
import com.sheng.one_sheng.util.HttpUtil;
import com.sheng.one_sheng.util.SPUtil;
import com.sheng.one_sheng.util.Utilty;


import java.util.List;

public class MusicActivity extends BaseActivity {

    private SwipeRefreshLayout swipeRefresh;
    private MyDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        setToolbar();
        changeStatusBar();
        dialog = MyDialog.showDialog(MusicActivity.this);
        dialog.show();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);      //显示返回按钮
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);               //显示返回图片
        }

        //添加刷新操作，并对刷新做监听
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉刷新的时候会回调这个方法
                //重新从服务器获取数据
                initMusic();
                dialog.show();
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
        String url = "http://v3.wufazhuce.com:8000/api/channel/music/more/0?platform=android";
        HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                final String responseText = response;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<Music> musicList = Utilty.handleMusicListResponse(responseText);
                        Log.d("MusicActivity", "集合2的大小为：" + musicList.size() + "");
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
                        swipeRefresh.setRefreshing(false);
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

        MusicListAdapter adapter = new MusicListAdapter
                (MusicActivity.this, R.layout.layout_card_music, musicList);
        MyListView listView = (MyListView) findViewById(R.id.music_list_view);
        listView.setAdapter(adapter);
        swipeRefresh.setRefreshing(false);
        dialog.dismiss();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MusicActivity.this, MusicDetailActivity.class);
                intent.putExtra("item_id", musicList.get(position).getItemId());
                Log.d("MusicActivity", "传递之前为：" + musicList.get(position).getItemId());
                startActivity(intent);
            }
        });
    }
}
