/**
 * Copyright (c) https://github.com/gushizone
 */

package org.mmall.util;

import java.math.BigDecimal;

/**
 * @author gushizone@gmail.com
 * @createTime 2018/4/22 19:37
 * @desc BigDecimal商业计算工具类
 * 商业计算，只有使用BigDecimal的String构造才不会丢失精度
 */
public class BigDecimalUtil {

    private BigDecimalUtil(){}

    /** 加 */
    public static BigDecimal add(double v1, double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2);
    }

    /** 减 */
    public static BigDecimal sub(double v1, double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2);
    }

    /** 乘 */
    public static BigDecimal mul(double v1, double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2);
    }


    /** 除 */
    public static BigDecimal div(double v1, double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP); // 四舍五入，保留两位小数
    }

}
