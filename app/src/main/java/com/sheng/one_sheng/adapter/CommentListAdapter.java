package com.sheng.one_sheng.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sheng.one_sheng.R;
import com.sheng.one_sheng.bean.Comment;

import java.util.List;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/16.
 */

/**
 * 评论列表的适配器
 */
public class CommentListAdapter extends ArrayAdapter<Comment> {

    private int resourceId;     //用来指定列表某子项的id

    public CommentListAdapter(Context context, int textViewResoureId, List<Comment> objects){
        super(context, textViewResoureId, objects);
        resourceId = textViewResoureId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Comment comment = getItem(position);
        View view;
        //优化ListView的运行效率
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        } else {
            view = convertView;
        }
        TextView mTvUserName = (TextView) view.findViewById(R.id.tv_comment_user_name);
        TextView mTvCreateTime = (TextView) view.findViewById(R.id.tv_comment_time);
        TextView mTvTouserContent = (TextView) view.findViewById(R.id.tv_comment_touser_content);
        TextView mTvUserContent = (TextView) view.findViewById(R.id.tv_comment_user_content);
        TextView mTvPraiseNum = (TextView) view.findViewById(R.id.tv_comment_praise_num);

        mTvUserName.setText(comment.getUserName());
        mTvCreateTime.setText(comment.getCreateTime());
        mTvTouserContent.setText(comment.getQuote());
        mTvUserContent.setText(comment.getContent());
        mTvPraiseNum.setText(comment.getPraiseNum() + "");
        return view;
    }
}
