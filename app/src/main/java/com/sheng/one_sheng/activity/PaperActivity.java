package com.sheng.one_sheng.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
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
import com.sheng.one_sheng.bean.Paper;
import com.sheng.one_sheng.util.HttpCallbackListener;
import com.sheng.one_sheng.util.HttpUtil;
import com.sheng.one_sheng.util.Utilty;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class PaperActivity extends BaseActivity {

    public List<String> paperIdList = new ArrayList<>();
    public List<Paper> paperList = new ArrayList<>();

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

    public void requestPaperId(){
        String paperIdUrl = "http://v3.wufazhuce.com:8000/api/hp/idlist/0?version=3.5.0&platform=android";
        HttpUtil.sendHttpRequest(paperIdUrl, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                //取出处理之后出来的Id集合
                String responseText = response;
                paperIdList = Utilty.handlePaperIdResponse(responseText);

                for (int i = 0; i < paperIdList.size(); i++){
                    String paperUrl = "http://v3.wufazhuce.com:8000/api/hp/detail/" + paperIdList.get(i) +
                                    "?version=3.5.0&platform=android";
                    requestPaper(paperUrl);
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PaperActivity.this, "获取信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    //向服务器发送请求并获取到返回的数据
    public void requestPaper(String paperUrl) {

        //循环取出插画id列表中的数据
            HttpUtil.sendHttpRequest(paperUrl, new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    //将服务器返回来的数据解析成Paper实体类
                    String responseText = response;
                    Paper paper = Utilty.handlePaperDetailResponse(responseText);
                    paperList.add(paper);
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PaperActivity.this, "获取信息失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper);
        setToolbar();
        changeStatusBar();
        requestPaperId();     //发送请求获取数据

        PaperListAdapter adapter = new PaperListAdapter
                (PaperActivity.this, R.layout.layout_card_paper, paperList);
        ListView listView = (ListView) findViewById(R.id.paper_list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PaperActivity.this, AuthorActivity.class);
                startActivity(intent);
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);      //显示返回按钮
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);               //显示返回图片
        }
    }

}
