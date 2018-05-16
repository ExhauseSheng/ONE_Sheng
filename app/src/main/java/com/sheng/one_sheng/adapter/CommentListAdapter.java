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
        TextView userName = (TextView) view.findViewById(R.id.tv_comment_user_name);
        TextView createTime = (TextView) view.findViewById(R.id.tv_comment_time);
//         TextView touserName = (TextView) view.findViewById(R.id.tv_comment_touser_name);
        TextView touserContent = (TextView) view.findViewById(R.id.tv_comment_touser_content);
        TextView userContent = (TextView) view.findViewById(R.id.tv_comment_user_content);
        TextView praiseNum = (TextView) view.findViewById(R.id.tv_comment_praise_num);

        userName.setText(comment.getUserName());
        createTime.setText(comment.getCreateTime());
//        touserName.setText(comment.getTouserName());
        touserContent.setText(comment.getQuote());
        userContent.setText(comment.getContent());
        praiseNum.setText(comment.getPraiseNum() + "");
        return view;
    }
}
