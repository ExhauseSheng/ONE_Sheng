package com.sheng.one_sheng.bean;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/8.
 */

public class Read extends Base {
    public int contentId = essayDeId;
    public String essayContent;     //文章主体
    public int nextId;      //下一篇文章的id
    public int previousId;  //上一篇文章的id
}
