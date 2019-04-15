/**
 * Copyright (c) https://github.com/gushizone
 */

package org.mmall.service;

import com.github.pagehelper.PageInfo;
import org.mmall.common.ServerResponse;
import org.mmall.pojo.Shipping;

/**
 * @author gushizone@gmail.com
 * @createTime 2018/4/23 19:11
 * @desc 收货地址Service接口
 */
public interface IShippingService {
    public ServerResponse add(Integer userId, Shipping shipping);

    public ServerResponse<String> del(Integer userId, Integer shippingId);

    public ServerResponse<String> update(Integer userId, Shipping shipping);

    public ServerResponse<Shipping> select(Integer userId, Integer shippingId);

    public ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);
}
