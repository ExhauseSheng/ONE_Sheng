package com.sheng.one_sheng.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sheng.one_sheng.R;
import com.sheng.one_sheng.bean.Read;

import java.util.List;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/8.
 */

public class ReadListAdapter extends ArrayAdapter<Read> {
    
    private int resourceId;     //用来指定列表某子项的id

    public ReadListAdapter (Context context, int textViewResoureId, List<Read> objects){
        super(context, textViewResoureId, objects);
        resourceId = textViewResoureId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Read read = getItem(position);    //获取当前项的Paper实例
        View view;
        ReadListAdapter.ReadViewHolder viewHolder;
        //优化ListView的运行效率
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ReadViewHolder();
            viewHolder.readTitle = (TextView) view.findViewById(R.id.tv_title);
            viewHolder.storyImage = (ImageView) view.findViewById(R.id.iv_story_image);
            viewHolder.readAuthor = (TextView) view.findViewById(R.id.tv_author);
            viewHolder.storyDesc = (TextView) view.findViewById(R.id.tv_story_desc);
            viewHolder.likeNum = (TextView) view.findViewById(R.id.tv_like_num);
            viewHolder.shareNum = (TextView) view.findViewById(R.id.tv_share_num);
            viewHolder.commentNum = (TextView) view.findViewById(R.id.tv_comment_num);
            view.setTag(viewHolder);        //将viewHolder储存在View中
        } else {
            view = convertView;
            viewHolder = (ReadListAdapter.ReadViewHolder) view.getTag();        //重新获取viewHolder
        }
        viewHolder.readTitle.setText(read.getTitle());
        viewHolder.storyImage.setImageResource(read.getId());
        viewHolder.readAuthor.setText(read.getUserName());
        viewHolder.storyDesc.setText(read.getForward());
        viewHolder.likeNum.setText(read.getLikeCount() + "");
        viewHolder.shareNum.setText(read.getShareNum() + "");
        viewHolder.commentNum.setText(read.getCommentNum() + "");
        return view;
    }

    class ReadViewHolder {
        ImageView storyImage;
        TextView readTitle;
        TextView readAuthor;
        TextView storyDesc;
        TextView likeNum;
        TextView shareNum;
        TextView commentNum;
    }
}
