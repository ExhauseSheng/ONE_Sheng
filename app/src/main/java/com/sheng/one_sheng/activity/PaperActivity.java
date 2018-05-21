package com.sheng.one_sheng.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sheng.one_sheng.MyApplication;
import com.sheng.one_sheng.R;
import com.sheng.one_sheng.adapter.MyPagerAdapter;
import com.sheng.one_sheng.adapter.PaperListAdapter;
import com.sheng.one_sheng.bean.Paper;
import com.sheng.one_sheng.ui.MyDialog;
import com.sheng.one_sheng.ui.MyListView;
import com.sheng.one_sheng.util.HttpCallbackListener;
import com.sheng.one_sheng.util.HttpUtil;
import com.sheng.one_sheng.util.ImageCallBack;
import com.sheng.one_sheng.util.ImageLoadAsyncTask;
import com.sheng.one_sheng.util.Utilty;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class PaperActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnTouchListener {

    private MyListView listView;
    public static final int PAPER_ID = 1;
    public static final int PAPER_LIST = 2;
    public static final int PAPER_IMAGE = 3;
    public static final int FINISH_DELAY = 4;
    private List<Paper> papers = new ArrayList<>();
    private List<String> imageUrls = new ArrayList<>();
    private DrawerLayout mDrawerLayout;     //滑动菜单布局
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
    public SwipeRefreshLayout swipeRefresh;
    private MyDialog dialog;
    private boolean isExit;     //用来判断是否退出的一个布尔值，用于按两次返回键退出程序

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper);
        setToolbar();
        changeStatusBar();
        dialog = MyDialog.showDialog(PaperActivity.this);
        dialog.show();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);      //显示菜单按钮
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);               //显示菜单图片
        }
        //菜单默认选中图文选项，并对菜单项做监听
        navView.setCheckedItem(R.id.nav_paper);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_read:
                        Intent intentRead = new Intent(MyApplication.getContext(), ReadActivity.class);
                        startActivity(intentRead);
                        break;
                    case R.id.nav_music:
                        Intent intentMusic = new Intent(MyApplication.getContext(), MusicActivity.class);
                        startActivity(intentMusic);
                        break;
                    case R.id.nav_movie:
                        Intent intentMovie = new Intent(MyApplication.getContext(), MovieActivity.class);
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

        //添加刷新操作，并对刷新做监听
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉刷新的时候会回调这个方法
                papers.clear();     //先将装有插画对象的集合清空，重新获取数据
                requestPaperId();
                dialog.show();
            }
        });

        requestPaperId();     //发送请求获取数据
    }

    /**
     * 菜单按钮监听事件
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);      //打开滑动菜单
        }
        return true;
    }

    /**
     * 发送网络请求获取装有插画id列表的集合
     */
    private void requestPaperId(){
        String paperIdUrl = "http://v3.wufazhuce.com:8000/api/hp/idlist/0?version=3.5.0&platform=android";
        HttpUtil.sendHttpRequest(paperIdUrl, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                //取出处理之后出来的Id集合
                final String responseText = response;
                final List<String> paperIdList = Utilty.handlePaperIdResponse(responseText);
                for (int i = 0; i < paperIdList.size(); i++){
                    Log.d("PaperActivity", paperIdList.get(i));
                }
                Message message = new Message();
                message.what = PAPER_ID;
                message.obj = paperIdList;
                handler.sendMessage(message);   //将Message对象发送出去
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PaperActivity.this, "获取id信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    /**
     * 根据传入的地址发送网络请求获取装有对应插画信息的对象，并发往信息处理器
     * @param url
     */
    private void requestPaper(String url) {
            //取出对应插画id中的插画内容
            HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    //将服务器返回来的数据解析成Paper实体类
                    final String responseText = response;
                    final Paper paper = Utilty.handlePaperDetailResponse(responseText);
                    Message message = new Message();
                    message.what = PAPER_LIST;
                    message.obj = paper;
                    handler.sendMessage(message);   //将Message对象发送出去

                    Message message2 = new Message();
                    message2.what = PAPER_IMAGE;
                    message2.obj = paper.getImageUrl();
                    handler.sendMessage(message2);   //将Message2对象发送出去
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PaperActivity.this, "获取详细信息失败", Toast.LENGTH_SHORT).show();
                            swipeRefresh.setRefreshing(false);
                        }
                    });
                }
            });
    }

    /**
     * 消息处理器
     */
    private Handler handler = new Handler(){

        public void handleMessage(Message msg){
            switch (msg.what){
                case PAPER_ID:
                    List<String> paperIds = (List<String>) msg.obj;
                    Log.d("PaperActivity", "第一个集合的大小为：" + paperIds.size() + "");
                    for (int i = 0; i < paperIds.size(); i++) {
                        String paperUrl = "http://v3.wufazhuce.com:8000/api/hp/detail/" +
                                paperIds.get(i) + "?version=3.5.0&platform=android";
                        requestPaper(paperUrl);
                    }
                    break;
                case PAPER_LIST:
                    Paper paper = (Paper) msg.obj;
                    papers.add(paper);
                    Log.d("PaperActivity2", "第二个集合的大小为：" + papers.size() + "");
                    if (papers.size() == 10){
                        setAdapter(papers);
                        swipeRefresh.setRefreshing(false);
                        dialog.dismiss();
                    }
                    break;
                case PAPER_IMAGE:
                    String imageUrl = (String) msg.obj;
                    Log.d("PaperActivity", imageUrl);
                    imageUrls.add(imageUrl);
                    if (imageUrls.size() == 10){
                        addImageView(imageUrls);
                    }
                    Log.d("PaperActivity2", "第三个集合的大小为：" + imageUrls.size() + "");
                case FINISH_DELAY:
                    isExit = false;
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 插画列表适配器的设置
     * @param paperList
     */
    private void setAdapter(List<Paper> paperList){
        listView = (MyListView) findViewById(R.id.paper_list_view);
        PaperListAdapter adapter = new PaperListAdapter
                (PaperActivity.this, R.layout.layout_card_paper, paperList, listView);
        listView.setAdapter(adapter);
    }

    /**
     * 给轮播图添加图片
     */
    private void addImageView(List<String> imageUrls){
        if (mItems != null){    //判断此时mItems是不是空集合
            mItems.clear();     //如果不是空集合就清空重新添加
        }
        for (int i = 0; i < imageUrls.size(); i++) {
            final ImageView view = new ImageView(this);
            String imgUrl = imageUrls.get(i);
            view.setImageResource(R.drawable.loading);
            //加载网络图片
            ImageLoadAsyncTask imageLoadAsyncTask = new ImageLoadAsyncTask(new ImageCallBack() {
                @Override
                public void callBitmap(Bitmap bitmap) {
                    if (bitmap != null){
                        view.setImageBitmap(bitmap);
                    }
                }
            });
            //设置缩放方式
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageLoadAsyncTask.execute(imgUrl);
            //将新建的view添加进Imageview集合里面
            mItems.add(view);
        }
        Log.d("PaperActivity", "轮播图图片集合大小为：" + mItems.size() + "");
        //通知适配器更新数据
        mAdapter.notifyDataSetChanged();
        //设置轮播图底部的小点
        setBottomIndicator();
    }

    /**
     * 设置轮播图小点的设置
     */
    private void setBottomIndicator(){
        //获取指示器（下面的小点）
        mBottomLiner = (LinearLayout) findViewById(R.id.live_indicator);
        //右下方小圆点
        mBottomImages = new ImageView[mItems.size()];
        for (int i = 0; i < mBottomImages.length; i++){
            ImageView imageView = new ImageView(this);
            //创建具有指定宽度、高度的一组新布局参数。
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            params.setMargins(5, 0, 5, 0);  //设置点的左上右下间隔
            imageView.setLayoutParams(params);
            //如果当前是第一个，设置为选中状态
            if (i == 0){
                imageView.setImageResource(R.drawable.indicator_select);
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
                        Thread.sleep(PaperActivity.VIEW_PAGER_DELAY);   //睡眠
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        mThread.start();
    }

    /**
     * ViewPager的监听事件
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        currentViewPageItem = position;
        if (mItems != null){
            //给当前页数取整
            position %= mBottomImages.length;
            //总页数
            int total = mBottomImages.length;
            //循环赋点
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){  //如果按下的是返回键
            if (!isExit){       //如果isExit是false的话
                isExit = true;      //变成true
                Toast.makeText(MyApplication.getContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                handler.sendEmptyMessageDelayed(FINISH_DELAY, 2000);  //延迟两秒发送一条消息，在处理器里面再次将isExit变成false，重复此操作
            } else {
                //如果在两秒内再次按下返回键，这时isExit就是true了，就会直接退出程序
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                System.exit(0);
            }
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 为防止内存泄漏，声明自己的Handler并弱引用Activity
     */
    private static class MyHandler extends Handler{

        /**
         * 用一个WeakReference管理Activity与Handler通信
         */
        private WeakReference<PaperActivity> mWeakReference;

        /**
         * 构造方法
         * @param activity
         */
        public MyHandler(PaperActivity activity){
            mWeakReference = new WeakReference<PaperActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    PaperActivity activity = mWeakReference.get();
                    if (activity.isAutoPlay){
                        activity.mViewPager.setCurrentItem(++activity.currentViewPageItem);
                    }
                    break;
            }
        }
    }
}
