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

    public ServerResponse pay(Long orderNo, Integer userId, String path);

    public ServerResponse aliCallback(Map<String, String> params);

    public ServerResponse queryOrderPayStatus(Integer userId, Long orderNo);

    public ServerResponse createOrder(Integer userId, Integer shippingId);

    public ServerResponse<String> cancel(Integer userId, Long orderNo);

    public ServerResponse getOrderCartProduct(Integer userId);

    public ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo);

    public ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);


//    backend

    public ServerResponse<PageInfo> manageList(int pageNum, int pageSize);

    public ServerResponse<OrderVo> manageDetail(Long orderNo);

    public ServerResponse<PageInfo> manageSearch(Long orderNo, int pageNum, int pageSize);

    public ServerResponse<String> manageSendGoods(Long orderNo);
}
