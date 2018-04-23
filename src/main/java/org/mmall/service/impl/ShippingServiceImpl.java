/**
 * Copyright (c) https://github.com/gushizone
 */

package org.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.mmall.common.ServerResponse;
import org.mmall.dao.ShippingMapper;
import org.mmall.pojo.Shipping;
import org.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author gushizone@gmail.com
 * @createTime 2018/4/23 19:12
 * @desc 收货地址Service实现
 */
@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    /** 增加 */
    @Override
    public ServerResponse add(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int rowCount = shippingMapper.insert(shipping);
        if(rowCount > 0) {
            Map result = Maps.newHashMap();
            result.put("shippingId", shipping.getId());// TODO 配合useGeneratedKeys="true" keyProperty="id" 获取主键
            return ServerResponse.createBySuccess("新建地址成功", result);// 约定向前端返回新增的id
        }
        return ServerResponse.createByErrorMessage("新建地址失败");
    }

    /** 删除 */
    @Override
    public ServerResponse<String> del(Integer userId, Integer shippingId) {
        int rowCount = shippingMapper.deleteByShippingIdUserId(shippingId, userId);// 避免横向越权，关联用户id
        if(rowCount >0){
            return ServerResponse.createBySuccessMessage("删除地址成功");
        }
        return ServerResponse.createByErrorMessage("删除地址失败");
    }

    /** 修改 */
    @Override
    public ServerResponse<String> update(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int rowCount = shippingMapper.updateByShipping(shipping);// 避免横向越权，关联用户id
        if(rowCount >0){
            return ServerResponse.createBySuccessMessage("修改地址成功");
        }
        return ServerResponse.createByErrorMessage("修改地址失败");
    }

    /** 查询单个 */
    @Override
    public ServerResponse<Shipping> select(Integer userId, Integer shippingId) {
        Shipping shipping = shippingMapper.selectByShippingIdUserId(shippingId, userId);// 避免横向越权，关联用户id
        if(shipping == null){
            return ServerResponse.createByErrorMessage("无法查询该地址");
        }
        return ServerResponse.createBySuccess("查询地址成功", shipping);
    }

    /** 查询个人所有收获地址（分页） */
    @Override
    public ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
