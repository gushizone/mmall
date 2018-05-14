package org.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by Apple on 2017/6/10.
 * 常量类
 */
public class Const {

    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    /**
     * 产品：排序方式（Set提高效率//todo 时间复杂度：List Set）
     */
    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc","price_asc");
    }

    // TODO 使用内部接口类分组常量 比枚举轻量

    /** 角色 */
    public interface Role{
        int ROLE_CUSTOMER = 0; //普通用户
        int ROLE_ADMIN = 1; //管理员
    }
    /** 购物车 */
    public interface Cart{
        int CHECKED = 1; //选中状态
        int UN_CHECKED = 0; // 未选中状态

        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";// 库存不满足需求
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS"; // 库存满足需求
    }

    /**
     * 产品状态枚举
     */
    public enum ProductStatusEnum{
        ON_SALE(1, "在线");

        private int code;
        private String value;

        ProductStatusEnum(int code, String value){
            this.code = code;
            this.value = value;
        }

        public int getCode(){
            return code;
        }

        public String getValue(){
            return value;
        }
    }

    /** 订单状态 */
    public enum OrderStatusEnum{
        CANCELED(0, "已取消"),
        NO_PAY(10, "未支付"),
        PAID(20, "已付款"),
        SHIPPED(40, "已发货"),
        ORDER_SUCCESS(50, "订单完成"),
        ORDER_CLOSE(60, "订单关闭");


        OrderStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        private int code;
        private String value;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    /**
     * 支付宝回调
     * https://docs.open.alipay.com/194/103296
     * 反馈success给支付宝，支付宝会不再异步通知
     */
    public interface AlipayCallback{
        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY"; // 交易创建，等待买家付款
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS"; // 交易支付成功

        String RESPONSE_SUCCESS = "success"; //响应：成功
        String RESPONSE_FAILED = "failed"; // 响应：失败
    }

    /** 支付平台 */
    public enum PayPlatformEnum{
        ALIPAY(1, "支付宝");

        PayPlatformEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        private int code;
        private String value;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }






}
