package com.sheng.one_sheng.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sheng.one_sheng.R;
import com.sheng.one_sheng.adapter.MusicListAdapter;
import com.sheng.one_sheng.bean.Music;


import java.util.ArrayList;
import java.util.List;

public class MusicActivity extends BaseActivity {

    private List<Music> musicList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        setToolbar();
        changeStatusBar();
        initMusic();     //初始化Read（测试）
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
        for (int i = 0; i < 6; i++){
            Music music = new Music();
            music.setTitle("大圣|“思念”_那一晚因此与宵夜共眠");
            music.setId(R.drawable.nav_icon);
            music.setUserName("文 / Sheng、");
            music.setForward("忍耐消融，觉得终有一天自己会被磨成珍珠，但到头来多是变成");
            music.setLikeCount(68 + i);
            music.setShareNum(45 + i);
            music.setCommentNum(18 + i);
            musicList.add(music);
        }
    }
}
