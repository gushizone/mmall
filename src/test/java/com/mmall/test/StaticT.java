/**
 * Copyright (c) https://github.com/gushizone
 */

package com.mmall.test;

import org.junit.Test;
import org.mmall.common.Const;

/**
 * @author gushizone@gmail.com
 * @createTime 2018/4/22 22:17
 * @desc TODO static常量会加入常量池
 */
public class StaticT {


    @Test
    public void test1(){
        System.out.println("currentUser" == Const.CURRENT_USER);// true
    }

    @Test
    public void test2(){
        String str = "currentUser";
        System.out.println(str == Const.CURRENT_USER);// true
        System.out.println(new String("currentUser") == Const.CURRENT_USER);// false
    }

}
