/**
 * Copyright (c) https://github.com/gushizone
 */

package org.mmall.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author gushizone@gmail.com
 * @createTime 2018/4/22 18:36
 * @desc 购物车Vo
 */
public class CartVo {

    private List<CartProductVo> cartProductVoList;
    private BigDecimal cartTotalPrice; // 购物车内总价
    private Boolean allChecked; // 是否已勾选
    private String imageHost; // 图片服务器地址

    public List<CartProductVo> getCartProductVoList() {
        return cartProductVoList;
    }

    public void setCartProductVoList(List<CartProductVo> cartProductVoList) {
        this.cartProductVoList = cartProductVoList;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public Boolean getAllChecked() {
        return allChecked;
    }

    public void setAllChecked(Boolean allChecked) {
        this.allChecked = allChecked;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
