package com.sheng.one_sheng.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.VoiceInteractor;
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

import com.sheng.one_sheng.MyApplication;
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

    private ListView listView;
    public static final int PAPER_ID = 1;
    public static final int PAPER_LIST = 2;
    private List<Paper> papers = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper);
        setToolbar();
        changeStatusBar();
        requestPaperId();     //发送请求获取数据

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

    private void requestPaperId(){
        String paperIdUrl = "http://v3.wufazhuce.com:8000/api/hp/idlist/0?version=3.5.0&platform=android";
        HttpUtil.sendHttpRequest(paperIdUrl, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                //取出处理之后出来的Id集合
                final String responseText = response;
                final List<String> paperIdList = Utilty.handlePaperIdResponse(responseText);
                for (int i = 0; i < paperIdList.size(); i++){
                    Log.d("PaperActivity", paperIdList.get(i));
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = PAPER_ID;
                        message.obj = paperIdList;
                        handler.sendMessage(message);   //将Message对象发送出去
                    }
                }).start();
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PaperActivity.this, "获取id信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    //向服务器发送请求并获取到返回的数据
    private void requestPaper(String url) {
            //取出对应插画id中的插画内容
            HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    //将服务器返回来的数据解析成Paper实体类
                    final String responseText = response;
                    final Paper paper = Utilty.handlePaperDetailResponse(responseText);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message message = new Message();
                            message.what = PAPER_LIST;
                            message.obj = paper;
                            handler.sendMessage(message);   //将Message对象发送出去
                        }
                    }).start();
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PaperActivity.this, "获取详细信息失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
    }

    private Handler handler = new Handler(){

        public void handleMessage(Message msg){
            switch (msg.what){
                case PAPER_ID:
                    List<String> paperIds = (List<String>) msg.obj;
                    Log.d("PaperActivity", "第一个集合的大小为：" + paperIds.size() + "");
                    for (int i = 0; i < paperIds.size(); i++) {
                        String paperUrl = "http://v3.wufazhuce.com:8000/api/hp/detail/" +
                                paperIds.get(i) + "?version=3.5.0&platform=android";
                        requestPaper(paperUrl);
                    }
                    break;
                case PAPER_LIST:
                    Paper paper = (Paper) msg.obj;
                    papers.add(paper);
                    Log.d("PaperActivity2", "第二个集合的大小为：" + papers.size() + "");
                    if (papers.size() == 10){
                        setAdapter(papers);
                    }
                default:
                    break;
            }
        }
    };

    private void setAdapter(List<Paper> paperList){

        PaperListAdapter adapter = new PaperListAdapter
                (PaperActivity.this, R.layout.layout_card_paper, paperList);
        listView = (ListView) findViewById(R.id.paper_list_view);
        listView.setAdapter(adapter);
    }
}
