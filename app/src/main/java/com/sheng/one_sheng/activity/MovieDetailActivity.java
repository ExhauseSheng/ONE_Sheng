package com.sheng.one_sheng.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.sheng.one_sheng.R;
import com.sheng.one_sheng.bean.Movie;
import com.sheng.one_sheng.ui.LoadDialog;
import com.sheng.one_sheng.util.HttpCallbackListener;
import com.sheng.one_sheng.util.HttpUtil;
import com.sheng.one_sheng.MyApplication;
import com.sheng.one_sheng.util.Utilty;

import static com.sheng.one_sheng.Contents.COMMENT_NEXT;
import static com.sheng.one_sheng.Contents.MOVIE_COMMENT_PRE;
import static com.sheng.one_sheng.Contents.MOVIE_DETAIL_NEXT;
import static com.sheng.one_sheng.Contents.MOVIE_DETAIL_PRE;

public class MovieDetailActivity extends CommentActivity {

    private ScrollView mSvMovieLayout;
    private TextView mTvTitle;
    private TextView mTvAuthor;
    private TextView mTvUpdateDate;
    private TextView mTvEssayContent;
    private TextView mTvSummary;
    private TextView mTvAuthorName;
    private TextView mTvAuthorDesc;
    private TextView mTvAuthorFans;
    private TextView mTvPraiseNum;
    private TextView mTvShareNum;
    private TextView mTvCommentNum;
    private LoadDialog mDialog;

    /**
     * 用于启动这个活动的方法
     * @param context
     */
    public static void actionStart(Context context, String itemId){
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra("item_id", itemId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        changeStatusBar();
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");   //将原本的标题栏清空，而用一个新的TextView代替
        setSupportActionBar(mToolbar);
        mDialog = LoadDialog.showDialog(MovieDetailActivity.this);
        mDialog.show();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);      //显示返回按钮
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);     //显示返回图片
        }

        //初始化各控件
        mSvMovieLayout = (ScrollView) findViewById(R.id.movie_detail_layout);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvAuthor = (TextView) findViewById(R.id.tv_author);
        mTvUpdateDate = (TextView) findViewById(R.id.tv_update_date);
        mTvEssayContent = (TextView) findViewById(R.id.tv_essay_content);
        mTvSummary = (TextView) findViewById(R.id.movie_summary);
        mTvAuthorName = (TextView) findViewById(R.id.tv_author_name);
        mTvAuthorDesc = (TextView) findViewById(R.id.tv_author_desc);
        mTvAuthorFans = (TextView) findViewById(R.id.tv_fans_num);
        mTvPraiseNum = (TextView) findViewById(R.id.tv_praise_num);
        mTvShareNum = (TextView) findViewById(R.id.tv_share_num);
        mTvCommentNum = (TextView) findViewById(R.id.tv_comment_num);

        final String itemId;

        itemId = getIntent().getStringExtra("item_id");
        mSvMovieLayout.setVisibility(View.INVISIBLE);
        //请求数据的时候先将ScrollView隐藏，不然空数据的界面看上去会很奇怪

        requestMovieDetail(itemId);     //请求影视内容

        String url = MOVIE_COMMENT_PRE + itemId + COMMENT_NEXT;
        requestCommentList(url);        //请求评论列表
    }

    /**
     * 给返回键做监听
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
     * 根据itemId发送网络请求获取装有影视详细内容的对象
     * @param itemId
     */
    private void requestMovieDetail(final String itemId){
        String url = MOVIE_DETAIL_PRE + itemId + MOVIE_DETAIL_NEXT;
        HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                final Movie movie = Utilty.handleMovieDetailResponse(response);
                runOnUiThread(new Runnable() {
                    /**
                     * 切换到主线程进行ui操作
                     */
                    @Override
                    public void run() {
                        if (movie != null){
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

    /**
     * 将数据显示在界面上
     * @param movie
     */
    private void showMovieInfo(Movie movie){
        if (movie != null){
            mTvTitle.setText(movie.getTitle());
            mTvAuthor.setText("文 / " + movie.getUserName());
            mTvUpdateDate.setText(movie.getUpdateDate());
            mTvEssayContent.setText(Html.fromHtml(movie.getContent()));
            mTvSummary.setText(movie.getSummary());
            mTvAuthorName.setText(movie.getUserName());
            mTvAuthorDesc.setText(movie.getDes());
            mTvAuthorFans.setText(movie.getFansTotal() + "关注");
            mTvPraiseNum.setText(movie.getPraiseNum() + "");
            mTvShareNum.setText("0");
            mTvCommentNum.setText("0");
            mSvMovieLayout.setVisibility(View.VISIBLE);
            mDialog.dismiss();
        }
    }
}
