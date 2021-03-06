package com.sheng.one_sheng.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import com.sheng.one_sheng.R;
import com.sheng.one_sheng.ui.RefreshListView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/19.
 */

/**
 * 异步图片加载工具
 * 借鉴于网络:
 * @author cuiran
 *
 * 补充LruCache的理解：
 * 对有限数量的值保持强引用的高速缓存。每次访问一个值时，它被移动到队列的头部。
 * 当一个值被添加到一个完整的缓存中时，该队列末尾的值被删除，并且可能成为垃圾收集的合格条件。
 */
public class imageLoader {
    private LruCache<String, Bitmap> mCaches;   //创建对象
    private ListView mListView;
    private Context mContext;
    private static Handler mHandler;
    private static ExecutorService mThreadPool;     //一个线程池
    private static Map<ImageView,Future<?>> mTaskTags=new LinkedHashMap<>();

    public imageLoader(Context context){
        mContext = context;
        int maxMemory = (int) Runtime.getRuntime().maxMemory(); //获取最大可用内存
        int cacheSize = maxMemory / 8;  //设置缓存数据的最大占用内存为最大值的1/8
        mCaches = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();    //每次存入缓存的时候调用，返回Bitmap的大小
            }
        };
        if(mHandler == null){
            mHandler = new Handler();
        }
        if(mThreadPool == null){
            mThreadPool = Executors.newFixedThreadPool(3);  //创建一个有3个线程的线程池
        }
    }

    public imageLoader(Context context, ListView listView){    //构造方法
        mContext = context;
        this.mListView = listView;
        int maxMemory = (int) Runtime.getRuntime().maxMemory(); //获取最大可用内存
        int cacheSize = maxMemory / 8;  //设置缓存数据的最大占用内存为最大值的1/8
        mCaches = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();    //每次存入缓存的时候调用，返回Bitmap的大小
            }
        };
        if(mHandler == null){
            mHandler = new Handler();
        }
        if(mThreadPool == null){
            mThreadPool = Executors.newFixedThreadPool(3);
        }
    }

    /**
     * 取出缓存数据
     * @param url
     * @return
     */
    private Bitmap getLruCaches(String url) {
        return mCaches.get(url);
        //通过url从缓存中相应的bitmap
    }

    /**
     * 添加缓存数据，添加前推断数据是否存在
     * @param url
     * @param bitmap
     */
    private void setLruCaches(String url, Bitmap bitmap) {
        if (getLruCaches(url) == null){
            //假设缓存中不存在url相应的Bitmap，则把bitmap增加进去mCaches
            mCaches.put(url, bitmap);
        }
    }

    /**
     * 给相应的imageView加载网络图片
     * @param img
     * @param url
     */
    public void loadingImage(ImageView img, String url){
//        从缓存中取出图片
        Bitmap bitmap = getLruCaches(url);
        if (bitmap != null){
            //假设在缓存中有这个图片，则直接给imageView加载图片
            img.setImageBitmap(bitmap);
            return;
        }
//        从硬盘中取出图片
        bitmap = loadBitmapFromLocal(url);
        //假设在硬盘中有这个图片，则直接给imageView加载图片
        if (bitmap != null) {
            img.setImageBitmap(bitmap);
            return;
        }
//        从网络中获取图片
        img.setImageResource(R.drawable.loading);   //获取到图片之前先加载默认图片
        loadBitmapFromNet(img, url);
    }

    private void loadBitmapFromNet(ImageView img, String url){
        //开启线程去网络获取 使用线程池管理
        //判断是否有线程为img加载数据
        //Future是一个接口，提供给了我们方法来检测当前的任务是否已经结束，还可以等待任务结束并且拿到一个结果
        Future<?> future = mTaskTags.get(img);
        //问号说明没有返回值，那么就返回一个null好了
        if (future != null && !future.isCancelled() && !future.isDone()){
            //线程正在执行
            future.cancel(true);
            future = null;
        }
        //线程池执行一个任务
        future = mThreadPool.submit(new ImageLoadTask(img, url));
        mTaskTags.put(img, future);
    }

    private class ImageLoadTask implements Runnable{
        private String mUrl;
        private ImageView img;

        public ImageLoadTask(ImageView img, String mUrl){
            this.mUrl = mUrl;
            this.img = img;
        }

        @Override
        public void run() {
            //从网络下载图片
            final Bitmap bitmap = HttpUtil.downloadBitmap(mUrl);
            write2Local(mUrl, bitmap);  //存储到本地
            setLruCaches(mUrl, bitmap);  //存储到内存
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mListView != null){
                        //用于ListView，取出ListView中Tag为mUrl的ImageView，可以防止图片出现错乱
                        ImageView img = (ImageView) mListView.findViewWithTag(mUrl);
                        if (img != null && bitmap != null){
                            img.setImageBitmap(bitmap);
                        }
                    } else {
                        //不是的话直接调用loadingImage方法加载图片
                        loadingImage(img, mUrl);
                    }
                }
            });
        }
    }

    /**
     * 从本地加载bitmap
     * @param url
     * @return
     */
    private Bitmap loadBitmapFromLocal(String url){
        //去硬盘中找文件，将文件转换为bitmap
        String name;
        try {
            name = Md5Encoder.encode(url);
            File file = new File(getCacheDir(), name);
            if (file.exists()){
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                //存储到内存
                setLruCaches(url, bitmap);
                return bitmap;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将图片写进本地
     * @param url
     * @param bitmap
     */
    private void write2Local(String url, Bitmap bitmap){
        String name;
        FileOutputStream fos = null;
        try {
            name = Md5Encoder.encode(url);
            File file = new File(getCacheDir(), name);
            fos = new FileOutputStream(file);

            //将图像写进流中
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            if (fos != null){
                try {
                    fos.close();
                    fos = null;
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private String getCacheDir(){
        String state = Environment.getExternalStorageState();
        File dir = null;
        if (Environment.MEDIA_MOUNTED.equals(state)){
            //有sd卡
            dir = new File(Environment.getExternalStorageDirectory(), "/Android/data/" +
                    mContext.getPackageName() + "/icon");
        } else {
            //没有sd卡
            dir = new File(mContext.getCacheDir(), "/icon");
        }
        if (!dir.exists()){
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }
}
