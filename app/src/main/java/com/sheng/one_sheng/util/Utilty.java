package com.sheng.one_sheng.util;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/9.
 */

import android.util.Log;

import com.sheng.one_sheng.bean.Comment;
import com.sheng.one_sheng.bean.Movie;
import com.sheng.one_sheng.bean.Music;
import com.sheng.one_sheng.bean.Paper;
import com.sheng.one_sheng.bean.Read;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 在这里进行解析和处理服务器返回的数据
 */
public class Utilty {

    /**
     * 将返回的JSON数据解析出一个插画id列表数组
     * @param response
     * @return
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
     * 将返回的JSON数据解析成Paper实体类用于临时装载详细内容数据
     * @param response
     * @return
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
                    paper.setId(paperContent.getString("hpcontent_id"));
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
     * 将返回的JSON数据解析成Music实体类，并放置于一个集合，用于临时装载列表数据
     * @param response
     * @return
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
                    return musics;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将返回的JSON数据解析成Movie实体类，并放置于一个集合，用于临时装载列表数据
     * @param response
     * @return
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
                        movie.setSubTitle(movieList.getJSONObject(i).getString("subtitle"));
                        movie.setUpdateDate(movieList.getJSONObject(i).getString("last_update_date"));
                        movie.setUserName(movieList.getJSONObject(i).getJSONObject("author").getString("user_name"));
                        movie.setDes(movieList.getJSONObject(i).getJSONObject("author").getString("desc"));
                        movie.setFansTotal(movieList.getJSONObject(i).getJSONObject("author").getString("fans_total"));
                        movies.add(movie);
                    }
                    return movies;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将返回的JSON数据解析成Read实体类，并放置于一个集合，用于临时装载列表数据
     * @param response
     * @return
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
                    return reads;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将返回的JSON数据解析成Read实体类用于临时展示阅读详细内容
     * @param response
     * @return
     */
    public static Read handleReadDetailResponse(String response) {
        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String resultCode = jsonObject.getString("res");
                if (resultCode.equals("0")) {
                    Read read = new Read();
                    JSONObject readContent = jsonObject.getJSONObject("data");
                    read.setContentId(readContent.getString("content_id"));
                    read.setTitle(readContent.getString("hp_title"));
                    read.setUserName(readContent.getString("hp_author"));
                    read.setContent(readContent.getString("hp_content"));
                    read.setWbImgUrl(readContent.getString("wb_img_url"));
                    read.setUpdateDate(readContent.getString("last_update_date"));
                    read.setGuideWord(readContent.getString("guide_word"));
                    read.setDes(readContent.getJSONArray("author").getJSONObject(0).getString("desc"));
                    read.setFansTotal(readContent.getJSONArray("author").getJSONObject(0).getString("fans_total"));
                    read.setNextId(readContent.getString("next_id"));
                    read.setPreId(readContent.getString("previous_id"));
                    read.setPraiseNum(readContent.getInt("praisenum"));
                    read.setShareNum(readContent.getInt("sharenum"));
                    read.setCommentNum(readContent.getInt("commentnum"));
                    return read;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将返回的JSON数据解析成Music实体类用于临时展示音乐详细内容
     * @param response
     * @return
     */
    public static Music handleMusicDetailResponse(String response){
        if (response != null){
            try{
                JSONObject jsonObject = new JSONObject(response);
                String resultCode = jsonObject.getString("res");
                if (resultCode.equals("0")){
                    Music music = new Music();
                    JSONObject musicContent = jsonObject.getJSONObject("data");
                    music.setSongId(musicContent.getString("id"));
                    music.setSongTitle(musicContent.getString("title"));
                    music.setCover(musicContent.getString("cover"));
                    music.setStoryTitle(musicContent.getString("story_title"));
                    music.setSummary(musicContent.getString("story_summary"));
                    music.setStory(musicContent.getString("story"));
                    music.setLyric(musicContent.getString("lyric"));
                    music.setAlbum(musicContent.getString("album"));
                    music.setInfo(musicContent.getString("info"));
                    music.setUpdateDate(musicContent.getString("last_update_date"));
                    music.setPraiseNum(musicContent.getInt("praisenum"));
                    music.setShareNum(musicContent.getInt("sharenum"));
                    music.setCommentNum(musicContent.getInt("commentnum"));
                    music.setReadNum(musicContent.getString("read_num"));
                    music.setUserName(musicContent.getJSONObject("author").getString("user_name"));
                    music.setDes(musicContent.getJSONObject("author").getString("desc"));
                    music.setFansTotal(musicContent.getJSONObject("author").getString("fans_total"));
                    music.setNextId(musicContent.getString("next_id"));
                    music.setPreId(musicContent.getString("previous_id"));
                    return music;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将返回的JSON数据解析成Movie实体类用于临时展示影视详细内容
     * @param response
     * @return
     */
    public static Movie handleMovieDetailResponse(String response){
        if (response != null){
            try {
                JSONObject jsonObject = new JSONObject(response);
                String resultCode = jsonObject.getString("res");
                if (resultCode.equals("0")){
                    Movie movie = new Movie();
                    JSONObject movieContent = jsonObject.getJSONObject("data");
                    movie.setCount(movieContent.getInt("count"));
                    movie.setMovieId(movieContent.getJSONArray("data").getJSONObject(0).getString("id"));
                    movie.setTitle(movieContent.getJSONArray("data").getJSONObject(0).getString("title"));
                    movie.setContent(movieContent.getJSONArray("data").getJSONObject(0).getString("content"));
                    movie.setPraiseNum(movieContent.getJSONArray("data").getJSONObject(0).getInt("praisenum"));
                    movie.setUpdateDate(movieContent.getJSONArray("data").getJSONObject(0).getString("input_date"));
                    movie.setSummary(movieContent.getJSONArray("data").getJSONObject(0).getString("summary"));
                    movie.setUserName(movieContent.getJSONArray("data").getJSONObject(0).getJSONObject("user").getString("user_name"));
                    movie.setDes(movieContent.getJSONArray("data").getJSONObject(0).getJSONObject("user").getString("desc"));
                    movie.setFansTotal(movieContent.getJSONArray("data").getJSONObject(0).getJSONObject("user").getString("fans_total"));
                    return movie;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将返回的JSON数据解析成Comment实体类，并放置于一个集合，用于临时展示列表数据
     * @param response
     * @return
     */
    public static List<Comment> handleCommentResponse(String response){
        if (response != null){
            try {
                JSONObject jsonObject = new JSONObject(response);
                String resultCode = jsonObject.getString("res");
                if (resultCode.equals("0")){
                    List<Comment> comments = new ArrayList<>();
                    JSONObject commentContent = jsonObject.getJSONObject("data");
                    JSONArray commentList = commentContent.getJSONArray("data");
                    for (int i = 0; i < commentList.length(); i++){
                        Comment comment = new Comment();
                        comment.setQuote(commentList.getJSONObject(i).getString("quote"));
                        comment.setContent(commentList.getJSONObject(i).getString("content"));
                        comment.setPraiseNum(commentList.getJSONObject(i).getInt("praisenum"));
                        comment.setCreateTime(commentList.getJSONObject(i).getString("created_at"));
                        comment.setUserName(commentList.getJSONObject(i).getJSONObject("user").getString("user_name"));
                        comments.add(comment);
                    }
                    return comments;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }
}
