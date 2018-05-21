package com.sheng.one_sheng.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.sheng.one_sheng.MyApplication;
import com.sheng.one_sheng.R;
import com.sheng.one_sheng.adapter.MovieListAdapter;
import com.sheng.one_sheng.bean.Movie;
import com.sheng.one_sheng.ui.MyDialog;
import com.sheng.one_sheng.ui.MyListView;
import com.sheng.one_sheng.util.HttpCallbackListener;
import com.sheng.one_sheng.util.HttpUtil;
import com.sheng.one_sheng.util.SPUtil;
import com.sheng.one_sheng.util.Utilty;

import java.util.List;

import static com.sheng.one_sheng.R.id.toolbar;

public class MovieActivity extends BaseActivity {

    private SwipeRefreshLayout swipeRefresh;    //刷新控件
    private MyDialog dialog;        //对话窗口

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        setToolbar();
        changeStatusBar();
        dialog = MyDialog.showDialog(MovieActivity.this);
        dialog.show();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);      //显示返回按钮
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);               //显示返回图片
        }

        //添加刷新操作，并对刷新做监听
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉刷新的时候会回调这个方法
                initMovie();
                dialog.show();  //显示加载框
            }
        });

        //取出缓存
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String movieListString = prefs.getString("movies", null);
        if (movieListString != null){
            //如果有缓存就直接解析
            List<Movie> moviesList = Utilty.handleMovieListResponse(movieListString);
            Log.d("MovieActivity", "成功取出缓存：大小为：" + moviesList.size() + "");
            setAdapter(moviesList);
        } else {
            //如果没有缓存就从服务器中获取数据
            initMovie();
            dialog.show();   //显示加载框
        }
    }

    /**
     * 对返回键做监听
     * @param item
     * @return
     */
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

    /**
     * 发送网络请求获取影视列表数据
     */
    private void initMovie(){
        String url = "http://v3.wufazhuce.com:8000/api/channel/movie/more/0?platform=android";
        HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                final String responseText = response;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //切换到主线程进行ui操作
                        List<Movie> movieList = Utilty.handleMovieListResponse(responseText);
                        Log.d("MovieActivity", "集合2的大小为：" + movieList.size() + "");
                        //将服务器返回的数据缓存下来
                        SPUtil.setParam(MyApplication.getContext(), "movies", responseText);
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
                        swipeRefresh.setRefreshing(false);      //结束刷新事件
                    }
                });
            }
        });
    }

    /**
     * 影视列表适配器的设置
     * @param movieList
     */
    private void setAdapter(final List<Movie> movieList){
        MyListView listView = (MyListView) findViewById(R.id.movie_list_view);
        MovieListAdapter adapter = new MovieListAdapter
                (MyApplication.getContext(), R.layout.layout_card_movie, movieList, listView);
        listView.setAdapter(adapter);
        swipeRefresh.setRefreshing(false);  //结束刷新事件
        dialog.dismiss();   //关闭加载框

        //给ListView设置监听事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyApplication.getContext(), MovieDetailActivity.class);
                intent.putExtra("item_id", movieList.get(position).getItemId());
                startActivity(intent);
            }
        });
    }
}
