/**
 * Copyright (c) https://github.com/gushizone
 */

package org.mmall.service;

import com.github.pagehelper.PageInfo;
import org.mmall.common.ServerResponse;
import org.mmall.vo.OrderVo;

import java.util.Map;

/**
 * @author gushizone@gmail.com
 * @createTime 2018/4/29 18:41
 * @desc 订单Service接口
 */
public interface IOrderService {

    ServerResponse pay(Long orderNo, Integer userId, String path);

    ServerResponse aliCallback(Map<String, String> params);

    ServerResponse queryOrderPayStatus(Integer userId, Long orderNo);

    ServerResponse createOrder(Integer userId, Integer shippingId);

    ServerResponse<String> cancel(Integer userId, Long orderNo);

    ServerResponse getOrderCartProduct(Integer userId);

    ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo);

    ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);


//    backend

    ServerResponse<PageInfo> manageList(int pageNum, int pageSize);

    ServerResponse<OrderVo> manageDetail(Long orderNo);

    ServerResponse<PageInfo> manageSearch(Long orderNo, int pageNum, int pageSize);

    ServerResponse<String> manageSendGoods(Long orderNo);

    // hour个小时以内未付款的订单，进行关闭
    void closeOrder(int hour);
}
