/**
 * Copyright (c) https://github.com/gushizone
 */

package org.mmall.controller.protal;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.mmall.common.ResponseCode;
import org.mmall.common.ServerResponse;
import org.mmall.pojo.Shipping;
import org.mmall.pojo.User;
import org.mmall.service.IShippingService;
import org.mmall.util.CookieUtil;
import org.mmall.util.JsonUtil;
import org.mmall.util.RedisPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author gushizone@gmail.com
 * @createTime 2018/4/23 19:10
 * @desc 收货地址
 */
@Controller
@RequestMapping("/shipping")
public class ShippingController {

    @Autowired
    private IShippingService iShippingService;

    /** 新增收货地址 */
    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse add(HttpServletRequest httpRequest, Shipping shipping){
        // 从 cookie 中获取 token
        String loginToken = CookieUtil.readLoginToken(httpRequest);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获得当前用户的信息");
        }
        // 从 redis 中获取 用户信息
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr, User.class);

        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.add(user.getId(), shipping);
    }

    /** 删除收货地址 */
    @RequestMapping("del.do")
    @ResponseBody
    public ServerResponse<String> del(HttpServletRequest httpRequest, Integer shippingId){
        // 从 cookie 中获取 token
        String loginToken = CookieUtil.readLoginToken(httpRequest);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获得当前用户的信息");
        }
        // 从 redis 中获取 用户信息
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr, User.class);

        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.del(user.getId(), shippingId);
    }

    /** 修改收货地址 */
    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse<String> update(HttpServletRequest httpRequest, Shipping shipping){
        // 从 cookie 中获取 token
        String loginToken = CookieUtil.readLoginToken(httpRequest);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获得当前用户的信息");
        }
        // 从 redis 中获取 用户信息
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr, User.class);

        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.update(user.getId(), shipping);
    }

    /** 查询收货地址 */
    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse<Shipping> select(HttpServletRequest httpRequest, Integer shippingId){
        // 从 cookie 中获取 token
        String loginToken = CookieUtil.readLoginToken(httpRequest);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获得当前用户的信息");
        }
        // 从 redis 中获取 用户信息
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr, User.class);

        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.select(user.getId(), shippingId);
    }

    /** 查询收货地址列表（分页） */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         HttpServletRequest httpRequest){
        // 从 cookie 中获取 token
        String loginToken = CookieUtil.readLoginToken(httpRequest);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录，无法获得当前用户的信息");
        }
        // 从 redis 中获取 用户信息
        String userJsonStr = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr, User.class);

        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iShippingService.list(user.getId(), pageNum, pageSize);
    }


}
