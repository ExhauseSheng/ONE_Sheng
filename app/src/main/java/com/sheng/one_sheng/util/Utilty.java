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
        //判断response是否为空
        if (response != null){
            try {
                JSONObject jsonObject = new JSONObject(response);
                String resultCode = jsonObject.getString("res");
                if (resultCode.equals("0")){
                    JSONObject paperContent = jsonObject.getJSONObject("data");
                    //获取数据
                    String paperId = paperContent.getString("hpcontent_id");
                    String title = paperContent.getString("hp_title");
                    String imageUrl = paperContent.getString("hp_img_url");
                    String imageUrlOriginal = paperContent.getString("hp_img_original_url");
                    String content = paperContent.getString("hp_content");
                    String authorName = paperContent.getString("hp_author");
                    String imageAuthorName = paperContent.getString("image_authors");
                    String textAuthorName = paperContent.getString("text_authors");
                    String updateDate = paperContent.getString("last_update_date");
                    int praiseNum = paperContent.getInt("praisenum");
                    int shareNum = paperContent.getInt("sharenum");
                    int commentNum = paperContent.getInt("commentnum");
                    //将数据组装到paper对象中
                    Paper paper = new Paper();
//                  paper.setId(paperId);
                    paper.setId(R.drawable.nav_icon_another);
                    paper.setTitle(title);
                    paper.setImageUrl(imageUrl);
                    paper.setImgUrlOriginal(imageUrlOriginal);
                    paper.setContent(content);
                    paper.setAuthorInfo(authorName);
                    paper.setImgAuthor(imageAuthorName);
                    paper.setTextAuthor(textAuthorName);
                    paper.setUpdateDate(updateDate);
                    paper.setPraiseNum(praiseNum);
                    paper.setShareNum(shareNum);
                    paper.setCommentNum(commentNum);
                    return paper;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }
}
