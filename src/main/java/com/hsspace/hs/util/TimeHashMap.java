package com.hsspace.hs.util;

import java.util.HashMap;

/**
 * TimeHashMap类
 * Git to： http://hs.mccspace.com:3000/Qing_ning/HSPasswordManagerServer/
 *
 * @TIME 2020/7/23 12:43
 * @AUTHOR 韩硕~
 */

public class TimeHashMap<T,K> extends HashMap<T,K> {

    private int time;

    public TimeHashMap(int time){
        this.time = time;
    }



}
