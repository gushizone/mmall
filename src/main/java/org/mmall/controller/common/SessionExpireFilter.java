package org.mmall.controller.common;


import org.apache.commons.lang.StringUtils;
import org.mmall.common.Const;
import org.mmall.pojo.User;
import org.mmall.util.CookieUtil;
import org.mmall.util.JsonUtil;
import org.mmall.util.RedisPoolUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 *
 * 用于重置redis的token有效期
 */
public class SessionExpireFilter implements Filter {

    /**
     * 初始化
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * 每次请求前都会调用doFilter方法
     *
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;

        // 从 cookie 中获取 token
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isNotEmpty(loginToken)){
            // 从 redis 中获取用户信息
//            String userJsonStr = RedisShardedPoolUtil.get(loginToken);
            String userJsonStr = RedisPoolUtil.get(loginToken);
            User user = JsonUtil.string2Obj(userJsonStr, User.class);
            if(user != null){
                //如果user不为空，则重置session的时间，即调用expire命令
//                RedisShardedPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
                RedisPoolUtil.expire(loginToken, Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }
        }

        // 放行
        filterChain.doFilter(servletRequest,servletResponse);
    }

    /**
     * 销毁
     */
    @Override
    public void destroy() {

    }
}
