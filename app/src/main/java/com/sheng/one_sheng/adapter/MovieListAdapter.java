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
import com.sheng.one_sheng.bean.Movie;
import com.sheng.one_sheng.util.HttpUtil;

import java.util.List;

import static com.sheng.one_sheng.util.HttpUtil.downloadBitmap;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/8.
 */

public class MovieListAdapter extends ArrayAdapter<Movie> {

    private int resourceId;     //用来指定列表某子项的id
    private LruCache<String, BitmapDrawable> mMemoryCache;
    private ListView mListView;

    public MovieListAdapter(Context context, int textViewResoureId, List<Movie> objects){
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
        Movie movie = getItem(position);
        View view;
        MovieViewHolder viewHolder;
        //优化ListView的运行效率
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new MovieViewHolder();
            viewHolder.movieTitle = (TextView) view.findViewById(R.id.tv_title);
            viewHolder.movieImage = (ImageView) view.findViewById(R.id.movie_image);
            viewHolder.movieAuthor = (TextView) view.findViewById(R.id.tv_author);
            viewHolder.movieName = (TextView) view.findViewById(R.id.movie_title);
            viewHolder.updateDate = (TextView) view.findViewById(R.id.tv_card_update_date);
            viewHolder.movieForward = (TextView) view.findViewById(R.id.tv_movie_desc);
            viewHolder.likeNum = (TextView) view.findViewById(R.id.tv_like_num);
            view.setTag(viewHolder);        //将viewHolder储存在View中
        } else {
            view = convertView;
            viewHolder = (MovieViewHolder) view.getTag();
        }
        viewHolder.movieTitle.setText(movie.getTitle());
        viewHolder.movieAuthor.setText(" 文 / " + movie.getUserName());
        viewHolder.movieName.setText(movie.getSubTitle());
        viewHolder.updateDate.setText(movie.getUpdateDate());
        viewHolder.movieForward.setText(movie.getForward());
        viewHolder.likeNum.setText(movie.getLikeCount() + "");

        String url = movie.getImageUrl();
        viewHolder.movieImage.setImageResource(R.drawable.loading);
        viewHolder.movieImage.setTag(url);
        BitmapDrawable drawable = getBitmapFromMemoryCache(url);
        if (drawable != null) {
            viewHolder.movieImage.setImageDrawable(drawable);
        } else {
            BitmapWorkerTask task = new MovieListAdapter.BitmapWorkerTask();
            task.execute(url);
        }
        return view;
    }

    class MovieViewHolder{
        TextView movieTitle;
        TextView movieAuthor;
        TextView movieName;
        TextView updateDate;
        ImageView movieImage;
        TextView movieForward;
        TextView likeNum;
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
