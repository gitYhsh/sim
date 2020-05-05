package com.xlcxx.config.filter;

import com.xlcxx.config.aspect.ValifyToken;
import com.xlcxx.plodes.baseServices.RedisService;
import com.xlcxx.plodes.system.domain.MyUser;
import com.xlcxx.plodes.system.service.UserService;
import com.xlcxx.utils.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Description: plodes
 * Created by yhsh on 2020/4/2 9:32
 * version 2.0
 * 方法说明
 */
public class AutonInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisService redis;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**从http中获取 toke**/
        String token = request.getHeader("token");
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //监察不需要验证的 method
        if (method.isAnnotationPresent(ValifyToken.class)) {
            ValifyToken isToken = method.getAnnotation(ValifyToken.class);
            //判断方法是否需要验证
            if (isToken.required()) {
                if (StringUtils.isEmpty(token)) {
                    throw new RuntimeException("请求没有token.请先登陆获取token");
                }
                String usera = (String) JwtUtil.parseJWT(token).get("username");
                Map<String, String> map = redis.hmGetAll(usera);
                MyUser user = new MyUser();
                if (null == map) {
                    throw new RuntimeException("用户不存在");
                }
                if (!map.get("userid").equals(usera)) {
                    throw new RuntimeException("用户不存在");
                }
                //验证token  是否有效  主要看时间
                boolean isTrui = JwtUtil.isVerify(token, user);
                if (!isTrui) {
                    throw new RuntimeException("token 无效");
                }
                return isTrui;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
