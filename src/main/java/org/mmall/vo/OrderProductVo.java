/**
 * Copyright (c) https://github.com/gushizone
 */

package org.mmall.vo;

import org.mmall.pojo.OrderItem;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author gushizone@gmail.com
 * @createTime 2018/5/20 21:44
 * @desc 订单产品Vo（订单预览）
 */
public class OrderProductVo {
    private List<OrderItemVo> orderItemVoList;
    private BigDecimal productTotalPrice;
    private String imageHost;

    public List<OrderItemVo> getOrderItemVoList() {
        return orderItemVoList;
    }

    public void setOrderItemVoList(List<OrderItemVo> orderItemVoList) {
        this.orderItemVoList = orderItemVoList;
    }

    public BigDecimal getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(BigDecimal productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
