package com.sheng.one_sheng.util;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import com.sheng.one_sheng.R;
import com.sheng.one_sheng.ui.MyListView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/19.
 */

public class imageLoader {
    private LruCache<String, Bitmap> mCaches;   //创建对象
    private ImageView imageView;
    private MyListView listView;
    private Set<ImageAsyncTask> mTask;

    @SuppressLint("NewApi")
    public imageLoader(MyListView listView){
        this.listView = listView;
        mTask = new HashSet<ImageAsyncTask>();
        int maxMemory = (int) Runtime.getRuntime().maxMemory(); //获取最大可用内存
        int cacheSize = maxMemory / 8;  //设置缓存数据的最大占用内存为最大值的1/8
        mCaches = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();    //每次存入缓存的时候调用，返回Bitmap的大小
            }
        };
    }

    @SuppressLint("NewApi")
    public Bitmap getLruCaches(String url) {
        return mCaches.get(url);
        //通过url从缓存中相应的bitmap
    }

    @SuppressLint("NewApi")
    /**
     * 添加缓存数据，添加前推断数据是否存在
     */
    public void setLruCaches(String url, Bitmap bitmap) {
        if (getLruCaches(url) == null){
            //假设缓存中不存在url相应的Bitmap，则把bitmap增加进去mCaches
            mCaches.put(url, bitmap);
        }
    }

    public void loadingByAsyncTask(ImageView img, String url){
        imageView = img;
        //从缓存中取出图片
        Bitmap bitmap = getLruCaches(url);
        if (bitmap == null){
            //假设在缓存中没有这个图片，则从网络上下载
            imageView.setImageResource(R.drawable.loading);
            new ImageAsyncTask(url).execute(url);
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }

    private class ImageAsyncTask extends AsyncTask<String, Void, Bitmap>{
        private ImageView imageView;
        private String mUrl;

        public ImageAsyncTask(String mUrl){
            this.mUrl = mUrl;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = HttpUtil.downloadBitmap(params[0]); //从网络获取图片
            if (bitmap != null){
                setLruCaches(params[0], bitmap);    //放进缓存数据中
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            ImageView img = (ImageView) listView.findViewWithTag(mUrl);
            if (img != null && bitmap != null){
                img.setImageBitmap(bitmap);
            }
            mTask.remove(this);
        }
    }

    public void setImageView(int start, int end, List<String> imageUrls){
        for (int i = start; i < end; i++){
            String url = imageUrls.get(i);
            Bitmap bitmap = getLruCaches(url);
            if (bitmap == null){
                //假设在缓存中没有此图片，就去网络下载
                ImageAsyncTask task = new ImageAsyncTask(url);
                task.execute(url);
                mTask.add(task);
            } else {
                //缓存中有此图片，则直接显示出来
                ImageView img = (ImageView) listView.findViewWithTag(url);
                img.setImageBitmap(bitmap);
            }
        }
    }

    public void stopAllTask(){
        if (mTask.size() > 0){
            for (ImageAsyncTask task : mTask){
                task.cancel(false);
            }
        }
    }
}
