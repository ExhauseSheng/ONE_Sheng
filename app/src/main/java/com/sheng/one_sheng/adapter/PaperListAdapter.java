package com.sheng.one_sheng.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sheng.one_sheng.R;
import com.sheng.one_sheng.bean.Paper;

import org.w3c.dom.Text;

import java.util.List;

import static com.sheng.one_sheng.util.HttpUtil.downloadBitmap;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/8.
 */

public class PaperListAdapter extends ArrayAdapter<Paper> {

    private int resourceId;     //用来指定列表某子项的id
    private LruCache<String, BitmapDrawable> mMemoryCache;
    private ListView mListView;

    public PaperListAdapter (Context context, int textViewResoureId, List<Paper> objects){
        super(context, textViewResoureId, objects);
        resourceId = textViewResoureId;
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, BitmapDrawable>(cacheSize) {
            @Override
            protected int sizeOf(String key, BitmapDrawable drawable) {
                return drawable.getBitmap().getByteCount();
            }
        };
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mListView == null){
            mListView = (ListView) parent;      //parent就是一个ListView实例
        }
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
        BitmapDrawable drawable = getBitmapFromMemoryCache(url);
        if (drawable != null) {
            viewHolder.paperImage.setImageDrawable(drawable);
        } else {
            BitmapWorkerTask task = new BitmapWorkerTask();
            task.execute(url);
        }
        return view;
    }

    /**
     * 将一张图片存储到LruCache中
     * @param key
     *            LruCache的键，这里传入图片的URL地址。
     * @param drawable
     *            LruCache的值，这里传入从网络上下载的BitmapDrawable对象。
     */
    public void addBitmapToMemoryCache(String key, BitmapDrawable drawable) {
        if (getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, drawable);
        }
    }

    /**
     * 从LruCache中获取一张图片，如果不存在就返回null。
     *
     * @param key
     *            LruCache的键，这里传入图片的URL地址。
     * @return 对应传入键的BitmapDrawable对象，或者null。
     */
    public BitmapDrawable getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }

    class BitmapWorkerTask extends AsyncTask<String, Void, BitmapDrawable> {

        String imageUrl;

        @Override
        protected BitmapDrawable doInBackground(String... params) {
            imageUrl = params[0];
            // 在后台开始下载图片
            Bitmap bitmap = downloadBitmap(imageUrl);
            BitmapDrawable drawable = new BitmapDrawable(getContext().getResources(), bitmap);
            addBitmapToMemoryCache(imageUrl, drawable);
            return drawable;
        }

        @Override
        protected void onPostExecute(BitmapDrawable drawable) {
            ImageView imageView = (ImageView) mListView.findViewWithTag(imageUrl);
            if (imageView != null && drawable != null) {
                imageView.setImageDrawable(drawable);
            }
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
