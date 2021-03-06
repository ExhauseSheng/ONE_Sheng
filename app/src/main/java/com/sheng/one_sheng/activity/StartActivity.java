package com.sheng.one_sheng.activity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.sheng.one_sheng.MyApplication;
import com.sheng.one_sheng.R;

import static com.sheng.one_sheng.Contents.VIEW_START_DELAY;

public class StartActivity extends BaseActivity {

    private ImageView mIvStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);
        initImage();
    }

    /**
     * 开场图片的设置
     */
    private void initImage(){
        mIvStart = (ImageView) findViewById(R.id.iv_start);
        mIvStart.setImageResource(R.drawable.start);
        //进行缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.4f, 1f, 1.4f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        scaleAnimation.setDuration(VIEW_START_DELAY);       //总共缩放时间为4秒钟
        //动画播放完成后保持形状
        scaleAnimation.setFillAfter(true);
        //监听事件
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                PaperActivity.actionStart(MyApplication.getContext());
                //用于呈现淡入淡出效果
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();       //结束活动
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //开始动画
        mIvStart.startAnimation(scaleAnimation);
    }
}
