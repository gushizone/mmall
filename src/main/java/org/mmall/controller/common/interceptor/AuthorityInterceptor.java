package org.mmall.controller.common.interceptor;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.mmall.common.Const;
import org.mmall.common.ServerResponse;
import org.mmall.pojo.User;
import org.mmall.util.CookieUtil;
import org.mmall.util.JsonUtil;
import org.mmall.util.RedisShardedPoolUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * 权限认证拦截器
 *
 * TODO 展示只演示管理员操作
 */
@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor{

    /**
     * controller 之前 解析权限认证
     * @param request
     * @param response
     * @param handler
     * @return true 表示放行，false 表示阻断
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle");
        //请求中Controller中的方法名
        HandlerMethod handlerMethod = (HandlerMethod)handler;

        //解析HandlerMethod 类名，方法名，参数
        String className = handlerMethod.getBean().getClass().getSimpleName();
        String methodName = handlerMethod.getMethod().getName();
        Map paramMap = request.getParameterMap();

        // TODO 代码方式 ：放行登录，避免死循环
//        if(StringUtils.equals(className,"UserManageController")
//            && StringUtils.equals(methodName,"login")){
//            log.info("权限拦截器拦截到请求,className:{},methodName:{}",className,methodName);
//            //如果是拦截到登录请求，不打印参数，因为参数里面有密码，全部会打印到日志中，防止日志泄露
//            return true;
//        }

        // 记录拦截信息
        printInterceptorInfo(className, methodName, paramMap);


        User user = null;
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isNotEmpty(loginToken)){
            String userJsonStr = RedisShardedPoolUtil.get(loginToken);
            user = JsonUtil.string2Obj(userJsonStr,User.class);
        }

        // 为登录或非管理员
        if(user == null || !user.getRole().equals(Const.Role.ROLE_ADMIN)){
            //返回false.即不会调用controller里的方法
            // 添加reset，重置响应，否则报异常 getWriter() has already been called for this response.
            response.reset();
            // 设置编码，否则会乱码 （因为脱离了 springmvc 的返回流程）
            response.setCharacterEncoding("UTF-8");
            // 设置返回值的类型，确保前端可以正常接收 json
            response.setContentType("application/json;charset=UTF-8");

            PrintWriter out = response.getWriter();

            // 上传由于富文本的控件要求，要特殊处理返回值，这里面区分是否登录以及是否有权限 （ProductManageController.richtextImgUpload）
            if(user == null){ // 未登录
                if(StringUtils.equals(className,"ProductManageController")
                        && StringUtils.equals(methodName,"richtextImgUpload")){
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success",false);
                    resultMap.put("msg","请登录管理员");
                    out.print(JsonUtil.obj2String(resultMap));
                }else{
                    out.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("拦截器拦截,用户未登录")));
                }
            }else{ // 非管理员
                if(StringUtils.equals(className,"ProductManageController")
                        && StringUtils.equals(methodName,"richtextImgUpload")){
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success",false);
                    resultMap.put("msg","无权限操作");
                    out.print(JsonUtil.obj2String(resultMap));
                }else{
                    out.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("拦截器拦截,用户无权限操作")));
                }
            }
            out.flush();
            out.close();//要关闭输出流
            // 阻断
            return false;

        }
        // 放行
        return true;
    }

    /**
     * controller 处理之后，Viewer 之前
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle");
    }

    /**
     * Viewer 之后
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("afterCompletion");
    }


    /**
     * 记录拦截到的信息
     * @param className
     * @param methodName
     * @param paramMap
     */
    private void printInterceptorInfo(String className, String methodName, Map paramMap) {
        //解析参数,具体的参数key以及value是什么，我们打印日志
        StringBuffer requestParamBuffer = new StringBuffer();
        Iterator it = paramMap.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry entry = (Map.Entry)it.next();
            String mapKey = (String)entry.getKey();

            String mapValue = StringUtils.EMPTY;

            //request这个参数的map，里面的value返回的是一个String[]
            Object obj = entry.getValue();
            if(obj instanceof String[]){
                String[] strs = (String[])obj;
                mapValue = Arrays.toString(strs);
            }
            requestParamBuffer.append(mapKey).append("=").append(mapValue);
        }

        log.info("权限拦截器拦截到请求,className:{},methodName:{},param:{}",className,methodName,requestParamBuffer.toString());
    }
}
