package com.sheng.one_sheng.activity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.sheng.one_sheng.MyApplication;
import com.sheng.one_sheng.R;
import com.sheng.one_sheng.adapter.CommentListAdapter;
import com.sheng.one_sheng.bean.Comment;
import com.sheng.one_sheng.bean.Music;
import com.sheng.one_sheng.ui.MyDialog;
import com.sheng.one_sheng.ui.MyListView;
import com.sheng.one_sheng.util.HttpCallbackListener;
import com.sheng.one_sheng.util.HttpUtil;
import com.sheng.one_sheng.util.ImageCallBack;
import com.sheng.one_sheng.util.ImageLoadAsyncTask;
import com.sheng.one_sheng.util.Utilty;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static com.sheng.one_sheng.util.HttpUtil.downloadBitmap;
import static com.sheng.one_sheng.MyApplication.getContext;

public class MusicDetailActivity extends BaseActivity {

    private ScrollView musicLayout;
    private ImageView musicImage;
    private TextView songName;
    private TextView songAlbum;
    private TextView songInfo;
    private TextView songLyricDetail;
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
    private MyDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_detail);
        setToolbar();
        changeStatusBar();
        dialog = MyDialog.showDialog(MusicDetailActivity.this);
        dialog.show();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);      //显示返回按钮
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);               //显示返回图片
        }

        //初始化各控件
        musicLayout = (ScrollView) findViewById(R.id.music_detail_layout);
        musicImage = (ImageView) findViewById(R.id.music_image);
        songName = (TextView) findViewById(R.id.tv_song_name);
        songAlbum = (TextView) findViewById(R.id.tv_song_album);
        songInfo = (TextView) findViewById(R.id.tv_song_info);
        songLyricDetail = (TextView) findViewById(R.id.tv_song_lyric_detail);
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
        Log.d("MusicDetailActivity", "此时详细内容id为：" + itemId);
        musicLayout.setVisibility(View.INVISIBLE);
        //请求数据的时候先将ScrollView隐藏，不然空数据的界面看上去会很奇怪
        requestMusicDetail(itemId);
        String url = "http://v3.wufazhuce.com:8000/api/comment/praiseandtime/music/" + itemId +
                "/0?&platform=android";
        requestCommentList(url, itemId);
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
        Log.d("MusicDetailActivity", "传递之后详细内容的id为：" + itemId);
        HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                final String responseText = response;
                final Music music = Utilty.handleMusicDetailResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (music != null){
                            SharedPreferences.Editor editor = PreferenceManager.
                                    getDefaultSharedPreferences(MusicDetailActivity.this).edit();
                            editor.putString("music_detail", responseText);
                            editor.apply();
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
            musicImage.setImageResource(R.drawable.loading);
            String url = music.getCover();
            ImageLoadAsyncTask imageLoadAsyncTask = new ImageLoadAsyncTask(new ImageCallBack() {
                @Override
                public void callBitmap(Bitmap bitmap) {
                    if (bitmap != null){
                        musicImage.setImageBitmap(bitmap);
                    }
                }
            });
            imageLoadAsyncTask.execute(url);

            songName.setText(" 歌曲： 《 " + music.getSongTitle() + " 》");
            songAlbum.setText("专辑： 《 " + music.getAlbum() + " 》");
            songInfo.setText(music.getInfo());
            songLyricDetail.setText(music.getLyric());
            title.setText(music.getStoryTitle());
            author.setText("文 / " + music.getUserName());
            updateDate.setText(music.getUpdateDate());
            forward.setText(music.getForward());

            NetWorkImageGetter imageGetter = new NetWorkImageGetter();
            Spanned spanned = Html.fromHtml(music.getStory(), imageGetter, null);
            essayContent.setText(spanned);

            authorName.setText(music.getUserName());
            authorDesc.setText(music.getDes());
            authorFans.setText(music.getFansTotal() + "关注");
            praiseNum.setText(music.getPraiseNum() + "");
            shareNum.setText(music.getShareNum() + "");
            commentNum.setText(music.getCommentNum() + "");
            musicLayout.setVisibility(View.VISIBLE);
            dialog.dismiss();
        }
    }

    /**
     * 图片收集器：收集Html文本中的图片
     */
    private final class NetWorkImageGetter implements Html.ImageGetter {

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
        private LevelListDrawable mDrawable;

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
                mDrawable.addLevel(1, 1, drawable);
                mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mDrawable.setLevel(1);
                CharSequence charSequence = essayContent.getText();
                essayContent.setText(charSequence);
            }
        }
    }
}
