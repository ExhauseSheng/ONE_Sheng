package com.sheng.one_sheng.util;

/**
 * Created by 一个傻傻的小男孩 on 2018/5/25.
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 借鉴于网络：
 * @author cuiran
 */
public class Md5Encoder {

    public static String encode(String password){
        try{
            //MessageDigest 类为应用程序提供信息摘要算法的功能，如 MD5 或 SHA 算法
            //信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
            MessageDigest digest=MessageDigest.getInstance("MD5");
            //调用digest() 方法完成哈希计算，执行之后摘要被重置
            byte[] result=digest.digest(password.getBytes());
            StringBuilder sb=new StringBuilder();
            for(int i=0;i<result.length;i++){
                int number=result[i]&0xff;
                String str=Integer.toHexString(number);
                if(str.length()==1){
                    sb.append("0");
                    sb.append(str);
                }else {
                    sb.append(str);
                }
            }
            return sb.toString();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            return "";
        }
    }
}
