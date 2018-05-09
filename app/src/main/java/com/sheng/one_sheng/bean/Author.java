package com.sheng.one_sheng.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/9.
 */

public class Author {

    @SerializedName("user_name")
    public String userName;     //作者名称

    public String des;          //作家简介

    @SerializedName("fans_total")
    public int fansTotal;       //粉丝数量
}
