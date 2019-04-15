package org.mmall.dao;

import org.apache.ibatis.annotations.Param;
import org.mmall.pojo.Cart;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    /** 根据用户id和产品查询购物车 */
    Cart selectCartByUseIdProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    /** 根据用户id查询购物车内所有产品 */
    List<Cart> selectCartByUserId(Integer userId);

    /** 查询购物车内商品勾选状态（购物车未勾选数目） */
    int selectCartProductCheckedStatusByUserId(Integer userId);

    /** 通过用户id和产品List删除 */
    int deleteByUserIdProductIds(@Param("userId")Integer userId, @Param("productList")List<String> productList);

    /** 全选/全反选, 单选/取消单选 */
    void checkedOrUncheckedProduct(@Param("userId")Integer userId, @Param("productId") Integer productId, @Param("checked") int checked);

    /** 获取购物车内商品总数 */
    int selectCartProductCount(Integer userId);

    /** 通过用户id，获取购物车已勾选商品列表 */
    List<Cart> selectCheckedCartByUserId(Integer userId);
}