package com.lrm.blog.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5Utils {

    /**
     * MD5加密类
     * 加密字符串
     */
    public static String code(String str) {

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] byteDiget = md.digest();
            int i;
            StringBuffer buff = new StringBuffer();
            for (int offset =0;offset<byteDiget.length;offset++){
                i=byteDiget[offset];
                if (i<0)
                    i+=256;
                if (i<16)
                    buff.append("0");
                buff.append(Integer.toHexString(i));
            }
            //32位加密
            return buff.toString();
            //16位的加密
          //  return buff.toString().substring(8,24);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void main(String[]args){
        System.out.println(code("123"));
    }
}
