package com.sheng.one_sheng.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sheng.one_sheng.R;
import com.sheng.one_sheng.bean.Read;

import java.util.List;

import static android.R.attr.key;
import static com.sheng.one_sheng.util.HttpUtil.downloadBitmap;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/8.
 */

public class ReadListAdapter extends ArrayAdapter<Read> {

    private int resourceId;     //用来指定列表某子项的id
    private LruCache<String, BitmapDrawable> mMemoryCache;
    private ListView mListView;

    public ReadListAdapter (Context context, int textViewResoureId, List<Read> objects){
        super(context, textViewResoureId, objects);
        resourceId = textViewResoureId;
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, BitmapDrawable>(cacheSize) {
            @Override
            protected int sizeOf(String key, BitmapDrawable drawable) {
                return drawable.getBitmap().getByteCount();
            }
        };
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mListView == null){
            mListView = (ListView) parent;      //parent就是一个ListView实例
        }
        View view;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        } else {
            view = convertView;
        }
        Read read = getItem(position);    //获取当前项的Paper实例
        //优化ListView的运行效率
        TextView readTitle = (TextView) view.findViewById(R.id.tv_title);
        ImageView storyImage = (ImageView) view.findViewById(R.id.iv_story_image);
        TextView readAuthor = (TextView) view.findViewById(R.id.tv_author);
        TextView storyDesc = (TextView) view.findViewById(R.id.tv_story_desc);
        TextView likeNum = (TextView) view.findViewById(R.id.tv_like_num);
        TextView shareNum = (TextView) view.findViewById(R.id.tv_share_num);
        TextView commentNum = (TextView) view.findViewById(R.id.tv_comment_num);
        readTitle.setText(read.getTitle());
        readAuthor.setText(" 文 / " + read.getUserName());
        storyDesc.setText(read.getForward());
        likeNum.setText(read.getLikeCount() + "");
        shareNum.setText(read.getShareNum() + "");
        commentNum.setText(read.getCommentNum() + "");

        String url = read.getImageUrl();
        storyImage.setImageResource(R.drawable.loading);
        storyImage.setTag(url);
        BitmapDrawable drawable = getBitmapFromMemoryCache(url);
        if (drawable != null) {
            storyImage.setImageDrawable(drawable);
        } else {
            BitmapWorkerTask task = new BitmapWorkerTask();
            task.execute(url);
        }
        return view;
    }

    /**
     * 将一张图片存储到LruCache中
     * @param key
     *            LruCache的键，这里传入图片的URL地址。
     * @param drawable
     *            LruCache的值，这里传入从网络上下载的BitmapDrawable对象。
     */
    public void addBitmapToMemoryCache(String key, BitmapDrawable drawable) {
        if (getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, drawable);
        }
    }

    /**
     * 从LruCache中获取一张图片，如果不存在就返回null。
     *
     * @param key
     *            LruCache的键，这里传入图片的URL地址。
     * @return 对应传入键的BitmapDrawable对象，或者null。
     */
    public BitmapDrawable getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }

    class BitmapWorkerTask extends AsyncTask<String, Void, BitmapDrawable> {

        String imageUrl;

        @Override
        protected BitmapDrawable doInBackground(String... params) {
            imageUrl = params[0];
            // 在后台开始下载图片
            Bitmap bitmap = downloadBitmap(imageUrl);
            BitmapDrawable drawable = new BitmapDrawable(getContext().getResources(), bitmap);
            addBitmapToMemoryCache(imageUrl, drawable);
            return drawable;
        }

        @Override
        protected void onPostExecute(BitmapDrawable drawable) {
            ImageView imageView = (ImageView) mListView.findViewWithTag(imageUrl);
            if (imageView != null && drawable != null) {
                imageView.setImageDrawable(drawable);
            }
        }
    }
}
