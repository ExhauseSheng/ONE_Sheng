package com.sheng.one_sheng.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.sheng.one_sheng.GlobalContext;
import com.sheng.one_sheng.R;
import com.sheng.one_sheng.bean.Music;
import com.sheng.one_sheng.ui.LoadDialog;
import com.sheng.one_sheng.util.HttpCallbackListener;
import com.sheng.one_sheng.util.HttpUtil;
import com.sheng.one_sheng.util.Utilty;
import com.sheng.one_sheng.util.imageLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MusicDetailActivity extends CommentActivity {

    private ScrollView mSlMusicLayout;
    private ImageView mIvMusicImage;
    private TextView mTvSongName;
    private TextView mTvSongAlbum;
    private TextView mTvSongInfo;
    private TextView mTvSongLyricDetail;
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
        Intent intent = new Intent(context, MusicDetailActivity.class);
        intent.putExtra("item_id", itemId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_detail);
        setToolbar();
        changeStatusBar();
        mDialog = LoadDialog.showDialog(MusicDetailActivity.this);
        mDialog.show();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);      //显示返回按钮
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);               //显示返回图片
        }

        //初始化各控件
        mSlMusicLayout = (ScrollView) findViewById(R.id.music_detail_layout);
        mIvMusicImage = (ImageView) findViewById(R.id.music_image);
        mTvSongName = (TextView) findViewById(R.id.tv_song_name);
        mTvSongAlbum = (TextView) findViewById(R.id.tv_song_album);
        mTvSongInfo = (TextView) findViewById(R.id.tv_song_info);
        mTvSongLyricDetail = (TextView) findViewById(R.id.tv_song_lyric_detail);
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
        Log.d("MusicDetailActivity", "此时详细内容id为：" + itemId);
        mSlMusicLayout.setVisibility(View.INVISIBLE);
        //请求数据的时候先将ScrollView隐藏，不然空数据的界面看上去会很奇怪

        requestMusicDetail(itemId); //请求音乐详细内容

        String url = "http://v3.wufazhuce.com:8000/api/comment/praiseandtime/music/" + itemId +
                "/0?&platform=android";
        requestCommentList(url);    //请求评论列表
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
     * 根据itemId来发送网络请求获取装有音乐详细内容数据的对象
     * @param itemId
     */
    private void requestMusicDetail(String itemId){
        String url = "http://v3.wufazhuce.com:8000/api/music/detail/" + itemId + "?version=3.5.0&platform=android";
        HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                final String responseText = response;
                final Music music = Utilty.handleMusicDetailResponse(responseText);
                runOnUiThread(new Runnable() {
                    /**
                     * 切换到主线程进行ui操作
                     */
                    @Override
                    public void run() {
                        if (music != null){
                            showMusicInfo(music);   //内容显示
                        }
                        else {
                            Toast.makeText(MusicDetailActivity.this, "请求失败，请检查网络是否可用", Toast.LENGTH_SHORT).show();
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
     * 向界面展示信息
     * @param music
     */
    private void showMusicInfo(Music music){
        if (music != null){
            mIvMusicImage.setImageResource(R.drawable.loading);
            String url = music.getCover();
            imageLoader mLoader = new imageLoader(GlobalContext.getContext());
            mLoader.loadingImage(mIvMusicImage, url);   //异步加载专辑图片

            mTvSongName.setText(" 歌曲： 《 " + music.getSongTitle() + " 》");
            mTvSongAlbum.setText("专辑： 《 " + music.getAlbum() + " 》");
            mTvSongInfo.setText(music.getInfo());
            mTvSongLyricDetail.setText(music.getLyric());
            mTvTitle.setText(music.getStoryTitle());
            mTvAuthor.setText("文 / " + music.getUserName());
            mTvUpdateDate.setText(music.getUpdateDate());
            mTvForward.setText(music.getForward());

            NetWorkImageGetter imageGetter = new NetWorkImageGetter();
//            这是文本的接口，它将标记对象附加到它的范围。
            Spanned spanned = Html.fromHtml(music.getStory(), imageGetter, null);
            mTvEssayContent.setText(spanned);

            mTvAuthorName.setText(music.getUserName());
            mTvAuthorDesc.setText(music.getDes());
            mTvAuthorFans.setText(music.getFansTotal() + "关注");
            mTvPraiseNum.setText(music.getPraiseNum() + "");
            mTvShareNum.setText(music.getShareNum() + "");
            mTvCommentNum.setText(music.getCommentNum() + "");
            mSlMusicLayout.setVisibility(View.VISIBLE);
            mDialog.dismiss();
        }
    }

    /**
     * 图片收集器：检索Html文本中的img标签图像
     */
    private final class NetWorkImageGetter implements Html.ImageGetter {

        /**
         * 当HTML解析器遇到<img>标签时会回调用此方法。
         * @param source
         * @return
         */
        @Override
        public Drawable getDrawable(String source) {
            LevelListDrawable drawable = new LevelListDrawable();
            new LoadImage().execute(source, drawable);
            return drawable;
        }
    }

    /**
     * 异步加载Html中的图片
     */
    private final class LoadImage extends AsyncTask<Object, Void, Bitmap> {
        private LevelListDrawable mDrawable;    //一组Drawable

        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            mDrawable = (LevelListDrawable) params[1];

            try {
                InputStream is = new URL(source).openStream();
                return BitmapFactory.decodeStream(is);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null){
                BitmapDrawable drawable = new BitmapDrawable(bitmap);
                mDrawable.addLevel(1, 1, drawable);     //给drawable设置level
                //给drawable设置边界大小，必须设为图片的边际,不然TextView显示不出图片
                mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mDrawable.setLevel(1);
                //CharSequence是一个接口，char值的一个可读序列，此接口对许多不同种类的char序列提供统一的自读访问
                CharSequence charSequence = mTvEssayContent.getText();
                mTvEssayContent.setText(charSequence);
            }
        }
    }
}
