package com.sheng.one_sheng.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/18.
 */

/**
 * SharedPreferences的一个工具类，
 * 调用setParam就能保存String, Integer, Boolean, Float, Long类型的参数
 * 调用getParam就能获取到保存在手机里面的数据
 * @author baidu
 *
 */
public class SPUtil {

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     * @param context
     * @param key
     * @param object
     */
    public static void setParam(Context context , String key, Object object){

        String type = object.getClass().getSimpleName();
        SharedPreferences.Editor editor = PreferenceManager.
                getDefaultSharedPreferences(context).edit();

        if("String".equals(type)){
            editor.putString(key, (String)object);
        }
        else if("Integer".equals(type)){
            editor.putInt(key, (Integer)object);
        }
        else if("Boolean".equals(type)){
            editor.putBoolean(key, (Boolean)object);
        }
        else if("Float".equals(type)){
            editor.putFloat(key, (Float)object);
        }
        else if("Long".equals(type)){
            editor.putLong(key, (Long)object);
        }

        editor.apply();
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(Context context , String key, Object defaultObject){
        //获取传入数据的类型
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        //根据数据类型取出相应的值
        if("String".equals(type)){
            return prefs.getString(key, (String)defaultObject);
        }
        else if("Integer".equals(type)){
            return prefs.getInt(key, (Integer)defaultObject);
        }
        else if("Boolean".equals(type)){
            return prefs.getBoolean(key, (Boolean)defaultObject);
        }
        else if("Float".equals(type)){
            return prefs.getFloat(key, (Float)defaultObject);
        }
        else if("Long".equals(type)){
            return prefs.getLong(key, (Long)defaultObject);
        }
        return null;
    }
}
