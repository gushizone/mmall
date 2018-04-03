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
     * Set提高效率//todo 时间复杂度
     * 降序升序
     */
    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc","price_asc");
    }

    /**
     * 使用内部接口类分组常量
     * 比枚举轻量
     */
    public interface Role{
        int ROLE_CUSTOMER = 0; //普通用户
        int ROLE_ADMIN = 1; //管理员
    }

    public enum ProductStatusEnum{
        ON_SALE(1, "在线");
        private String value;
        private int code;
        ProductStatusEnum(int code, String value){
            this.code = code;
            this.value = value;
        }
        public String getValue(){
            return value;
        }
        public int getCode(){
            return code;
        }
    }
}
