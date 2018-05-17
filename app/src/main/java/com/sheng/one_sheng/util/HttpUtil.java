package com.sheng.one_sheng.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/9.
 */


/**
 * 在这里和服务器进行交互
 */
public class HttpUtil {

    /**
     * 在这里向服务器发送请求
     * @param address
     * @param listener
     */
    public static void sendHttpRequest(final String address, final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
//                    connection.setConnectTimeout(8000);
//                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        response.append(line);
                    }
                    if (listener != null){
                        //回调onFinish()方法
                        Log.d("HttpUtil", response.toString());
                        listener.onFinish(response.toString());
                    }
                }catch (Exception e){
                    if (listener != null){
                        //回调onError()方法
                        listener.onError(e);
                    }
                }finally {
                    if (connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     * 发送网络请求下载图片
     * @param url
     * @return
     */
    public static Bitmap downloadBitmap(String url){

        URL fileUrl = null;
        Bitmap bitmap = null;
        HttpURLConnection conn = null;

        try {
            fileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try{
            conn = (HttpURLConnection) fileUrl.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(10000);

            //获得图像的字符流
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null){
                conn.disconnect();
            }
        }
        return bitmap;
    }
}
