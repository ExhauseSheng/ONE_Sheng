package com.sheng.one_sheng.util;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import static com.sheng.one_sheng.util.HttpUtil.downloadBitmap;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/15.
 */

/**
 * 加载网络图片的工具
 */
public class ImageLoadAsyncTask extends AsyncTask<String, Void, Bitmap> {

    private ImageCallBack imageCallBack;

    public ImageLoadAsyncTask(ImageCallBack imageCallBack){
        this.imageCallBack = imageCallBack;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        if (bitmap != null){
            //设置图片，回调回去
            imageCallBack.callBitmap(bitmap);
        }
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String path = params[0];
        Bitmap bitmap = downloadBitmap(path);
        return bitmap;
    }
}
