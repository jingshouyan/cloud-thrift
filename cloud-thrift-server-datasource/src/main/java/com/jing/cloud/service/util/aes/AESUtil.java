package com.jing.cloud.service.util.aes;

import com.google.common.io.BaseEncoding;
import lombok.SneakyThrows;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * Created by 29017 on 2017/8/24.
 */
public class AESUtil {

    /**
     * 加密
     *
     * @param content 需要加密的内容
     * @param password  加密密码
     * @return 加密后字符串
     */
    @SneakyThrows
    public static String encrypt(String content, String password) {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128, new SecureRandom(password.getBytes()));
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES");// 创建密码器
        byte[] byteContent = content.getBytes("utf-8");
        cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
        byte[] result = cipher.doFinal(byteContent);// 加密
        return base64Encode(result);
    }

    /**解密
     * @param content  待解密内容
     * @param password 解密密钥
     * @return 解密后字符串
     */
    @SneakyThrows
    public static String decrypt(String content, String password) {
        byte[] buf = base64Decode(content);
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128, new SecureRandom(password.getBytes()));
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES");// 创建密码器
        cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
        byte[] result = cipher.doFinal(buf);//解密
        return new String(result,"utf-8");
    }



    public static String base64Encode(byte[] buf){
        return BaseEncoding.base64().encode(buf);
    }

    public static byte[] base64Decode(String content){
        return BaseEncoding.base64().decode(content);
    }

    public static void main(String[] args) {
        String a = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < 10; i++) {
            a=a+a;
        }
        System.out.println(a+"----a-----"+a.length());
        String pwd = ""+null;
        System.out.println(pwd);
        String d = encrypt(a,pwd);
        System.out.println(d+"----d-----"+d.length());
        String e = decrypt(d,pwd);
        System.out.println(e+"----e-----"+e.length());
    }
}
