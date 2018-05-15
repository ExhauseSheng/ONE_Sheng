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
import com.sheng.one_sheng.adapter.MovieListAdapter;
import com.sheng.one_sheng.bean.Movie;
import com.sheng.one_sheng.bean.Music;
import com.sheng.one_sheng.util.HttpCallbackListener;
import com.sheng.one_sheng.util.HttpUtil;
import com.sheng.one_sheng.util.Utilty;

import java.util.ArrayList;
import java.util.List;

import static com.sheng.one_sheng.R.id.toolbar;

public class MovieActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        setToolbar();
        changeStatusBar();
        initMovie();            //初始化movie（测试）

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

    private void initMovie(){
        String url = "http://v3.wufazhuce.com:8000/api/channel/movie/more/0?platform=android";
        HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                final String responseText = response;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("MusicActivity", "**************************");
                        List<Movie> movieList = Utilty.handleMovieListResponse(responseText);
                        Log.d("MusicActivity", "集合2的大小为：" + movieList.size() + "");
                        setAdapter(movieList);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MovieActivity.this, "获取视频列表失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void setAdapter(final List<Movie> movieList){
        MovieListAdapter adapter = new MovieListAdapter
                (MovieActivity.this, R.layout.layout_card_movie, movieList);
        ListView listView = (ListView) findViewById(R.id.movie_list_view);
        listView.setAdapter(adapter);
        //给ListView设置监听事件

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MovieActivity.this, MovieDetailActivity.class);
                intent.putExtra("item_id", movieList.get(position).getItemId());
                startActivity(intent);
            }
        });
    }
}
