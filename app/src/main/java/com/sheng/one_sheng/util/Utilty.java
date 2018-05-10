package com.sheng.one_sheng.util;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/9.
 */

import com.sheng.one_sheng.R;
import com.sheng.one_sheng.bean.Paper;

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
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject paperContent = jsonObject.getJSONObject("data");
            Paper paper = new Paper();
            paper.setId(paperContent.getInt("hpcontent_id"));
//            paper.setId(R.drawable.nav_icon_another);
            paper.setTitle(paperContent.getString("hp_title"));
            paper.setImageUrl(paperContent.getString("hp_img_url"));
            paper.setImgUrlOriginal(paperContent.getString("hp_img_origin_url"));
            paper.setContent(paperContent.getString("hp_content"));
            paper.setAuthorInfo(paperContent.getString("hp_author"));
            paper.setImgAuthor(paperContent.getString("image_authors"));
            paper.setTextAuthor(paperContent.getString("text_authors"));
            paper.setUpdateDate(paperContent.getString("last_update_date"));
            paper.setPraiseNum(paperContent.getInt("praisenum"));
            paper.setShareNum(paperContent.getInt("sharenum"));
            paper.setCommentNum(paperContent.getInt("commentnum"));
            return paper;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
