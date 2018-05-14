/**
 * Copyright (c) https://github.com/gushizone
 */

package org.mmall.service;

import org.mmall.common.ServerResponse;

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
}
