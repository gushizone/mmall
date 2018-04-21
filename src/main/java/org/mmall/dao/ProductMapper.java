package org.mmall.dao;

import org.apache.ibatis.annotations.Param;
import org.mmall.pojo.Product;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    /** 查询产品列表 */
    List<Product> selectList();

    /** 通过产品名或id查询 */
    List<Product> selectByNameAndProductId(@Param("productName")String productName, @Param("productId")Integer productId);

    /** 通过产品名或分类id列表查询 */
    List<Product> selectByNameAndCategoryIds(@Param("productName")String productName, @Param("categoryIdList")List<Integer> categoryIdList);
}