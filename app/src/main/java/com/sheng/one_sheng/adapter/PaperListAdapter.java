package com.sheng.one_sheng.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sheng.one_sheng.R;
import com.sheng.one_sheng.bean.Paper;
import com.sheng.one_sheng.ui.NoScrollListView;
import com.sheng.one_sheng.util.imageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/8.
 */

/**
 * 插画列表适配器
 */
public class PaperListAdapter extends ArrayAdapter<Paper> {

    private int resourceId;     //用来指定列表某子项的id
    private NoScrollListView mListView;
    private List<String> mImageUrls = new ArrayList<>();
    private boolean isFirst;//是否是第一次进入
    private imageLoader mImageLoader;
    private int mSart;
    private int mEnd;
    private List<Paper> mPaperList;
    private Context mContext;

    public PaperListAdapter (Context context, int textViewResoureId, List<Paper> objects){
        super(context, textViewResoureId, objects);
        resourceId = textViewResoureId;
        this.mPaperList = objects;
        this.mContext = context;

        for (int i = 0; i < this.mPaperList.size(); i++){
            mImageUrls.add(this.mPaperList.get(i).getImageUrl());
        }
        Log.d("PaperListAdapter", "图片url集合大小为：" + mImageUrls.size() + "");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (parent != null){
            this.mListView = (NoScrollListView) parent;
        }
        mImageLoader = new imageLoader(mListView);
        Paper paper = getItem(position);    //获取当前项的Paper实例
        View view;
        PaperViewHolder viewHolder;
        //优化ListView的运行效率
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new PaperViewHolder();
            viewHolder.mTvPaperTitle = (TextView) view.findViewById(R.id.paper_title);
            viewHolder.mIvPaperImage = (ImageView) view.findViewById(R.id.paper_image);
            viewHolder.mTvPaperAuthor = (TextView) view.findViewById(R.id.paper_author);
            viewHolder.mTvPaperEssayContent = (TextView) view.findViewById(R.id.paper_essay_content);
            viewHolder.mTvPaperEssayAuthor = (TextView) view.findViewById(R.id.paper_essay_author);
            viewHolder.mTvPraiseNum = (TextView) view.findViewById(R.id.tv_praise_num);
            viewHolder.mTvShareNum = (TextView) view.findViewById(R.id.tv_share_num);
            viewHolder.mTvCommentNum = (TextView) view.findViewById(R.id.tv_comment_num);
            view.setTag(viewHolder);        //将viewHolder储存在View中
        } else {
            view = convertView;
            viewHolder = (PaperViewHolder) view.getTag();        //重新获取viewHolder
        }
        viewHolder.mTvPaperTitle.setText(paper.getTitle());
        viewHolder.mTvPaperAuthor.setText(paper.getTextAuthor());
        viewHolder.mTvPaperEssayContent.setText(paper.getContent());
        viewHolder.mTvPaperEssayAuthor.setText(paper.getAuthorInfo());
        viewHolder.mTvPraiseNum.setText(paper.getPraiseNum() + "");
        viewHolder.mTvShareNum.setText(paper.getShareNum() + "");
        viewHolder.mTvCommentNum.setText(paper.getCommentNum() + "");

        String url = paper.getImageUrl();
        viewHolder.mIvPaperImage.setImageResource(R.drawable.loading);
        viewHolder.mIvPaperImage.setTag(url);

        mImageLoader.loadingByAsyncTask(viewHolder.mIvPaperImage, url);
        return view;
    }

    class PaperViewHolder {
        TextView mTvPaperTitle;
        ImageView mIvPaperImage;
        TextView mTvPaperAuthor;
        TextView mTvPaperEssayContent;
        TextView mTvPaperEssayAuthor;
        TextView mTvPraiseNum;
        TextView mTvShareNum;
        TextView mTvCommentNum;
    }
}
