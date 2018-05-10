package com.sheng.one_sheng.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
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

    private List<Paper> paperList = new ArrayList<>();
    private List<String> paperIdlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper);
        setToolbar();
        changeStatusBar();
        initPaper();    //初始化（测试）
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

    //初始化列表中插画的内容
    private void initPaper(){
//        String paperIdUrl = "http://v3.wufazhuce.com:8000/api/hp/idlist/0?version=3.5.0&platform=android";
//        HttpUtil.sendHttpRequest(paperIdUrl, new HttpCallbackListener() {
//            @Override
//            public void onFinish(String response) {
//                //取出处理之后出来的Id数组
//                paperIdlist = Utilty.handlePaperIdResponse(response);
                //将服务器返回的信息缓存下来
//                SharedPreferences.Editor editor = PreferenceManager.
//                        getDefaultSharedPreferences(PaperActivity.this).edit();
//                editor.putString("paperId", response);
//                editor.apply();
//            }

//            @Override
//            public void onError(Exception e) {
//                e.printStackTrace();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(PaperActivity.this, "获取信息失败", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });

//        for (int i = 0; i < paperIdlist.size(); i++){
//            //循环取出每一个id，组成详细的插画信息地址
//            String paperUrl = "http://v3.wufazhuce.com:8000/api/hp/detail/" + paperIdlist.get(i) +
//                    "?version=3.5.0&platform=android";
//            HttpUtil.sendHttpRequest(paperUrl, new HttpCallbackListener() {
//                @Override
//                public void onFinish(String response) {
//                    //将服务器返回来的数据解析成Paper实体类
//                    Paper paper = Utilty.handlePaperDetailResponse(response);
//                    paperList.add(paper);
//                }
//
//                @Override
//                public void onError(Exception e) {
//                    e.printStackTrace();
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(PaperActivity.this, "获取信息失败", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            });
//        }
        for (int i = 0; i < 6; i++){
            Paper paper = new Paper();
            paper.setTitle("-  VOL.1529  -");
            paper.setId(R.drawable.nav_icon_another);
            paper.setTextAuthor("摄像  |  Sheng、");
            paper.setContent("人生的时间为太过于客观，时间太过于繁琐，摆在剧本里，轻松一句就可以交代的过场戏。");
            paper.setAuthorInfo(" 静 岛 ");
            paper.setPraiseNum(68 + i);
            paper.setShareNum(45 + i);
            paper.setCommentNum(18 + i);
            paperList.add(paper);
        }
    }
}
