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
import com.sheng.one_sheng.bean.Movie;

import java.util.List;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/8.
 */

public class MovieListAdapter extends ArrayAdapter<Movie> {

    private int resourceId;     //用来指定列表某子项的id

    public MovieListAdapter(Context context, int textViewResoureId, List<Movie> objects){
        super(context, textViewResoureId, objects);
        resourceId = textViewResoureId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);
        View view;
        MovieViewHolder viewHolder;
        //优化ListView的运行效率
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new MovieViewHolder();
            viewHolder.movieTitle = (TextView) view.findViewById(R.id.tv_title);
            viewHolder.movieImage = (ImageView) view.findViewById(R.id.movie_image);
            viewHolder.movieAuthor = (TextView) view.findViewById(R.id.tv_author);
            viewHolder.movieForward = (TextView) view.findViewById(R.id.tv_movie_desc);
            view.setTag(viewHolder);        //将viewHolder储存在View中
        } else {
            view = convertView;
            viewHolder = (MovieViewHolder) view.getTag();
        }
        viewHolder.movieTitle.setText(movie.getTitle());
        viewHolder.movieImage.setImageResource(R.drawable.nav_icon_another);
        viewHolder.movieAuthor.setText(" 文 / " + movie.getUserName());
        viewHolder.movieForward.setText(movie.getForward());
        return view;
    }

    class MovieViewHolder{
        TextView movieTitle;
        TextView movieAuthor;
        ImageView movieImage;
        TextView movieForward;
    }
}
