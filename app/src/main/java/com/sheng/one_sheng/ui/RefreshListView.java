package com.sheng.one_sheng.ui;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sheng.one_sheng.R;

import java.text.SimpleDateFormat;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/16.
 */

/**
 * 重写ListView，实现下拉刷新和上拉加载更多的功能
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener {

    private static final String TAG = "RefreshListView";
    private int firstVisibleItemPosition; // 屏幕显示在第一个的item的索引
    private int downY; // 按下时y轴的偏移量
    private int headerViewHeight; // 头布局的高度
    private View headerView; // 头布局的对象

    private final int DOWN_PULL_REFRESH = 0; // 下拉刷新状态
    private final int RELEASE_REFRESH = 1; // 松开刷新
    private final int REFRESHING = 2; // 正在刷新中
    private int currentState = DOWN_PULL_REFRESH; // 头布局的状态: 默认为下拉刷新状态

    private Animation upAnimation; // 向上旋转的动画
    private Animation downAnimation; // 向下旋转的动画

    private ImageView ivArrow; // 头布局的向上拉的箭头
    private ProgressBar mProgressBar; // 头布局的进度条
    private TextView tvState; // 头布局的状态
    private TextView tvLastUpdateTime; // 头布局的最后更新时间

    private OnRefreshListener mOnRefershListener;
    private boolean isScrollToBottom; // 是否滑动到底部
    private View footerView; // 脚布局的对象
    private int footerViewHeight; // 脚布局的高度
    private boolean isLoadingMore = false; // 是否正在加载更多中

    public RefreshListView(android.content.Context context, android.util.AttributeSet attrs){
        super(context, attrs);
        initHeaderView();
        initFooterView();
        this.setOnScrollListener(this);
    }

    /**
     * 初始化脚布局
     */
    private void initFooterView() {
        //取出脚布局
        footerView = View.inflate(getContext(), R.layout.listview_footer, null);
        footerView.measure(0, 0); //系统会帮我们测量出footerView的高度
        footerViewHeight = footerView.getMeasuredHeight();
        footerView.setPadding(0, -footerViewHeight, 0, 0);
        this.addFooterView(footerView); //向ListView的顶部添加一个view对象
    }

    /**
     * 初始化头布局
     */
    private void initHeaderView() {
        //取出头布局
        headerView = View.inflate(getContext(), R.layout.listview_header, null);
        ivArrow = (ImageView) headerView.findViewById(R.id.iv_listview_header_arrow);
        mProgressBar = (ProgressBar) headerView.findViewById(R.id.pb_listview_header);
        tvState = (TextView) headerView.findViewById(R.id.tv_listview_header_state);
        tvLastUpdateTime = (TextView) headerView.findViewById(R.id.tv_listview_header_last_update_time);

        // 设置最后刷新时间
        tvLastUpdateTime.setText("最后刷新时间: " + getLastUpdateTime());

        headerView.measure(0, 0); // 系统会帮我们测量出headerView的高度
        headerViewHeight = headerView.getMeasuredHeight();
        headerView.setPadding(0, -headerViewHeight, 0, 0);
        this.addHeaderView(headerView); // 向ListView的顶部添加一个view对象
        initAnimation();
    }

    /**
     * 获得系统的最新时间
     *
     * @return
     */
    private String getLastUpdateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //时间格式
        return sdf.format(System.currentTimeMillis());
    }

    /**
     * 初始化动画
     */
    private void initAnimation() {
        //向上旋转的动画
        upAnimation = new RotateAnimation(0f, -180f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //旋转的速率
        upAnimation.setDuration(500);
        upAnimation.setFillAfter(true); // 动画结束后, 停留在结束的位置上

        //向下旋转的动画
        downAnimation = new RotateAnimation(-180f, -360f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //旋转的速率
        downAnimation.setDuration(500);
        downAnimation.setFillAfter(true); // 动画结束后, 停留在结束的位置上
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {       //屏幕触碰监听
        switch (ev.getAction()) {               //获取事件
            case MotionEvent.ACTION_DOWN :      //如果此时按下了屏幕不动
                downY = (int) ev.getY();        //取出此时触点的Y坐标
                break;
            case MotionEvent.ACTION_MOVE :      //如果此时时按住屏幕移动
                int moveY = (int) ev.getY();    //取出移动中的触点的Y坐标
                // 移动中的y - 按下的y = 间距.
                int diff = (moveY - downY) / 2;
                // -头布局的高度 + 间距 = paddingTop
                int paddingTop = -headerViewHeight + diff;
                // 如果: -头布局的高度 > paddingTop的值 执行super.onTouchEvent(ev);
                if (firstVisibleItemPosition == 0 && -headerViewHeight < paddingTop) {
                    if (paddingTop > 0 && currentState == DOWN_PULL_REFRESH) {
                        //此时表示已经完全显示头布局，下拉完全
                        Log.i(TAG, "松开刷新");
                        currentState = RELEASE_REFRESH;     //此时状态变成松开刷新
                        refreshHeaderView();
                    } else if (paddingTop < 0 && currentState == RELEASE_REFRESH) {
                        //此时表示没有完全显示头布局，下拉不完全
                        Log.i(TAG, "下拉刷新");
                        currentState = DOWN_PULL_REFRESH;   //此时状态仍然是下拉刷新
                        refreshHeaderView();
                    }
                    // 下拉头布局的操作
                    headerView.setPadding(0, paddingTop, 0, 0);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP :    //如果此时松开屏幕
                // 判断当前的状态是松开刷新还是下拉刷新
                if (currentState == RELEASE_REFRESH) {
                    //如果是松开刷新的状态，那么松开手指的瞬间将执行刷新操作
                    Log.i(TAG, "刷新数据.");
                    // 把头布局设置为完全显示状态
                    headerView.setPadding(0, 0, 0, 0);
                    // 进入到正在刷新中状态
                    currentState = REFRESHING;
                    refreshHeaderView();

                    //判断此时刷新事件的监听器是否为空
                    if (mOnRefershListener != null) {
                        mOnRefershListener.onDownPullRefresh(); // 调用使用者的监听方法
                    }
                } else if (currentState == DOWN_PULL_REFRESH) {
                    //如果此时还处于下拉刷新的状态，那么松开手指的瞬间就应该收起头布局
                    // 隐藏头布局
                    headerView.setPadding(0, -headerViewHeight, 0, 0);
                }
                break;
            default :
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 根据currentState刷新头布局的状态
     */
    private void refreshHeaderView() {
        switch (currentState) {
            case DOWN_PULL_REFRESH : // 下拉刷新状态
                tvState.setText("下拉刷新");    //替换此时TextView的文字
                ivArrow.startAnimation(downAnimation); // 执行向下旋转
                break;
            case RELEASE_REFRESH :  // 松开刷新状态
                tvState.setText("松开刷新");    //替换此时TextView的文字
                ivArrow.startAnimation(upAnimation); // 执行向上旋转
                break;
            case REFRESHING :       // 正在刷新中状态
                ivArrow.clearAnimation();   //清空动画
                ivArrow.setVisibility(View.GONE);   //隐藏向上的箭头
                mProgressBar.setVisibility(View.VISIBLE);   //显示加载圆圈
                tvState.setText("正在刷新中...");    //替换此时TextView的文字
                break;
            default :
                break;
        }
    }

    /**
     * 当滚动状态改变时回调
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {
            // 判断当前listView是否已经到了底部
            if (isScrollToBottom && !isLoadingMore) {
                isLoadingMore = true;
                // 当前到底部
                Log.i(TAG, "加载更多数据");
                //显示出脚布局
                footerView.setPadding(0, 0, 0, 0);
                this.setSelection(this.getCount());

                if (mOnRefershListener != null) {
                    //回调监听器其中的加载更多的方法
                    mOnRefershListener.onLoadingMore();
                }
            }
        }
    }

    /**
     * 当滚动时调用
     *
     * @param firstVisibleItem
     *            当前屏幕显示在顶部的item的position
     * @param visibleItemCount
     *            当前屏幕显示了多少个条目的总数
     * @param totalItemCount
     *            ListView的总条目的总数
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        firstVisibleItemPosition = firstVisibleItem;

        //如果ListView最后一个项目的position等于总条目的总数-1
        if (getLastVisiblePosition() == (totalItemCount - 1)) {
            isScrollToBottom = true;        //说明已经到达底部了
        } else {
            isScrollToBottom = false;       //否则说明还没到达底部
        }
    }

    /**
     * 设置刷新监听事件
     *
     * @param listener
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mOnRefershListener = listener;
    }

    /**
     * 隐藏头布局
     */
    public void hideHeaderView() {
        headerView.setPadding(0, -headerViewHeight, 0, 0);
        ivArrow.setVisibility(View.VISIBLE);    //显示箭头
        mProgressBar.setVisibility(View.GONE);  //隐藏加载框
        tvState.setText("下拉刷新");    //替换文字
        tvLastUpdateTime.setText("最后刷新时间: " + getLastUpdateTime());     //显示刷新时间
        currentState = DOWN_PULL_REFRESH;   //将此时的状态变成下拉刷新的状态
    }

    /**
     * 隐藏脚布局
     */
    public void hideFooterView() {
        footerView.setPadding(0, -footerViewHeight, 0, 0);
        isLoadingMore = false;
    }
}
