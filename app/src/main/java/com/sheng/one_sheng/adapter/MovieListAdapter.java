package com.sheng.one_sheng.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sheng.one_sheng.R;
import com.sheng.one_sheng.bean.Movie;
import com.sheng.one_sheng.ui.RefreshListView;
import com.sheng.one_sheng.util.imageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/8.
 */

/**
 * 影视列表的适配器
 */
public class MovieListAdapter extends ArrayAdapter<Movie> {

    private int resourceId;     //用来指定列表某子项的id
    private RefreshListView mListView;
    private List<String> mImageUrls = new ArrayList<>();
    private boolean isFirst;//是否是第一次进入
    private imageLoader mImageLoader;
    private int mSart;
    private int mEnd;
    private List<Movie> mMovieList;
    private Context mContext;

    public MovieListAdapter(Context context, int textViewResoureId, List<Movie> objects){
        super(context, textViewResoureId, objects);
        resourceId = textViewResoureId;
        this.mMovieList = objects;
        this.mContext = context;

        for (int i = 0; i < this.mMovieList.size(); i++){
            mImageUrls.add(this.mMovieList.get(i).getImageUrl());
        }
        Log.d("MovieListtAdapter", "图片url集合大小为：" + mImageUrls.size() + "");
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (parent != null){
            this.mListView = (RefreshListView) parent;
        }
        mImageLoader = new imageLoader(mListView);
        Movie movie = getItem(position);
        View view;
        MovieViewHolder viewHolder;
        //优化ListView的运行效率
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new MovieViewHolder();
            viewHolder.mTvMovieTitle = (TextView) view.findViewById(R.id.tv_title);
            viewHolder.mIvMovieImage = (ImageView) view.findViewById(R.id.movie_image);
            viewHolder.mTvMovieAuthor = (TextView) view.findViewById(R.id.tv_author);
            viewHolder.mTvMovieName = (TextView) view.findViewById(R.id.movie_title);
            viewHolder.mTvUpdateDate = (TextView) view.findViewById(R.id.tv_card_update_date);
            viewHolder.mTvMovieForward = (TextView) view.findViewById(R.id.tv_movie_desc);
            viewHolder.mTvLikeNum = (TextView) view.findViewById(R.id.tv_like_num);
            view.setTag(viewHolder);        //将viewHolder储存在View中
        } else {
            view = convertView;
            viewHolder = (MovieViewHolder) view.getTag();
        }
        viewHolder.mTvMovieTitle.setText(movie.getTitle());
        viewHolder.mTvMovieAuthor.setText(" 文 / " + movie.getUserName());
        viewHolder.mTvMovieName.setText(movie.getSubTitle());
        viewHolder.mTvUpdateDate.setText(movie.getUpdateDate());
        viewHolder.mTvMovieForward.setText(movie.getForward());
        viewHolder.mTvLikeNum.setText(movie.getLikeCount() + "");

        String url = movie.getImageUrl();
        viewHolder.mIvMovieImage.setImageResource(R.drawable.loading);
        viewHolder.mIvMovieImage.setTag(url);

        mImageLoader.loadingByAsyncTask(viewHolder.mIvMovieImage, url);
        return view;
    }

    class MovieViewHolder{
        TextView mTvMovieTitle;
        TextView mTvMovieAuthor;
        TextView mTvMovieName;
        TextView mTvUpdateDate;
        ImageView mIvMovieImage;
        TextView mTvMovieForward;
        TextView mTvLikeNum;
    }
}
