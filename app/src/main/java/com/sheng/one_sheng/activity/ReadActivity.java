package com.sheng.one_sheng.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sheng.one_sheng.R;
import com.sheng.one_sheng.adapter.PaperListAdapter;
import com.sheng.one_sheng.adapter.ReadListAdapter;
import com.sheng.one_sheng.bean.Music;
import com.sheng.one_sheng.bean.Paper;
import com.sheng.one_sheng.bean.Read;
import com.sheng.one_sheng.util.HttpCallbackListener;
import com.sheng.one_sheng.util.HttpUtil;
import com.sheng.one_sheng.util.Utilty;

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
        String url = "http://v3.wufazhuce.com:8000/api/channel/reading/more/0?channel=wdj&version=4.0.2&platform=android";
        HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                final String responseText = response;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("ReadActivity", "**************************");
                        List<Read> readList = Utilty.handleReadListResponse(responseText);
                        Log.d("ReadActivity", "集合2的大小为：" + readList.size() + "");
                        setAdapter(readList);
                    }
                });

            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ReadActivity.this, "获取阅读列表失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void setAdapter(List<Read> readList){
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
    }
}
