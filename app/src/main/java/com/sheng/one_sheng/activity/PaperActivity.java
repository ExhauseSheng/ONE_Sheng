package com.sheng.one_sheng.activity;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.sheng.one_sheng.R;
import com.sheng.one_sheng.adapter.PaperListAdapter;
import com.sheng.one_sheng.bean.Paper;

import java.util.ArrayList;
import java.util.List;

public class PaperActivity extends BaseActivity {

    private List<Paper> paperList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper);
        setToolbar();
        changeStatusBar();
        initPaper();    //初始化（测试）
        PaperListAdapter adapter = new PaperListAdapter
                (PaperActivity.this, R.layout.layout_card_paper, paperList);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
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

    private void initPaper(){
        for (int i = 0; i < 6; i++){
            Paper paper = new Paper();
            paper.setTitle("-  VOL.1529  -");
            paper.setId(R.drawable.nav_icon_another);
            paper.setTextAuthor("摄像  |  Sheng、");
            paper.setContent("人生的时间为太过于客观，时间太过于繁琐，摆在剧本里，轻松一句就可以交代的过场戏。");
            paper.setAuthorInfo(" 静 岛 ");
            paper.setPraiseNum(68);
            paper.setShareNum(45);
            paper.setCommentNum(18);
            paperList.add(paper);
        }
    }
}
