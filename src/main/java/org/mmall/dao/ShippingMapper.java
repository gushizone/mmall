package org.mmall.dao;

import org.apache.ibatis.annotations.Param;
import org.mmall.pojo.Shipping;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    /** 通过用户id和收获地址id删除 */
    int deleteByShippingIdUserId(@Param("shippingId") Integer shippingId, @Param("userId") Integer userId);

    /** 通过用户id和收获地址id修改 */
    int updateByShipping(Shipping shipping);

    /** 通过用户id和收获地址id查询单个 */
    Shipping selectByShippingIdUserId(@Param("shippingId") Integer shippingId, @Param("userId") Integer userId);

    /** 查询个人所有收货地址 */
    List<Shipping> selectByUserId(Integer userId);
}