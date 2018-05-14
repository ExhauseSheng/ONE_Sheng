package com.sheng.one_sheng.util;

import java.io.BufferedReader;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/9.
 */

public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);
}
