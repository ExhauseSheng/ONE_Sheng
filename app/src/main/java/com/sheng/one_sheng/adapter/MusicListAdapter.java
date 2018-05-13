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
import com.sheng.one_sheng.bean.Music;
import com.sheng.one_sheng.bean.Paper;

import java.util.List;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/8.
 */

public class MusicListAdapter extends ArrayAdapter<Music> {

    private int resourceId;     //用来指定列表某子项的id

    public MusicListAdapter (Context context, int textViewResoureId, List<Music> objects){
        super(context, textViewResoureId, objects);
        resourceId = textViewResoureId;
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
            viewHolder.musicForward = (TextView) view.findViewById(R.id.tv_music_desc);
            viewHolder.likeNum = (TextView) view.findViewById(R.id.tv_like_num);
            viewHolder.shareNum = (TextView) view.findViewById(R.id.tv_share_num);
            viewHolder.commentNum = (TextView) view.findViewById(R.id.tv_comment_num);
            view.setTag(viewHolder);        //将viewHolder储存在View中
        } else {
            view = convertView;
            viewHolder = (MusicViewHolder) view.getTag();        //重新获取viewHolder
        }
        viewHolder.musicTitle.setText(music.getTitle());
        viewHolder.musicImage.setImageResource(R.drawable.nav_icon);
        viewHolder.musicAuthor.setText(" 文 / " + music.getUserName());
        viewHolder.musicForward.setText(music.getForward());
        viewHolder.likeNum.setText(music.getLikeCount() + "");
        viewHolder.shareNum.setText(music.getShareNum() + "");
        viewHolder.commentNum.setText(music.getCommentNum() + "");
        return view;
    }

    class MusicViewHolder{
        TextView musicTitle;
        TextView musicAuthor;
        ImageView musicImage;
        TextView musicForward;
        TextView likeNum;
        TextView shareNum;
        TextView commentNum;
    }
}
