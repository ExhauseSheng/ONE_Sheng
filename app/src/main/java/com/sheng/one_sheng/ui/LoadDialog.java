package com.sheng.one_sheng.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.sheng.one_sheng.R;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/18.
 */

/**
 * 自定义Dialog：加载时弹出
 */
public class LoadDialog extends Dialog {

    private Context mContext;
    private static LoadDialog mDialog;
    private ImageView mIvProgress;


    private LoadDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;

    }
    //显示dialog的方法
    public static LoadDialog showDialog(Context context){
        mDialog = new LoadDialog(context, R.style.MyDialog);//dialog样式
        mDialog.setContentView(R.layout.loading_dialog);//dialog的布局文件
        mDialog.setCanceledOnTouchOutside(false);//点击外部不允许关闭dialog
        mDialog.setCancelable(false);//按返回键不能够取消dialog
        return mDialog;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {    //屏幕的焦点状态改变
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus && mDialog != null){
            mIvProgress = (ImageView) mDialog.findViewById(R.id.ivProgress);  //加载的图片
            //动画设置，调用AnimationUtils的loadAnimation方法，传进去一个context对象和动画文件
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.progress_anim);
            mIvProgress.startAnimation(animation);   //开启动画
        }
    }
}
