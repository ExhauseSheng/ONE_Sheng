package com.sheng.one_sheng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sheng.one_sheng.MyApplication;
import com.sheng.one_sheng.R;
import com.sheng.one_sheng.bean.Paper;
import com.sheng.one_sheng.ui.RefreshListView;
import com.sheng.one_sheng.util.imageLoader;

import java.util.List;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/8.
 */

/**
 * 插画列表适配器
 */
public class PaperListAdapter extends ArrayAdapter<Paper> {

    private int resourceId;     //用来指定列表某子项的id
    private RefreshListView mListView;
    private imageLoader mImageLoader;


    public PaperListAdapter (Context context, int textViewResoureId, List<Paper> objects){
        super(context, textViewResoureId, objects);
        resourceId = textViewResoureId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (parent != null){
            this.mListView = (RefreshListView) parent;
        }
        mImageLoader = new imageLoader(MyApplication.getContext(), mListView);
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
        viewHolder.mIvPaperImage.setTag(url);   //将图片的url地址设置为图片的Tag标签

        mImageLoader.loadingImage(viewHolder.mIvPaperImage, url);   //异步加载网络图片
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
