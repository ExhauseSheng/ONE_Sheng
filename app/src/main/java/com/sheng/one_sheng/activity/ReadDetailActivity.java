package com.sheng.one_sheng.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.sheng.one_sheng.R;
import com.sheng.one_sheng.bean.Read;
import com.sheng.one_sheng.ui.LoadDialog;
import com.sheng.one_sheng.util.HttpCallbackListener;
import com.sheng.one_sheng.util.HttpUtil;
import com.sheng.one_sheng.util.Utilty;

import static com.sheng.one_sheng.Contents.COMMENT_NEXT;
import static com.sheng.one_sheng.Contents.READ_COMMENT_PRE;
import static com.sheng.one_sheng.Contents.READ_DETAIL_NEXT;
import static com.sheng.one_sheng.Contents.READ_DETAIL_PRE;

public class ReadDetailActivity extends CommentActivity {

    private ScrollView mSlreadLayout;
    private TextView mTvTitle;
    private TextView mTvAuthor;
    private TextView mTvUpdateDate;
    private TextView mTvForward;
    private TextView mTvEssayContent;
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
        Intent intent = new Intent(context, ReadDetailActivity.class);
        intent.putExtra("item_id", itemId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_detail);
        setToolbar();
        changeStatusBar();

        mDialog = LoadDialog.showDialog(ReadDetailActivity.this);
        mDialog.show();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);      //显示返回按钮
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);               //显示返回图片
        }

        //初始化各控件
        mSlreadLayout = (ScrollView) findViewById(R.id.read_detail_layout);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvAuthor = (TextView) findViewById(R.id.tv_author);
        mTvUpdateDate = (TextView) findViewById(R.id.tv_update_date);
        mTvForward = (TextView) findViewById(R.id.tv_forward);
        mTvEssayContent = (TextView) findViewById(R.id.tv_essay_content);
        mTvAuthorName = (TextView) findViewById(R.id.tv_author_name);
        mTvAuthorDesc = (TextView) findViewById(R.id.tv_author_desc);
        mTvAuthorFans = (TextView) findViewById(R.id.tv_fans_num);
        mTvPraiseNum = (TextView) findViewById(R.id.tv_praise_num);
        mTvShareNum = (TextView) findViewById(R.id.tv_share_num);
        mTvCommentNum = (TextView) findViewById(R.id.tv_comment_num);

        final String itemId;

        itemId = getIntent().getStringExtra("item_id");
        mSlreadLayout.setVisibility(View.INVISIBLE);
        //请求数据的时候先将ScrollView隐藏，不然空数据的界面看上去会很奇怪
        requestReadDetail(itemId);

        String url = READ_COMMENT_PRE + itemId + COMMENT_NEXT;
        requestCommentList(url);
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

    /**
     * 根据itemId获取装有文章详细内容数据的对象
     * @param itemId
     */
    private void requestReadDetail(final String itemId){
        Log.d("ReadDetailActivity", "传递之后详细内容id为：" + itemId);
        String url = READ_DETAIL_PRE + itemId + READ_DETAIL_NEXT;
        HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                final String responseText = response;
                final Read read = Utilty.handleReadDetailResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (read != null){
                            showReadInfo(read);   //内容显示
                        }
                        else {
                            Toast.makeText(ReadDetailActivity.this, "请求失败，请检查网络是否可用", Toast.LENGTH_SHORT).show();
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
     * 将数据展示在界面上
     * @param read
     */
    private void showReadInfo(Read read){

        mTvTitle.setText(read.getTitle());
        mTvAuthor.setText("文 / " + read.getUserName());
        mTvUpdateDate.setText(read.getUpdateDate());
        mTvForward.setText(read.getGuideWord());
        mTvEssayContent.setText(Html.fromHtml(read.getContent()));
        mTvAuthorName.setText(read.getUserName());
        mTvAuthorDesc.setText(read.getDes());
        mTvAuthorFans.setText(read.getFansTotal() + "关注");
        mTvPraiseNum.setText(read.getPraiseNum() + "");
        mTvShareNum.setText(read.getShareNum() + "");
        mTvCommentNum.setText(read.getCommentNum() + "");
        mSlreadLayout.setVisibility(View.VISIBLE);
        mDialog.dismiss();
    }
}
