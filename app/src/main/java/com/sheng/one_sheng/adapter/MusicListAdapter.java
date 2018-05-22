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
import com.sheng.one_sheng.bean.Music;
import com.sheng.one_sheng.ui.NoScrollListView;
import com.sheng.one_sheng.util.imageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/8.
 */

/**
 * 音乐列表适配器
 */
public class MusicListAdapter extends ArrayAdapter<Music> {

    private int resourceId;     //用来指定列表某子项的id
    private NoScrollListView mListView;
    private List<String> mImageUrls = new ArrayList<>();
    private boolean isFirst;//是否是第一次进入
    private imageLoader mImageLoader;
    private int mSart;
    private int mEnd;
    private List<Music> mMusicList;
    private Context mContext;

    public MusicListAdapter (Context context, int textViewResoureId, List<Music> objects){
        super(context, textViewResoureId, objects);
        resourceId = textViewResoureId;
        this.mMusicList = objects;
        this.mContext = context;

        mImageLoader = new imageLoader(mListView);
        for (int i = 0; i < this.mMusicList.size(); i++){
            mImageUrls.add(this.mMusicList.get(i).getImageUrl());
        }
        Log.d("MusicListAdapter", "图片url集合大小为：" + mImageUrls.size() + "");
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (parent != null){
            this.mListView = (NoScrollListView) parent;
        }
        mImageLoader = new imageLoader(mListView);
        Music music = getItem(position);
        View view;
        MusicViewHolder viewHolder;
        //优化ListView的运行效率
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new MusicViewHolder();
            viewHolder.mTvmusicTitle = (TextView) view.findViewById(R.id.tv_title);
            viewHolder.mIvMusicImage = (ImageView) view.findViewById(R.id.music_image);
            viewHolder.mTvMusicAuthor = (TextView) view.findViewById(R.id.tv_author);
            viewHolder.mTvUpdateDate = (TextView) view.findViewById(R.id.tv_card_update_date);
            viewHolder.mTvMusicForward = (TextView) view.findViewById(R.id.tv_music_desc);
            viewHolder.mTvLikeNum = (TextView) view.findViewById(R.id.tv_like_num);
            view.setTag(viewHolder);        //将viewHolder储存在View中
        } else {
            view = convertView;
            viewHolder = (MusicViewHolder) view.getTag();        //重新获取viewHolder
        }
        viewHolder.mTvmusicTitle.setText(music.getTitle());
        viewHolder.mTvMusicAuthor.setText(" 文 / " + music.getUserName());
        viewHolder.mTvUpdateDate.setText(music.getUpdateDate());
        viewHolder.mTvMusicForward.setText(music.getForward());
        viewHolder.mTvLikeNum.setText(music.getLikeCount() + "");

        String url = music.getImageUrl();
        viewHolder.mIvMusicImage.setImageResource(R.drawable.loading);
        viewHolder.mIvMusicImage.setTag(url);

        mImageLoader.loadingByAsyncTask(viewHolder.mIvMusicImage, url);
        return view;
    }

    class MusicViewHolder{
        TextView mTvmusicTitle;
        TextView mTvMusicAuthor;
        TextView mTvUpdateDate;
        ImageView mIvMusicImage;
        TextView mTvMusicForward;
        TextView mTvLikeNum;
    }
}
