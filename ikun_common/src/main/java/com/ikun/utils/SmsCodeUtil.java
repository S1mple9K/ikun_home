package com.ikun.utils;

import java.util.Random;

/**
 * 验证码生成工具类
 * @author 9K
 * @create: 2023-06-15 21:03
 */

public class SmsCodeUtil {
    /**
     * 生成四位数
     */
    public static Integer generateFour(){
        Random random = new Random();
        //[1000-10000)
        return random.nextInt(9000)+1000;
    }

    /**
     * 生成六位数
     */
    public static Integer generateSix(){
        Random random = new Random();
        //[1000-10000)
        return random.nextInt(900000)+100000;
    }

    /**
     * 生成八位数
     */
    public static Integer generateEight(){
        Random random = new Random();
        //[1000-10000)
        return random.nextInt(90000000)+10000000;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 50; i++) {
            System.out.println(generateSix());
        }
    }
}
