package org.mmall.controller.backend;

import org.mmall.common.Const;
import org.mmall.common.ServerResponse;
import org.mmall.pojo.User;
import org.mmall.service.IUserService;
import org.mmall.util.CookieUtil;
import org.mmall.util.JsonUtil;
import org.mmall.util.RedisPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by Apple on 2017/6/12.
 * 后台管理：用户
 */
@Controller
@RequestMapping("/manage/user")
public class UserManageContrlller {

    @Autowired
    private IUserService iUserService;


    /** 管理员登陆：session记录 */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session,
                                      HttpServletResponse httpResponse){
        ServerResponse<User> response = iUserService.login(username, password);
        if(response.isSuccess()){
            User user = response.getData();
            if(user.getRole() == Const.Role.ROLE_ADMIN){
                //说明登录的是管理员
                CookieUtil.writeLoginToken(httpResponse, session.getId());
                RedisPoolUtil.setEx(session.getId(), JsonUtil.obj2String(response.getData()),
                        Const.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }else {
                return ServerResponse.createByErrorMessage("不是管理员,无法登录");
            }
        }
        return response;
    }

}
