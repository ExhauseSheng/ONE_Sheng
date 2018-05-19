package com.sheng.one_sheng.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sheng.one_sheng.R;
import com.sheng.one_sheng.bean.Music;
import com.sheng.one_sheng.bean.Paper;
import com.sheng.one_sheng.ui.MyListView;
import com.sheng.one_sheng.util.HttpUtil;
import com.sheng.one_sheng.util.imageLoader;

import java.util.ArrayList;
import java.util.List;

import static com.sheng.one_sheng.util.HttpUtil.downloadBitmap;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/8.
 */

/**
 * 音乐列表适配器
 */
public class MusicListAdapter extends ArrayAdapter<Music> implements AbsListView.OnScrollListener {

    private int resourceId;     //用来指定列表某子项的id
    private MyListView mListView;
    private List<String> imageUrls = new ArrayList<>();
    private boolean isFirst;//是否是第一次进入
    private imageLoader mImageLoader;
    private int mSart;
    private int mEnd;
    private List<Music> musicList;
    private Context mContext;

    public MusicListAdapter (Context context, int textViewResoureId, List<Music> objects, MyListView listView){
        super(context, textViewResoureId, objects);
        resourceId = textViewResoureId;
        this.musicList = objects;
        this.mContext = context;
        this.mListView = listView;

        mImageLoader = new imageLoader(mListView);
        for (int i = 0; i < this.musicList.size(); i++){
            imageUrls.add(this.musicList.get(i).getImageUrl());
        }
        Log.d("MusicListAdapter", "图片url集合大小为：" + imageUrls.size() + "");
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Music music = getItem(position);
        View view;
        MusicViewHolder viewHolder;
        //优化ListView的运行效率
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new MusicViewHolder();
            viewHolder.musicTitle = (TextView) view.findViewById(R.id.tv_title);
            viewHolder.musicImage = (ImageView) view.findViewById(R.id.music_image);
            viewHolder.musicAuthor = (TextView) view.findViewById(R.id.tv_author);
            viewHolder.updateDate = (TextView) view.findViewById(R.id.tv_card_update_date);
            viewHolder.musicForward = (TextView) view.findViewById(R.id.tv_music_desc);
            viewHolder.likeNum = (TextView) view.findViewById(R.id.tv_like_num);
            view.setTag(viewHolder);        //将viewHolder储存在View中
        } else {
            view = convertView;
            viewHolder = (MusicViewHolder) view.getTag();        //重新获取viewHolder
        }
        viewHolder.musicTitle.setText(music.getTitle());
        viewHolder.musicAuthor.setText(" 文 / " + music.getUserName());
        viewHolder.updateDate.setText(music.getUpdateDate());
        viewHolder.musicForward.setText(music.getForward());
        viewHolder.likeNum.setText(music.getLikeCount() + "");

        String url = music.getImageUrl();
        viewHolder.musicImage.setImageResource(R.drawable.loading);
        viewHolder.musicImage.setTag(url);

        mImageLoader.loadingByAsyncTask(viewHolder.musicImage, url);
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

    class MusicViewHolder{
        TextView musicTitle;
        TextView musicAuthor;
        TextView updateDate;
        ImageView musicImage;
        TextView musicForward;
        TextView likeNum;
    }
}
