package com.sheng.one_sheng.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sheng.one_sheng.R;
import com.sheng.one_sheng.bean.Movie;
import com.sheng.one_sheng.ui.MyListView;
import com.sheng.one_sheng.util.imageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/8.
 */

/**
 * 影视列表的适配器
 */
public class MovieListAdapter extends ArrayAdapter<Movie> implements AbsListView.OnScrollListener {

    private int resourceId;     //用来指定列表某子项的id
    private MyListView mListView;
    private List<String> imageUrls = new ArrayList<>();
    private boolean isFirst;//是否是第一次进入
    private imageLoader mImageLoader;
    private int mSart;
    private int mEnd;
    private List<Movie> movieList;
    private Context mContext;

    public MovieListAdapter(Context context, int textViewResoureId, List<Movie> objects, MyListView listView){
        super(context, textViewResoureId, objects);
        resourceId = textViewResoureId;
        this.movieList = objects;
        this.mContext = context;
        this.mListView = listView;

        mImageLoader = new imageLoader(mListView);
        for (int i = 0; i < this.movieList.size(); i++){
            imageUrls.add(this.movieList.get(i).getImageUrl());
        }
        Log.d("MovieListtAdapter", "图片url集合大小为：" + imageUrls.size() + "");
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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

        mImageLoader.loadingByAsyncTask(viewHolder.movieImage, url);
        return view;
    }

    /**
     * ListView在滑动的过程调用
     * @param view
     * @param firstVisibleItem
     * @param visibleItemCount
     * @param totalItemCount
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mSart = firstVisibleItem;   //可见第一个item
        mEnd = firstVisibleItem + visibleItemCount;     //可见的最后一个item
        if (isFirst && visibleItemCount > 0){
            //第一次载入的时候数据处理
            mImageLoader.setImageView(mSart, mEnd, imageUrls);
            isFirst = false;
        }
    }

    /**
     * ListView在流动状态变化时调用
     * @param view
     * @param scrollState
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE){
            //流动停止，此时载入可见项数据
            mImageLoader.setImageView(mSart, mEnd, imageUrls);
        } else {
            //停止载入数据
            mImageLoader.stopAllTask();
        }
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
}
