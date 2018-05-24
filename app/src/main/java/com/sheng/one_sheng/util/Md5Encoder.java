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
            MessageDigest digest=MessageDigest.getInstance("MD5");
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
