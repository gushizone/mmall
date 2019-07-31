package org.mmall.dao;

import org.apache.ibatis.annotations.Param;
import org.mmall.pojo.Order;

import java.util.List;

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

    /** 通过用户id查询订单 */
    List<Order> selectByUserId(Integer userId);

    /** 查询所有订单 */
    List<Order> selectAllOrder();

    /** 查询订单状态 **/
    List<Order> selectOrderStatusByCreateTime(@Param("status") Integer status,@Param("date") String date);

    /** 关闭订单 **/
    int closeOrderByOrderId(Integer id);
}