package com.sheng.one_sheng.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/9.
 */

public class data {

    public int id;
    public int movieId;
    public String title;
    public String content;
    public String inputDate;
    public String summary;

    public class user {

        public String userName;
        public String desc;
        public int fansTotal;

    }
}
