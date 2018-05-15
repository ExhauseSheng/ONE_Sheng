package com.sheng.one_sheng.activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.sheng.one_sheng.R;
import com.sheng.one_sheng.bean.Read;
import com.sheng.one_sheng.util.HttpCallbackListener;
import com.sheng.one_sheng.util.HttpUtil;
import com.sheng.one_sheng.util.Utilty;

public class ReadDetailActivity extends BaseActivity {

    private ScrollView readLayout;
    private TextView title;
    private TextView author;
    private TextView updateDate;
    private TextView forward;
    private TextView essayContent;
    private TextView authorName;
    private TextView authorDesc;
    private TextView authorFans;
    private TextView praiseNum;
    private TextView shareNum;
    private TextView commentNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_detail);
        setToolbar();
        changeStatusBar();

        //初始化各控件
        readLayout = (ScrollView) findViewById(R.id.read_detail_layout);
        title = (TextView) findViewById(R.id.tv_title);
        author = (TextView) findViewById(R.id.tv_author);
        updateDate = (TextView) findViewById(R.id.tv_update_date);
        forward = (TextView) findViewById(R.id.tv_forward);
        essayContent = (TextView) findViewById(R.id.tv_essay_content);
        authorName = (TextView) findViewById(R.id.tv_author_name);
        authorDesc = (TextView) findViewById(R.id.tv_author_desc);
        authorFans = (TextView) findViewById(R.id.tv_fans_num);
        praiseNum = (TextView) findViewById(R.id.tv_praise_num);
        shareNum = (TextView) findViewById(R.id.tv_share_num);
        commentNum = (TextView) findViewById(R.id.tv_comment_num);

        final String itemId;

        itemId = getIntent().getStringExtra("item_id");
        Log.d("ReadDetailActivity", "此时详细内容id为：" + itemId);
        readLayout.setVisibility(View.INVISIBLE);
        //请求数据的时候先将ScrollView隐藏，不然空数据的界面看上去会很奇怪
        requestReadDetail(itemId);
    }

    private void requestReadDetail(final String itemId){
        Log.d("ReadDetailActivity", "传递之后详细内容id为：" + itemId);
        String url = "http://v3.wufazhuce.com:8000/api/essay/" + itemId + "?platform=android";
        HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                final String responseText = response;
                final Read read = Utilty.handleReadDetailResponse(response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (read != null){
                            SharedPreferences.Editor editor = PreferenceManager.
                                    getDefaultSharedPreferences(ReadDetailActivity.this).edit();
                            editor.putString("read_detail", responseText);
                            editor.apply();
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

    private void showReadInfo(Read read){
        if (read != null){
            title.setText(read.getTitle());
            author.setText("文 / " + read.getUserName());
            updateDate.setText(read.getUpdateDate());
            forward.setText(read.getGuideWord());
            essayContent.setText(Html.fromHtml(read.getContent()));
            authorName.setText(read.getUserName());
            authorDesc.setText(read.getDes());
            authorFans.setText(read.getFansTotal() + "关注");
            praiseNum.setText(read.getPraiseNum() + "");
            shareNum.setText(read.getShareNum() + "");
            commentNum.setText(read.getCommentNum() + "");
            readLayout.setVisibility(View.VISIBLE);
        }
    }
}
