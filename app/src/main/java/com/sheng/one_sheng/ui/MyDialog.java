package com.sheng.one_sheng.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sheng.one_sheng.R;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/18.
 */

public class MyDialog extends Dialog {

    private Context context;
    private static MyDialog dialog;
    private ImageView ivProgress;


    public MyDialog(Context context) {
        super(context);
        this.context = context;
    }

    public MyDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;

    }
    //显示dialog的方法
    public static MyDialog showDialog(Context context){
        dialog = new MyDialog(context, R.style.MyDialog);//dialog样式
        dialog.setContentView(R.layout.loading_dialog);//dialog的布局文件
        dialog.setCanceledOnTouchOutside(false);//点击外部不允许关闭dialog
        dialog.setCancelable(false);//按返回键不能够取消dialog
        return dialog;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {    //屏幕的焦点状态改变
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus && dialog != null){
            ivProgress = (ImageView) dialog.findViewById(R.id.ivProgress);  //加载的图片
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.dialog_progress_anim);   //动画设置
            ivProgress.startAnimation(animation);   //开启动画
        }
    }
}
