/**
 * Copyright (c) https://github.com/gushizone
 */

package org.mmall.service;

import org.mmall.common.ServerResponse;
import org.mmall.vo.CartVo;

/**
 * @author gushizone@gmail.com
 * @createTime 2018/4/22 17:48
 * @desc 购物车Service接口
 */
public interface ICartService {

    public ServerResponse<CartVo> list(Integer userId);

    public ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);

    public ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count);

    public ServerResponse<CartVo> deleteProduct(Integer userId, String productIds);

    public ServerResponse<CartVo> selectOrUnSelect(Integer userId, Integer productId, int checked);

    public ServerResponse<Integer> getCartProductCount(Integer userId);
}
