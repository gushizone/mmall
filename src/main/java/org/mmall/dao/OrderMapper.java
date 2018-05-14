package org.mmall.dao;

import org.apache.ibatis.annotations.Param;
import org.mmall.pojo.Order;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    /** 通过用户id和订单号查订单 */
    Order selectByUserIdAndOrderNo(@Param("userId") Integer userId, @Param("orderNo") Long orderNo);

    /** 通过订单号查询订单 */
    Order selectByOrderNo(Long orderNo);
}