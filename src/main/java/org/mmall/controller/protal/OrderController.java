/**
 * Copyright (c) https://github.com/gushizone
 */

package org.mmall.controller.protal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import org.mmall.common.Const;
import org.mmall.common.ResponseCode;
import org.mmall.common.ServerResponse;
import org.mmall.pojo.User;
import org.mmall.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

/**
 * @author gushizone@gmail.com
 * @createTime 2018/4/29 18:31
 * @desc 门户：订单接口
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);


    @Autowired
    private IOrderService iOrderService;

    /**
     * 创建订单
     * （购物车Cart -> 订单项OrderItem） + 收货地址Shipping + 生成订单 + 减少库存 + 清空购物车
     * @param shippingId 收货地址id
     */
    @RequestMapping("create.do")
    @ResponseBody
    public ServerResponse create(HttpSession session, Integer shippingId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.createOrder(user.getId(), shippingId);
    }

    /** 取消订单 */
    @RequestMapping("cancel.do")
    @ResponseBody
    public ServerResponse cancel(HttpSession session, Long orderNo){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.cancel(user.getId(), orderNo);
    }

    /** 订单预览：获取购物车已勾选产品等 */
    @RequestMapping("get_order_cart_product.do")
    @ResponseBody
    public ServerResponse getOrderCartProduct(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderCartProduct(user.getId());
    }

    /** 获取订单详情 */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse detail(HttpSession session, Long orderNo){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderDetail(user.getId(), orderNo);
    }

    /** 获取订单列表：分页 */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse list(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderList(user.getId(), pageNum, pageSize);
    }

    /**
     * 订单支付（整合支付宝支付，预下单 ）
     * return Map（订单号；二维码路径）
     */
    @RequestMapping("pay.do")
    @ResponseBody
    public ServerResponse pay(HttpSession session, Long orderNo, HttpServletRequest request){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        String path = request.getSession().getServletContext().getRealPath("upload"); // TODO 根目录下upload文件夹
        return iOrderService.pay(orderNo, user.getId(), path);
    }

    /**
     * 处理支付宝回调，验证合法性，完成交易（当面付-扫码支付 异步通知）
     * https://docs.open.alipay.com/194/103296
     * 1. 支付宝SDK验签
     * 2. 自行验证（out_trade_no） 及 过滤重复通知
     * 3. 修改订单状态，记录支付信息
     * @param request 包含 支付宝异步通知
     * @return 程序执行成功完后必须打印输出“success”（不包含引号）。
     * 如果商户反馈给支付宝的字符不是success这7个字符，支付宝服务器会不断重发通知，直到超过24小时22分钟。
     * 一般情况下，25小时以内完成8次通知（通知的间隔频率一般是：4m,10m,10m,1h,2h,6h,15h）；
     */
    @RequestMapping("alipay_callback")
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request){
        Map<String, String> params = Maps.newHashMap();

        Map requestParams = request.getParameterMap();
//        将 字符串数组 转变为 ","分割的字符串
        for(Iterator iter = requestParams.keySet().iterator(); iter.hasNext();){ // TODO 迭代器循环
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valuesStr = "";
            for(int i = 0; i < values.length; i++){
                valuesStr = (i == values.length-1) ? valuesStr+values[i] : valuesStr+values[i]+",";
            }
            params.put(name, valuesStr);
        }
//        打印支付宝签名，交易状态，所有后调参数
        logger.info("支付宝回调,sign:{},trade_status:{},参数{}", params.get("sign"), params.get("trade_status"), params.toString());


//        SDK异步返回结果的验签（https://docs.open.alipay.com/194/103296#s4）
        params.remove("sign_type"); // 需要手动去除"sign_type"（SDK会自动去除"sign"）
        try {
//             SDK提供的验签方法（支持RSA1和RSA2，注意：1.参数是支付宝公钥；2.创建订单时字符集默认"utf-8"）
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(),
                    "utf-8", Configs.getSignType());

            if(!alipayRSACheckedV2){
                return ServerResponse.createByErrorMessage("非法请求，验证不通过。若再恶意请求，后果自负！");
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝验证回调异常", e);
        }

//        处理支付宝回调
        ServerResponse serverResponse = iOrderService.aliCallback(params);
        if(serverResponse.isSuccess()){
            return Const.AlipayCallback.RESPONSE_SUCCESS; // 反馈success给支付宝，支付宝会不再异步通知
        }
        return Const.AlipayCallback.RESPONSE_FAILED;
    }

    /** 查询订单支付状态（轮询接口，数据库） */
    @RequestMapping("query_order_pay_status.do")
    @ResponseBody
    public ServerResponse<Boolean> queryOrderPayStatus(HttpSession session, Long orderNo){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        ServerResponse serverResponse = iOrderService.queryOrderPayStatus(user.getId(), orderNo);
        if(serverResponse.isSuccess()){
            return ServerResponse.createBySuccess(true); // 查询成功：已完成支付
        }
        return ServerResponse.createBySuccess(false); // 查询成功：未完成支付
    }

}
