package com.sheng.one_sheng.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sheng.one_sheng.R;
import com.sheng.one_sheng.adapter.MyPagerAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnTouchListener
{
    public static final int VIEW_PAGER_DELAY = 3500;       //睡眠3.5s
    private MyPagerAdapter mAdapter;
    private List<ImageView> mItems;         //轮播图片的集合
    private ImageView[] mBottomImages;      //底下小白点图片的集合
    private LinearLayout mBottomLiner;      //底下小白点图片的布局
    private ViewPager mViewPager;

    private int currentViewPageItem;    //当前页数
    private boolean isAutoPlay;     //是否自动播放
    private MyHandler mHandler;     //自定义Handler
    private Thread mThread;     //线程
    private Toolbar toolbar;    //定制toolbar
    private DrawerLayout mDrawerLayout;     //滑动菜单布局

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();
        changeStatusBar();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);      //显示菜单按钮
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);               //显示菜单图片
        }
        navView.setCheckedItem(R.id.nav_home);  //首页菜单默认选中
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_paper:
                        Intent intentPaper = new Intent(MainActivity.this, PaperActivity.class);
                        startActivity(intentPaper);
                        break;
                    case R.id.nav_read:
                        Intent intentRead = new Intent(MainActivity.this, ReadActivity.class);
                        startActivity(intentRead);
                        break;
                    case R.id.nav_music:
                        Intent intentMusic = new Intent(MainActivity.this, MusicActivity.class);
                        startActivity(intentMusic);
                        break;
                    case R.id.nav_movie:
                        Intent intentMovie = new Intent(MainActivity.this, MovieActivity.class);
                        startActivity(intentMovie);
                        break;
                    default:
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        mHandler = new MyHandler(this);
        //配置轮播图ViewPager
        mViewPager = (ViewPager) findViewById(R.id.live_view_pager);
        mItems = new ArrayList<>();
        mAdapter = new MyPagerAdapter(mItems, this);    //把图片集传进去
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnTouchListener(this);
        mViewPager.addOnPageChangeListener(this);
        isAutoPlay = true;

        //添加ImageView
        addImageView();
        mAdapter.notifyDataSetChanged();
        //设置底部4个小点
        setBottomIndicator();
    }

    private void addImageView(){
        ImageView view0 = new ImageView(this);
        view0.setImageResource(R.mipmap.pic0);
        ImageView view1 = new ImageView(this);
        view1.setImageResource(R.mipmap.pic1);
        ImageView view2 = new ImageView(this);
        view2.setImageResource(R.mipmap.pic2);

        view0.setScaleType(ImageView.ScaleType.CENTER_CROP);    //设置缩进方式
        view1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view2.setScaleType(ImageView.ScaleType.CENTER_CROP);

        mItems.add(view0);
        mItems.add(view1);
        mItems.add(view2);
    }

    // 菜单按钮监听事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);      //打开滑动菜单
        }
        return true;
    }

    // 设置4个小点
    private void setBottomIndicator(){
        //获取指示器（下面三个小点）
        mBottomLiner = (LinearLayout) findViewById(R.id.live_indicator);
        //右下方小圆点
        mBottomImages = new ImageView[mItems.size()];
        for (int i = 0; i < mBottomImages.length; i++){
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            params.setMargins(5, 0, 5, 0);  //设置点的左上右下间隔
            imageView.setLayoutParams(params);
            //如果当前是第一个，设置为选中状态
            if (i == 0){
                imageView.setImageResource(R.drawable.indicator_no_select);
            } else {
                imageView.setImageResource(R.drawable.indicator_no_select);
            }
            mBottomImages[i] = imageView;
            //添加到父容器
            mBottomLiner.addView(imageView);
        }

        //让其在最大值的中间开始滑动，一定要在mBottomImages初始化之前完成
        int mid = MyPagerAdapter.MAX_SCROLL_VALUE / 2;
        mViewPager.setCurrentItem(mid);
        currentViewPageItem = mid;

        //定时发送消息
        mThread = new Thread(){
            @Override
            public void run() {
                super.run();
                while (true){
                    mHandler.sendEmptyMessage(0);
                    try {
                        Thread.sleep(MainActivity.VIEW_PAGER_DELAY);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        mThread.start();
    }

    ////////////////////////
    // ViewPager的监听事件
    ///////////////////////
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        currentViewPageItem = position;
        if (mItems != null){
            position %= mBottomImages.length;
            int total = mBottomImages.length;

            for (int i = 0; i < total; i++){
                if (i == position){
                    mBottomImages[i].setImageResource(R.drawable.indicator_select);
                } else {
                    mBottomImages[i].setImageResource(R.drawable.indicator_no_select);
                }
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();     //获取事件
        switch (action){
            case MotionEvent.ACTION_DOWN:       //如果按住图片
                isAutoPlay = false;     //取消自动轮播
                break;
            case MotionEvent.ACTION_UP:         //如果按住之后抬上
                isAutoPlay = true;      //重新开启自动轮播
                break;
        }
        return false;
    }

    //为防止内存泄漏，声明自己的Handler并弱引用Activity
    private static class MyHandler extends Handler{

//        用一个WeakReference管理Activity与Handler通信
        private WeakReference<MainActivity> mWeakReference;

        public MyHandler(MainActivity activity){
            mWeakReference = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    MainActivity activity = mWeakReference.get();
                    if (activity.isAutoPlay){
                        activity.mViewPager.setCurrentItem(++activity.currentViewPageItem);
                    }
                    break;
            }
        }
    }
}
