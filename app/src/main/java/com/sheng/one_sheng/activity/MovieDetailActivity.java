package com.sheng.one_sheng.activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.sheng.one_sheng.R;
import com.sheng.one_sheng.adapter.CommentListAdapter;
import com.sheng.one_sheng.bean.Comment;
import com.sheng.one_sheng.bean.Movie;
import com.sheng.one_sheng.ui.MyListView;
import com.sheng.one_sheng.util.HttpCallbackListener;
import com.sheng.one_sheng.util.HttpUtil;
import com.sheng.one_sheng.MyApplication;
import com.sheng.one_sheng.util.Utilty;

import java.util.List;

public class MovieDetailActivity extends BaseActivity {

    private ScrollView movieLayout;
    private TextView title;
    private TextView author;
    private TextView updateDate;
    private TextView essayContent;
    private TextView summary;
    private TextView authorName;
    private TextView authorDesc;
    private TextView authorFans;
    private TextView praiseNum;
    private TextView shareNum;
    private TextView commentNum;
    private MyListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        setToolbar();
        changeStatusBar();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);      //显示返回按钮
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);               //显示返回图片
        }

        //初始化各控件
        movieLayout = (ScrollView) findViewById(R.id.movie_detail_layout);
        title = (TextView) findViewById(R.id.tv_title);
        author = (TextView) findViewById(R.id.tv_author);
        updateDate = (TextView) findViewById(R.id.tv_update_date);
        essayContent = (TextView) findViewById(R.id.tv_essay_content);
        summary = (TextView) findViewById(R.id.movie_summary);
        authorName = (TextView) findViewById(R.id.tv_author_name);
        authorDesc = (TextView) findViewById(R.id.tv_author_desc);
        authorFans = (TextView) findViewById(R.id.tv_fans_num);
        praiseNum = (TextView) findViewById(R.id.tv_praise_num);
        shareNum = (TextView) findViewById(R.id.tv_share_num);
        commentNum = (TextView) findViewById(R.id.tv_comment_num);

        final String itemId;

        itemId = getIntent().getStringExtra("item_id");
        Log.d("MusicDetailActivity", "此时详细内容id为：" + itemId);
        movieLayout.setVisibility(View.INVISIBLE);
        //请求数据的时候先将ScrollView隐藏，不然空数据的界面看上去会很奇怪

        requestMovieDetail(itemId);     //请求详细内容
        requestCommentList(itemId);     //请求评论列表
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

    private void requestMovieDetail(String itemId){
        String url = "http://v3.wufazhuce.com:8000/api/movie/" + itemId + "/story/1/0?platform=android";
        Log.d("MovieDetailActivity", "传递之后详细内容的id为：" + itemId);
        HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                final String responseText = response;
                final Movie movie = Utilty.handleMovieDetailResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (movie != null){
                            SharedPreferences.Editor editor = PreferenceManager.
                                    getDefaultSharedPreferences(MyApplication.getContext()).edit();
                            editor.putString("music_detail", responseText);
                            editor.apply();
                            showMovieInfo(movie);   //内容显示
                        }
                        else {
                            Toast.makeText(MyApplication.getContext(), "请求失败，请检查网络是否可用", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void requestCommentList(String itemId){
        Log.d("MovieDetailActivity", "传递之后详细内容的id为：" + itemId);
        String url = "http://v3.wufazhuce.com:8000/api/comment/praiseandtime/movie/" + itemId +
                "/0?&platform=android";
        HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                final String responseText = response;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        List<Comment> commentList = Utilty.handleCommentResponse(responseText);
                        setAdapter(commentList);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyApplication.getContext(), "获取评论列表失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showMovieInfo(Movie movie){
        if (movie != null){
            title.setText(movie.getTitle());
            author.setText("文 / " + movie.getUserName());
            updateDate.setText(movie.getUpdateDate());
            essayContent.setText(Html.fromHtml(movie.getContent()));
            summary.setText(movie.getSummary());
            authorName.setText(movie.getUserName());
            authorDesc.setText(movie.getDes());
            authorFans.setText(movie.getFansTotal() + "关注");
            praiseNum.setText(movie.getPraiseNum() + "");
            shareNum.setText("0");
            commentNum.setText("0");
            movieLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setAdapter(List<Comment> commentList){
        Log.d("MovieDetailActivity", "此步骤没有出错！");
        CommentListAdapter adapter = new CommentListAdapter(MyApplication.getContext(),
                R.layout.layout_item_comment, commentList);
        listView = (MyListView) findViewById(R.id.lv_comment_list_view);
        listView.setAdapter(adapter);
    }
}
