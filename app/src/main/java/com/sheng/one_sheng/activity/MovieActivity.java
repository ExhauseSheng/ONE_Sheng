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
import com.sheng.one_sheng.adapter.MovieListAdapter;
import com.sheng.one_sheng.bean.Movie;
import com.sheng.one_sheng.bean.Music;

import java.util.ArrayList;
import java.util.List;

import static com.sheng.one_sheng.R.id.toolbar;

public class MovieActivity extends BaseActivity {

    private List<Movie> movieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        setToolbar();
        changeStatusBar();
        initMovie();            //初始化movie（测试）
        MovieListAdapter adapter = new MovieListAdapter
                (MovieActivity.this, R.layout.layout_card_movie, movieList);
        ListView listView = (ListView) findViewById(R.id.movie_list_view);
        listView.setAdapter(adapter);
        //给ListView设置监听事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MovieActivity.this, AuthorActivity.class);
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

    private void initMovie(){
        for (int i = 0; i < 6; i++){
            Movie movie = new Movie();
            movie.setTitle("大圣|“思念”_那一晚因此与宵夜共眠");
            movie.setId(R.drawable.nav_icon);
            movie.setUserName("文 / Sheng、");
            movie.setForward("忍耐消融，觉得终有一天自己会被磨成珍珠，但到头来多是变成");
            movieList.add(movie);
        }
    }
}
