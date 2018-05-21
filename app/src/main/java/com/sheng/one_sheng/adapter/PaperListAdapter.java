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
import com.sheng.one_sheng.ui.MyListView;
import com.sheng.one_sheng.util.imageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/8.
 */

/**
 * 插画列表适配器
 */
public class PaperListAdapter extends ArrayAdapter<Paper> implements AbsListView.OnScrollListener {

    private int resourceId;     //用来指定列表某子项的id
    private MyListView mListView;
    private List<String> imageUrls = new ArrayList<>();
    private boolean isFirst;//是否是第一次进入
    private imageLoader mImageLoader;
    private int mSart;
    private int mEnd;
    private List<Paper> paperList;
    private Context mContext;

    public PaperListAdapter (Context context, int textViewResoureId, List<Paper> objects, MyListView listView){
        super(context, textViewResoureId, objects);
        resourceId = textViewResoureId;
        this.paperList = objects;
        this.mContext = context;
        this.mListView = listView;

        mImageLoader = new imageLoader(mListView);
        for (int i = 0; i < this.paperList.size(); i++){
            imageUrls.add(this.paperList.get(i).getImageUrl());
        }
        Log.d("PaperListAdapter", "图片url集合大小为：" + imageUrls.size() + "");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Paper paper = getItem(position);    //获取当前项的Paper实例
        View view;
        PaperViewHolder viewHolder;
        //优化ListView的运行效率
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new PaperViewHolder();
            viewHolder.paperTitle = (TextView) view.findViewById(R.id.paper_title);
            viewHolder.paperImage = (ImageView) view.findViewById(R.id.paper_image);
            viewHolder.paperAuthor = (TextView) view.findViewById(R.id.paper_author);
            viewHolder.paperEssayContent = (TextView) view.findViewById(R.id.paper_essay_content);
            viewHolder.paperEssayAuthor = (TextView) view.findViewById(R.id.paper_essay_author);
            viewHolder.praiseNum = (TextView) view.findViewById(R.id.tv_praise_num);
            viewHolder.shareNum = (TextView) view.findViewById(R.id.tv_share_num);
            viewHolder.commentNum = (TextView) view.findViewById(R.id.tv_comment_num);
            view.setTag(viewHolder);        //将viewHolder储存在View中
        } else {
            view = convertView;
            viewHolder = (PaperViewHolder) view.getTag();        //重新获取viewHolder
        }
        viewHolder.paperTitle.setText(paper.getTitle());
        viewHolder.paperAuthor.setText(paper.getTextAuthor());
        viewHolder.paperEssayContent.setText(paper.getContent());
        viewHolder.paperEssayAuthor.setText(paper.getAuthorInfo());
        viewHolder.praiseNum.setText(paper.getPraiseNum() + "");
        viewHolder.shareNum.setText(paper.getShareNum() + "");
        viewHolder.commentNum.setText(paper.getCommentNum() + "");

        String url = paper.getImageUrl();
        viewHolder.paperImage.setImageResource(R.drawable.loading);
        viewHolder.paperImage.setTag(url);

        mImageLoader.loadingByAsyncTask(viewHolder.paperImage, url);
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

    class PaperViewHolder {
        TextView paperTitle;
        ImageView paperImage;
        TextView paperAuthor;
        TextView paperEssayContent;
        TextView paperEssayAuthor;
        TextView praiseNum;
        TextView shareNum;
        TextView commentNum;
    }
}
