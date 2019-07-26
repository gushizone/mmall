/**
 * Copyright (c) https://github.com/gushizone
 */

package org.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import org.mmall.common.ServerResponse;
import org.mmall.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author gushizone@gmail.com
 * @createTime 2018/5/20 23:42
 * @desc 后台：订单管理
 */
@Controller
@RequestMapping("/manage/order")
public class OrderManageController {

    @Autowired
    private IOrderService iOrderService;


    /** 获取订单列表：分页 */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse orderList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        return iOrderService.manageList(pageNum, pageSize);
    }

    /** 获取订单详情 */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse orderDetail(Long orderNo){
        return iOrderService.manageDetail(orderNo);
    }

    /** 搜索订单（分页 ，TODO 多条件搜索） */
    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderSearch(Long orderNo,
                                                @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        return iOrderService.manageSearch(orderNo, pageNum, pageSize);
    }

    /** 发货 */
    @RequestMapping("send_goods.do")
    @ResponseBody
    public ServerResponse<String> orderSendGoods(Long orderNo){
        return iOrderService.manageSendGoods(orderNo);
    }

}
