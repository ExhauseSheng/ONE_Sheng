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

import com.sheng.one_sheng.GlobalContext;
import com.sheng.one_sheng.R;
import com.sheng.one_sheng.bean.Read;
import com.sheng.one_sheng.ui.RefreshListView;
import com.sheng.one_sheng.util.imageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/8.
 */

/**
 * 阅读列表适配器
 */
public class ReadListAdapter extends ArrayAdapter<Read> {

    private int resourceId;     //用来指定列表某子项的id
    private RefreshListView mListView;
    private imageLoader mImageLoader;

    public ReadListAdapter (Context context, int textViewResoureId, List<Read> objects){
        super(context, textViewResoureId, objects);
        resourceId = textViewResoureId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (parent != null){
            this.mListView = (RefreshListView) parent;
        }
        mImageLoader = new imageLoader(GlobalContext.getContext(), mListView);
        Read read = getItem(position);    //获取当前项的Paper实例
        View view;
        ReadViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ReadViewHolder();
            viewHolder.mTvReadTitle = (TextView) view.findViewById(R.id.tv_title);
            viewHolder.mIvStoryImage = (ImageView) view.findViewById(R.id.iv_story_image);
            viewHolder.mTvReadAuthor = (TextView) view.findViewById(R.id.tv_author);
            viewHolder.mTvUpdateDate = (TextView) view.findViewById(R.id.tv_card_update_date);
            viewHolder.mTvStoryDesc = (TextView) view.findViewById(R.id.tv_story_desc);
            viewHolder.mTvLikeNum = (TextView) view.findViewById(R.id.tv_like_num);
            view.setTag(viewHolder);        //将viewHolder储存在View中
        } else {
            view = convertView;
            viewHolder = (ReadViewHolder) view.getTag();        //重新获取viewHolder
        }
        //优化ListView的运行效率
        viewHolder.mTvReadTitle.setText(read.getTitle());
        viewHolder.mTvReadAuthor.setText(" 文 / " + read.getUserName());
        viewHolder.mTvUpdateDate.setText(read.getUpdateDate());
        viewHolder.mTvStoryDesc.setText(read.getForward());
        viewHolder.mTvLikeNum.setText(read.getLikeCount() + "");

        String url = read.getImageUrl();
        viewHolder.mIvStoryImage.setImageResource(R.drawable.loading);
        viewHolder.mIvStoryImage.setTag(url);

        mImageLoader.loadingImage(viewHolder.mIvStoryImage, url);
        return view;
    }

    class ReadViewHolder {
        TextView mTvReadTitle;
        ImageView mIvStoryImage;
        TextView mTvReadAuthor;
        TextView mTvUpdateDate;
        TextView mTvStoryDesc;
        TextView mTvLikeNum;
    }
}
