package org.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by geely
 *
 * Cookie 工具类
 */
@Slf4j
public class CookieUtil {

    // cookie 根域名
    private final static String COOKIE_DOMAIN = ".mmall.com";
    // cookie 登录token
    private final static String COOKIE_NAME = "mmall_login_token";


    /**
     * 读取 cookie
     *
     * @param request
     * @return
     */
    public static String readLoginToken(HttpServletRequest request) {
        Cookie[] cks = request.getCookies();
        if (cks != null) {
            for (Cookie ck : cks) {
                log.info("read cookieName:{},cookieValue:{}", ck.getName(), ck.getValue());
                if (StringUtils.equals(ck.getName(), COOKIE_NAME)) {
                    log.info("return cookieName:{},cookieValue:{}", ck.getName(), ck.getValue());
                    return ck.getValue();
                }
            }
        }
        return null;
    }

    // TODO cookie 可以被子域名拿到，不能被同级和上一级拿到。
    //X:domain=".happymmall.com"
    //a:A.mmall.com            cookie:domain=A.mmall.com;path="/"
    //b:B.mmall.com            cookie:domain=B.mmall.com;path="/"
    //c:A.mmall.com/test/cc    cookie:domain=A.mmall.com;path="/test/cc"
    //d:A.mmall.com/test/dd    cookie:domain=A.mmall.com;path="/test/dd"
    //e:A.mmall.com/test       cookie:domain=A.mmall.com;path="/test"

    /**
     * 写入 登录 token
     *
     * @param response
     * @param token
     */
    public static void writeLoginToken(HttpServletResponse response, String token) {
        Cookie ck = new Cookie(COOKIE_NAME, token);
        ck.setDomain(COOKIE_DOMAIN);
        ck.setPath("/");//代表设置在根目录
        ck.setHttpOnly(true);// TODO 防止脚本读取 cookie，提高安全性。
        //TODO 如果这个maxage不设置的话，cookie就不会写入硬盘，而是写在内存。只在当前页面有效。
        ck.setMaxAge(60 * 60 * 24 * 365);//如果是-1，代表永久。单位(s)。
        log.info("write cookieName:{},cookieValue:{}", ck.getName(), ck.getValue());
        response.addCookie(ck);
    }


    /**
     * 删除 登录 token
     *
     * @param request
     * @param response
     */
    public static void delLoginToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cks = request.getCookies();
        if (cks != null) {
            for (Cookie ck : cks) {
                if (StringUtils.equals(ck.getName(), COOKIE_NAME)) {
                    ck.setDomain(COOKIE_DOMAIN);
                    ck.setPath("/");
                    ck.setMaxAge(0);//设置成0，代表删除此cookie。
                    log.info("del cookieName:{},cookieValue:{}", ck.getName(), ck.getValue());
                    response.addCookie(ck);
                    return;
                }
            }
        }
    }


}
