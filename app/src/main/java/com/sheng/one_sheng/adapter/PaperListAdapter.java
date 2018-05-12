package com.sheng.one_sheng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sheng.one_sheng.R;
import com.sheng.one_sheng.bean.Paper;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/8.
 */

public class PaperListAdapter extends ArrayAdapter<Paper> {

    private int resourceId;     //用来指定列表某子项的id

    public PaperListAdapter (Context context, int textViewResoureId, List<Paper> objects){
        super(context, textViewResoureId, objects);
        resourceId = textViewResoureId;
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
            viewHolder.likeNum = (TextView) view.findViewById(R.id.tv_like_num);
            viewHolder.shareNum = (TextView) view.findViewById(R.id.tv_share_num);
            viewHolder.commentNum = (TextView) view.findViewById(R.id.tv_comment_num);
            view.setTag(viewHolder);        //将viewHolder储存在View中
        } else {
            view = convertView;
            viewHolder = (PaperViewHolder) view.getTag();        //重新获取viewHolder
        }
        viewHolder.paperTitle.setText(paper.getTitle());
        viewHolder.paperImage.setImageResource(paper.getId());
        viewHolder.paperAuthor.setText(paper.getTextAuthor());
        viewHolder.paperEssayContent.setText(paper.getContent());
        viewHolder.paperEssayAuthor.setText(paper.getAuthorInfo());
        viewHolder.likeNum.setText(paper.getPraiseNum() + "");
        viewHolder.shareNum.setText(paper.getShareNum() + "");
        viewHolder.commentNum.setText(paper.getCommentNum() + "");
        return view;
    }

    class PaperViewHolder {
        TextView paperTitle;
        ImageView paperImage;
        TextView paperAuthor;
        TextView paperEssayContent;
        TextView paperEssayAuthor;
        TextView likeNum;
        TextView shareNum;
        TextView commentNum;
    }
}
