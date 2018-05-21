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
import com.sheng.one_sheng.bean.Read;
import com.sheng.one_sheng.ui.MyListView;
import com.sheng.one_sheng.util.imageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/8.
 */

/**
 * 阅读列表适配器
 */
public class ReadListAdapter extends ArrayAdapter<Read> implements AbsListView.OnScrollListener {

    private int resourceId;     //用来指定列表某子项的id
    private MyListView mListView;
    private List<String> imageUrls = new ArrayList<>();
    private boolean isFirst;//是否是第一次进入
    private imageLoader mImageLoader;
    private int mSart;
    private int mEnd;
    private List<Read> readList;
    private Context mContext;

    public ReadListAdapter (Context context, int textViewResoureId, List<Read> objects, MyListView listView){
        super(context, textViewResoureId, objects);
        resourceId = textViewResoureId;
        this.readList = objects;
        this.mContext = context;
        this.mListView = listView;
        mImageLoader = new imageLoader(mListView);

        for (int i = 0; i < this.readList.size(); i++){
            imageUrls.add(this.readList.get(i).getImageUrl());
        }
        Log.d("ReadListAdapter", "图片url集合大小为：" + imageUrls.size() + "");
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Read read = getItem(position);    //获取当前项的Paper实例
        View view;
        ReadViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ReadViewHolder();
            viewHolder.readTitle = (TextView) view.findViewById(R.id.tv_title);
            viewHolder.storyImage = (ImageView) view.findViewById(R.id.iv_story_image);
            viewHolder.readAuthor = (TextView) view.findViewById(R.id.tv_author);
            viewHolder.updateDate = (TextView) view.findViewById(R.id.tv_card_update_date);
            viewHolder.storyDesc = (TextView) view.findViewById(R.id.tv_story_desc);
            viewHolder.likeNum = (TextView) view.findViewById(R.id.tv_like_num);
            view.setTag(viewHolder);        //将viewHolder储存在View中
        } else {
            view = convertView;
            viewHolder = (ReadViewHolder) view.getTag();        //重新获取viewHolder
        }
        //优化ListView的运行效率
        viewHolder.readTitle.setText(read.getTitle());
        viewHolder.readAuthor.setText(" 文 / " + read.getUserName());
        viewHolder.updateDate.setText(read.getUpdateDate());
        viewHolder.storyDesc.setText(read.getForward());
        viewHolder.likeNum.setText(read.getLikeCount() + "");

        String url = read.getImageUrl();
        viewHolder.storyImage.setImageResource(R.drawable.loading);
        viewHolder.storyImage.setTag(url);

        mImageLoader.loadingByAsyncTask(viewHolder.storyImage, url);
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

    class ReadViewHolder {
        TextView readTitle;
        ImageView storyImage;
        TextView readAuthor;
        TextView updateDate;
        TextView storyDesc;
        TextView likeNum;
    }
}
