package com.sheng.one_sheng.activity;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/9.
 */

/**
 * 一个活动管理器
 */
public class ActivityCollector
{
    public static List<Activity> mActivities = new ArrayList<>();

    public static void addActivity(Activity activity)
    {
        mActivities.add(activity);   //添加活动
    }

    public static void removeActivity(Activity activity)
    {
        mActivities.remove(activity);    //删除活动
    }

    public static void finishAll()      //销毁全部活动
    {
        for (Activity activity : mActivities)
        {
            if (!activity.isFinishing())    //如果活动没有被销毁的就立即销毁
            {
                activity.finish();
            }
        }
    }
}
