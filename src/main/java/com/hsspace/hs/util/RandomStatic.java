package com.hsspace.hs.util;

import java.util.Random;

/**
 * RandomStatic类
 * Git to： http://hs.mccspace.com:3000/Qing_ning/web-mvc/
 *
 * @TIME 2020/8/18 12:18
 * @AUTHOR 韩硕~
 */

public class RandomStatic {

    public static RandomStatic loginStatic = new RandomStatic();

    private Random random = new Random();
    private int num = 10000;

    public int next(){
        return num+=(random.nextInt(100)+1);
    }

}
