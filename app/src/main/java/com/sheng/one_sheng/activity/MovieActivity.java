package com.sheng.one_sheng.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.sheng.one_sheng.GlobalContext;
import com.sheng.one_sheng.R;
import com.sheng.one_sheng.adapter.MovieListAdapter;
import com.sheng.one_sheng.bean.Movie;
import com.sheng.one_sheng.ui.LoadDialog;
import com.sheng.one_sheng.ui.OnRefreshListener;
import com.sheng.one_sheng.ui.RefreshListView;
import com.sheng.one_sheng.util.HttpCallbackListener;
import com.sheng.one_sheng.util.HttpUtil;
import com.sheng.one_sheng.util.SPUtil;
import com.sheng.one_sheng.util.Utilty;

import java.util.ArrayList;
import java.util.List;

import static com.sheng.one_sheng.Contents.LIST_MORE_TIME;
import static com.sheng.one_sheng.Contents.LIST_REFRESH_TIME;
import static com.sheng.one_sheng.Contents.MOVIE_LIST;
import static com.sheng.one_sheng.Contents.MOVIE_LIST_URL;
import static com.sheng.one_sheng.Contents.MOVIE_MORE;
import static com.sheng.one_sheng.Contents.MOVIE_MORE_URL;

public class MovieActivity extends BaseActivity implements OnRefreshListener {

    private LoadDialog mDialog;        //对话窗口
    private List<Movie> mMovieList;
    private RefreshListView mListView;
    private MovieListAdapter mAdapter;
    private boolean isFirstLoadingMore = true;  //判断是不是第一次上拉加载更多

    /**
     * 用于启动这个活动的方法
     * @param context
     */
    public static void actionStart(Context context){
        Intent intent = new Intent(context, MovieActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        setToolbar();
        changeStatusBar();
        mDialog = LoadDialog.showDialog(MovieActivity.this);
        mDialog.show();
        mListView = (RefreshListView) findViewById(R.id.movie_list_view);
        mListView.setOnRefreshListener(this);
        mMovieList = new ArrayList<>();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);      //显示返回按钮
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);               //显示返回图片
        }

        //取出缓存
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String movieListString = prefs.getString("movie_list", null);
        if (movieListString != null){
            //如果有缓存就直接解析
            mMovieList = Utilty.handleMovieListResponse(movieListString);
            Log.d("MovieActivity", "成功取出缓存");
            setAdapter();
        } else {
            //如果没有缓存就从服务器中获取数据
            initMovie(MOVIE_LIST_URL);
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
    private void initMovie(final String url){
        HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                final String responseText = response;
                final List<Movie> movies = Utilty.handleMovieListResponse(responseText);

                if (url.equals(MOVIE_LIST_URL)){
                    Message message = new Message();
                    message.what = MOVIE_LIST;
                    message.obj = movies;
                    handler.sendMessage(message);   //将Message对象发送出去
                    //同时将服务器返回的数据缓存起来
                    SPUtil.setParam(GlobalContext.getContext(), "movie_list", responseText);

                } else if (url.equals(MOVIE_MORE_URL)){  //如果加载更多
                    for (int i =0; i < movies.size(); i++){
                        for (int j = 0; j < mMovieList.size(); j++){
                            if (movies.get(i).getId().equals(mMovieList.get(j).getId())){     //根据id来判断有没有重复的内容
                                movies.remove(i);    //如果有重复的内容就删除掉
                            }
                        }
                    }
                    Message message = new Message();
                    message.what = MOVIE_MORE;
                    message.obj = movies;            //将删除重复对象之后新的集合发送出去
                    handler.sendMessage(message);   //将Message对象发送出去
                    //同时将服务器返回的数据缓存起来
                    SPUtil.setParam(GlobalContext.getContext(), "movie_more", responseText);
                }
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

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MOVIE_LIST:
                    mMovieList = (List<Movie>)msg.obj;
                    setAdapter();
                    break;
                case MOVIE_MORE:
                    List<Movie> movieList = (List<Movie>) msg.obj;
                    for (int i = 0; i < movieList.size(); i++){
                        mMovieList.add(movieList.get(i));       //循环将新的集合的对象装到原有集合里面
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 影视列表适配器的设置
     */
    private void setAdapter(){
        mAdapter = new MovieListAdapter(GlobalContext.getContext(), R.layout.layout_card_movie, mMovieList);
        mListView.setAdapter(mAdapter);
        mDialog.dismiss();   //关闭加载框

        //给ListView设置监听事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               MovieDetailActivity.actionStart(GlobalContext.getContext(),
                       mMovieList.get(position - 1).getItemId());
            }
        });
    }

    /**
     * 下拉刷新的回调方法
     */
    @Override
    public void onDownPullRefresh() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(LIST_REFRESH_TIME);   //加载时间
                mMovieList.clear();           //先将集合里面的内容清空重新收集一遍
                initMovie(MOVIE_LIST_URL);     //重新初始化阅读列表
                isFirstLoadingMore = true;   //重新变成第一次加载更多数据
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                mListView.hideHeaderView();         //同时隐藏头布局
            }
        }.execute(new Void[]{});
    }

    /**
     * 上拉加载更多的回调方法
     */
    @Override
    public void onLoadingMore() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(LIST_MORE_TIME);  //加载时间
                if (isFirstLoadingMore) {      //如果这是第一次加载更多数据
                    //取出缓存
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences
                            (GlobalContext.getContext());
                    String movieListString = prefs.getString("movie_more", null);
                    if (movieListString != null){
                        Log.d("MovieActivity", "取出缓存成功！");
                        List<Movie> movieList = Utilty.handleMovieListResponse(movieListString);
                        for (int i = 0; i < movieList.size(); i++){
                            mMovieList.add(movieList.get(i));       //循环将新的集合的对象装到原有集合里面
                        }
                    } else {
                        initMovie(MOVIE_MORE_URL);
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (isFirstLoadingMore){            //如果这是第一次加载更多数据
                    mAdapter.notifyDataSetChanged();    //通知适配器更新数据
                    isFirstLoadingMore = false;     //不再是第一次
                }else {
                    Toast.makeText(GlobalContext.getContext(), "已无更多", Toast.LENGTH_SHORT).show();
                }
                // 控制脚布局隐藏
                mListView.hideFooterView();
            }
        }.execute(new Void[]{});
    }
}
