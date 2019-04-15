package org.mmall.dao;

import org.apache.ibatis.annotations.Param;
import org.mmall.pojo.OrderItem;

import java.util.List;

public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

    /** 获取订单项，通过订单号和用户id */
    List<OrderItem> getByOrderNoUserId(@Param("orderNo") Long orderNo, @Param("userId") Integer userId);

    /** 获取订单项，通过订单号 */
    List<OrderItem> getByOrderNo(Long orderNo);

    /** 批量插入(TODO 一定要@Param，否则无法识别) */
    void batchInsert(@Param("orderItemList") List<OrderItem> orderItemList);
}