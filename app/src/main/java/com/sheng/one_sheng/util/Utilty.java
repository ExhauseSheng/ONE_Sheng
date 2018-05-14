package com.sheng.one_sheng.util;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/9.
 */

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.sheng.one_sheng.R;
import com.sheng.one_sheng.bean.Movie;
import com.sheng.one_sheng.bean.Music;
import com.sheng.one_sheng.bean.Paper;
import com.sheng.one_sheng.bean.Read;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 在这里进行解析和处理服务器返回的数据
 */
public class Utilty {

    /**
     * 将返回的JSON数据解析出一个插画id列表数组
     */
    public static List<String> handlePaperIdResponse(String response){
        List<String> paperId = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray idList = jsonObject.getJSONArray("data");

            for (int i = 0; i < idList.length(); i++){
                paperId.add(idList.getString(i));       //将解析出来的插画id赋给一个字符串数组
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return paperId;
    }

    /**
     * 将返回的JSON数据解析成Paper实体类
     */
    public static Paper handlePaperDetailResponse(String response) {
        //判断response是否为空
        if (response != null){
            try {
                JSONObject jsonObject = new JSONObject(response);
                String resultCode = jsonObject.getString("res");
                if (resultCode.equals("0")){
                    JSONObject paperContent = jsonObject.getJSONObject("data");
                    //将数据组装到paper对象中
                    Paper paper = new Paper();
//                  paper.setId(paperContent.getString("hpcontent_id"));
                    paper.setId(R.drawable.nav_icon_another);
                    paper.setTitle(paperContent.getString("hp_title"));
                    paper.setImageUrl(paperContent.getString("hp_img_url"));
                    paper.setImgUrlOriginal(paperContent.getString("hp_img_original_url"));
                    paper.setContent(paperContent.getString("hp_content"));
                    paper.setAuthorInfo(paperContent.getString("hp_author"));
                    paper.setImgAuthor(paperContent.getString("image_authors"));
                    paper.setTextAuthor(paperContent.getString("text_authors"));
                    paper.setUpdateDate(paperContent.getString("last_update_date"));
                    paper.setPraiseNum(paperContent.getInt("praisenum"));
                    paper.setShareNum(paperContent.getInt("sharenum"));
                    paper.setCommentNum(paperContent.getInt("commentnum"));
                    return paper;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将返回的JSON数据解析成Music实体类
     */
    public static List<Music> handleMusicListResponse(String response) {
        if (response != null){
            try {
                JSONObject jsonObject = new JSONObject(response);
                String resultCode = jsonObject.getString("res");
                if (resultCode.equals("0")){
                    List<Music> musics = new ArrayList<>();
                    JSONArray musicList = jsonObject.getJSONArray("data");
                    for (int i = 0; i < musicList.length(); i++) {
                        Music music = new Music();
                        music.setId(musicList.getJSONObject(i).getString("id"));
                        music.setItemId(musicList.getJSONObject(i).getString("item_id"));
                        music.setForward(musicList.getJSONObject(i).getString("forward"));
                        music.setTitle(musicList.getJSONObject(i).getString("title"));
                        music.setImageUrl(musicList.getJSONObject(i).getString("img_url"));
                        music.setLikeCount(musicList.getJSONObject(i).getInt("like_count"));
                        music.setUpdateDate(musicList.getJSONObject(i).getString("last_update_date"));
                        music.setUserName(musicList.getJSONObject(i).getJSONObject("author").getString("user_name"));
                        music.setDes(musicList.getJSONObject(i).getJSONObject("author").getString("desc"));
                        music.setFansTotal(musicList.getJSONObject(i).getJSONObject("author").getString("fans_total"));
                        musics.add(music);
                    }
                    Log.d("MusicUtily", "集合1的大小为：" + musics.size() + "");
                    return musics;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将返回的JSON数据解析成Movie实体类
     */
    public static List<Movie> handleMovieListResponse(String response){
        if (response != null){
            try {
                JSONObject jsonObject = new JSONObject(response);
                String resultCode = jsonObject.getString("res");
                if (resultCode.equals("0")){
                    List<Movie> movies = new ArrayList<>();
                    JSONArray movieList = jsonObject.getJSONArray("data");
                    for (int i = 0; i < movieList.length(); i++) {
                        Movie movie = new Movie();
                        movie.setId(movieList.getJSONObject(i).getString("id"));
                        movie.setItemId(movieList.getJSONObject(i).getString("item_id"));
                        movie.setForward(movieList.getJSONObject(i).getString("forward"));
                        movie.setTitle(movieList.getJSONObject(i).getString("title"));
                        movie.setImageUrl(movieList.getJSONObject(i).getString("img_url"));
                        movie.setLikeCount(movieList.getJSONObject(i).getInt("like_count"));
                        movie.setUpdateDate(movieList.getJSONObject(i).getString("last_update_date"));
                        movie.setUserName(movieList.getJSONObject(i).getJSONObject("author").getString("user_name"));
                        movie.setDes(movieList.getJSONObject(i).getJSONObject("author").getString("desc"));
                        movie.setFansTotal(movieList.getJSONObject(i).getJSONObject("author").getString("fans_total"));
                        movies.add(movie);
                    }
                    Log.d("MovieUtily", "集合1的大小为：" + movies.size() + "");
                    return movies;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将返回的JSON数据解析成Read实体类
     */
    public static List<Read> handleReadListResponse(String response){
        if (response != null){
            try {
                JSONObject jsonObject = new JSONObject(response);
                String resultCode = jsonObject.getString("res");
                if (resultCode.equals("0")){
                    List<Read> reads = new ArrayList<>();
                    JSONArray readList = jsonObject.getJSONArray("data");
                    for (int i = 0; i < readList.length(); i++) {
                        Read read = new Read();
                        read.setId(readList.getJSONObject(i).getString("id"));
                        read.setItemId(readList.getJSONObject(i).getString("item_id"));
                        read.setForward(readList.getJSONObject(i).getString("forward"));
                        read.setTitle(readList.getJSONObject(i).getString("title"));
                        read.setImageUrl(readList.getJSONObject(i).getString("img_url"));
                        read.setLikeCount(readList.getJSONObject(i).getInt("like_count"));
                        read.setUpdateDate(readList.getJSONObject(i).getString("last_update_date"));
                        read.setUserName(readList.getJSONObject(i).getJSONObject("author").getString("user_name"));
                        read.setDes(readList.getJSONObject(i).getJSONObject("author").getString("desc"));
                        read.setFansTotal(readList.getJSONObject(i).getJSONObject("author").getString("fans_total"));
                        reads.add(read);
                    }
                    Log.d("ReadUtily", "集合1的大小为：" + reads.size() + "");
                    return reads;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 图片工具类:生成Drawable图片
     * @param url
     * @return
     */
    public static Drawable getPicture(String url){
        InputStream is = null;
        Drawable d = null;
        try {
            is = (InputStream) new URL(url).getContent();   //查看网页源代码
            d = Drawable.createFromStream(is, "src name");  //生成Drawable
            return d;
        }catch (Exception e){
            return null;
        }
    }
}
