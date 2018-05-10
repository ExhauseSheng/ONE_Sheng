package com.sheng.one_sheng.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sheng.one_sheng.R;
import com.sheng.one_sheng.adapter.PaperListAdapter;
import com.sheng.one_sheng.adapter.ReadListAdapter;
import com.sheng.one_sheng.bean.Read;

import java.util.ArrayList;
import java.util.List;

public class ReadActivity extends BaseActivity {

    private List<Read> readList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        setToolbar();
        changeStatusBar();
        initRead();     //初始化Read（测试）
        ReadListAdapter adapter = new ReadListAdapter
                (ReadActivity.this, R.layout.layout_card_read, readList);
        ListView listView = (ListView) findViewById(R.id.read_list_view);
        listView.setAdapter(adapter);
        //给listView的每一项做监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ReadActivity.this, AuthorActivity.class);
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

    private void initRead(){
        for (int i = 0; i < 6; i++){
            Read read = new Read();
            read.setTitle("大圣|“思念”_那一晚因此与宵夜共眠");
            read.setId(R.drawable.nav_icon_another);
            read.setUserName("文 / Sheng、");
            read.setForward("忍耐消融，觉得终有一天自己会被磨成珍珠，但到头来多是变成");
            read.setLikeCount(68 + i);
            read.setShareNum(45 + i);
            read.setCommentNum(18 + i);
            readList.add(read);
        }
    }
}
