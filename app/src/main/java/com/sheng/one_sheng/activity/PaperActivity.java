package com.sheng.one_sheng.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
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

import com.sheng.one_sheng.GlobalContext;
import com.sheng.one_sheng.R;
import com.sheng.one_sheng.adapter.MyPagerAdapter;
import com.sheng.one_sheng.adapter.PaperListAdapter;
import com.sheng.one_sheng.bean.Paper;
import com.sheng.one_sheng.ui.LoadDialog;
import com.sheng.one_sheng.ui.OnRefreshListener;
import com.sheng.one_sheng.ui.RefreshListView;
import com.sheng.one_sheng.util.HttpCallbackListener;
import com.sheng.one_sheng.util.HttpUtil;
import com.sheng.one_sheng.util.Utilty;
import com.sheng.one_sheng.util.imageLoader;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.sheng.one_sheng.Contents.FINISH_DELAY;
import static com.sheng.one_sheng.Contents.LIST_MORE_TIME;
import static com.sheng.one_sheng.Contents.LIST_REFRESH_TIME;
import static com.sheng.one_sheng.Contents.PAPER_ID;
import static com.sheng.one_sheng.Contents.PAPER_IMAGE;
import static com.sheng.one_sheng.Contents.PAPER_LIST;
import static com.sheng.one_sheng.Contents.PAPER_LIST_URL;
import static com.sheng.one_sheng.Contents.PAPER_MORE_URL;
import static com.sheng.one_sheng.Contents.VIEW_PAGER_DELAY;

public class PaperActivity extends BaseActivity implements OnRefreshListener, ViewPager.OnPageChangeListener, View.OnTouchListener {

    private RefreshListView mListView;
    private List<Paper> mPapers;
    private List<String> mImageUrls;
    private DrawerLayout mDrawerLayout;     //滑动菜单布局
    private PaperListAdapter mAdapter;   //ListView适配器
    private LoadDialog mDialog;
    private boolean isExit;     //用来判断是否退出的一个布尔值，用于按两次返回键退出程序
    private boolean isFirstLoadingMore = true;  //判断是不是第一次上拉加载更多

    private MyPagerAdapter mPaperAdapter;    //轮播图适配器
    private List<ImageView> mItems;         //轮播图片的集合
    private ImageView[] mIvBottomImages;      //底下小白点图片的集合
    private LinearLayout mLlBottomLiner;      //底下小白点图片的布局
    private ViewPager mViewPager;
    private int currentViewPageItem;    //当前页数
    private boolean isAutoPlay;     //是否自动播放
    private MyHandler mHandler;     //自定义Handler

    /**
     * 用于启动这个活动的方法
     * @param context
     */
    public static void actionStart(Context context){
        Intent intent = new Intent(context, PaperActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper);
        setToolbar();
        changeStatusBar();
        mDialog = LoadDialog.showDialog(PaperActivity.this);
        mDialog.show();
        mListView = (RefreshListView) findViewById(R.id.paper_list_view);
        mListView.setOnRefreshListener(this);
        mPapers = new ArrayList<>();
        mImageUrls = new ArrayList<>();
        mItems = new ArrayList<>();
        mHandler = new MyHandler(this);

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
                        ReadActivity.actionStart(GlobalContext.getContext());
                        break;
                    case R.id.nav_music:
                        MusicActivity.actionStart(GlobalContext.getContext());
                        break;
                    case R.id.nav_movie:
                        MovieActivity.actionStart(GlobalContext.getContext());
                        break;
                    default:
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });


        //配置轮播图ViewPager
        mViewPager = (ViewPager) findViewById(R.id.live_view_pager);
        mItems = new ArrayList<>();
        mPaperAdapter = new MyPagerAdapter(mItems, this);    //把图片集传进去
        mViewPager.setAdapter(mPaperAdapter);
        mViewPager.setOnTouchListener(this);
        mViewPager.addOnPageChangeListener(this);
        isAutoPlay = true;

        requestPaperId(PAPER_LIST_URL);     //发送请求获取数据
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
                break;
        }
        return true;
    }

    /**
     * 发送网络请求获取装有插画id列表的集合
     */
    private void requestPaperId(final String url){
        HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                //取出处理之后出来的Id集合
                final String responseText = response;
                final List<String> paperIdList = Utilty.handlePaperIdResponse(responseText);

                if (url.equals(PAPER_MORE_URL)) {   //如果此时传进来的url让你获取更多id
                    for (int i =0; i < paperIdList.size(); i++){
                        for (int j = 0; j < mPapers.size(); j++){
                            if (paperIdList.get(i).equals(mPapers.get(j).getId())){     //根据id来判断有没有重复的内容
                                paperIdList.remove(i);    //如果有重复的内容就删除掉
                            }
                        }
                    }
                    Log.d("ReadActivity", "发出集合1.5（加载更多）大小为：" + paperIdList.size());

                }
                Message message = new Message();
                message.what = PAPER_ID;
                message.obj = paperIdList;            //将删除之后新的集合发送出去
                handler.sendMessage(message);   //将Message对象发送出去
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PaperActivity.this, "获取id信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 根据传入的地址发送网络请求获取装有对应插画信息的对象，并发往信息处理器
     * @param url
     */
    private void initPaper(final String url) {
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
                    if (paperIds.size() > 0){
                        for (int i = 0; i < paperIds.size(); i++) {
                            String paperUrl = "http://v3.wufazhuce.com:8000/api/hp/detail/" +
                                    paperIds.get(i) + "?version=3.5.0&platform=android";
                            initPaper(paperUrl);
                        }
                    }
                    break;
                case PAPER_LIST:
                    Paper paper = (Paper) msg.obj;
                    mPapers.add(paper);
                    if (mPapers.size() == 10){
                        setAdapter();
                    }
                    break;
                case PAPER_IMAGE:
                    String imageUrl = (String) msg.obj;
                    mImageUrls.add(imageUrl);
                    if (mImageUrls.size() == 10){
                        addImageView(mImageUrls);
                    }
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
     */
    private void setAdapter(){
        mAdapter = new PaperListAdapter(GlobalContext.getContext(), R.layout.layout_card_paper, mPapers);
        mListView.setAdapter(mAdapter);
        mDialog.dismiss();
    }

    /**
     * 下拉刷新回调方法
     */
    @Override
    public void onDownPullRefresh() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(LIST_REFRESH_TIME);
                mPapers.clear();           //先将集合里面的内容清空重新收集一遍
                requestPaperId(PAPER_LIST_URL);     //重新初始化阅读列表
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
     * 上拉加载更多回调方法
     */
    @Override
    public void onLoadingMore() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(LIST_MORE_TIME);
                if (isFirstLoadingMore) {      //如果这是第一次加载更多数据
                    requestPaperId(PAPER_MORE_URL);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (isFirstLoadingMore){            //如果这是第一次加载更多数据
                    mAdapter.notifyDataSetChanged();
                    isFirstLoadingMore = false;     //不再是第一次
                }else {
                    Toast.makeText(GlobalContext.getContext(), "已无更多", Toast.LENGTH_SHORT).show();
                }
                // 控制脚布局隐藏
                mListView.hideFooterView();
            }
        }.execute(new Void[]{});
    }

    /**
     * 给轮播图添加图片
     */
    private void addImageView(List<String> imageUrls){
        if (mItems != null){    //判断此时mItems是不是空集合
            mItems.clear();     //如果不是空集合就清空重新添加
        }
        for (int i = 0; i < imageUrls.size(); i++) {
            ImageView view = new ImageView(this);       //定义一个imageView来放图片
            String imgUrl = imageUrls.get(i);           //取出每一张图片的url地址
            view.setImageResource(R.drawable.loading);  //默认图片
            imageLoader mLoader = new imageLoader(GlobalContext.getContext());    //定义图片加载器
            mLoader.loadingImage(view, imgUrl);   //加载网络图片
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);  //设置缩放方式
            mItems.add(view);       //将新建的view添加进Imageview集合里面
        }
        mPaperAdapter.notifyDataSetChanged();   //通知轮播图适配器更新数据
        //设置轮播图底部的小点
        setBottomIndicator();
    }

    /**
     * 设置轮播图小点
     */
    private void setBottomIndicator(){
        mLlBottomLiner = (LinearLayout)findViewById(R.id.live_indicator);
        //右下方小圆点
        mIvBottomImages = new ImageView[mItems.size()];
        for (int i = 0; i < mIvBottomImages.length; i++){
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
            mIvBottomImages[i] = imageView;
            //添加到父容器
            mLlBottomLiner.addView(imageView);
        }

        //让其在最大值的中间开始滑动，一定要在mBottomImages初始化之前完成
        int mid = MyPagerAdapter.MAX_SCROLL_VALUE / 2;
        mViewPager.setCurrentItem(mid);
        currentViewPageItem = mid;

        //定时发送消息
        new Thread(){
            @Override
            public void run() {
                super.run();
                while (true){
                    mHandler.sendEmptyMessage(0);
                    try {
                        Thread.sleep(VIEW_PAGER_DELAY);   //睡眠
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }.start();
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
            position %= mIvBottomImages.length;
            //总页数
            int total = mIvBottomImages.length;
            //循环赋点
            for (int i = 0; i < total; i++){
                if (i == position){
                    mIvBottomImages[i].setImageResource(R.drawable.indicator_select);
                } else {
                    mIvBottomImages[i].setImageResource(R.drawable.indicator_no_select);
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
                Toast.makeText(GlobalContext.getContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
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
