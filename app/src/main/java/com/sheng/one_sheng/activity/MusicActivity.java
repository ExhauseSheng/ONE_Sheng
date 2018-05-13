package com.sheng.one_sheng.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sheng.one_sheng.R;
import com.sheng.one_sheng.adapter.MusicListAdapter;
import com.sheng.one_sheng.bean.Music;
import com.sheng.one_sheng.util.HttpCallbackListener;
import com.sheng.one_sheng.util.HttpUtil;
import com.sheng.one_sheng.util.Utilty;


import java.util.List;

public class MusicActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        setToolbar();
        changeStatusBar();
        initMusic();     //初始化Read（测试）

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);      //显示返回按钮
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);               //显示返回图片
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

    private void initMusic(){
        String url = "http://v3.wufazhuce.com:8000/api/channel/music/more/0?platform=android";
        HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                final String responseText = response;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("MusicActivity", "**************************");
                        List<Music> musicList = Utilty.handleMusicListResponse(responseText);
                        Log.d("MusicActivity", "集合2的大小为：" + musicList.size() + "");
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
                    }
                });
            }
        });
    }

    private void setAdapter(List<Music> musicList){

        MusicListAdapter adapter = new MusicListAdapter
                (MusicActivity.this, R.layout.layout_card_music, musicList);
        ListView listView = (ListView) findViewById(R.id.music_list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MusicActivity.this, AuthorActivity.class);
                startActivity(intent);
            }
        });
    }
}
