package cn.lqcnb.mall.api.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * @Author: Lin QiCheng
 * @Date: 2019/8/30 19:11
 * To change this template use File | Settings | File Templates.
 * Description: url无法注入 必须new 改地址记得改
 * Modify by
 */

public class AdminLoginInterceptor extends HandlerInterceptorAdapter {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //        session版本
        if (request.getSession().getAttribute("admin") == null) {
            logger.info("AdminLoginInterceptor.preHandle");
            response.sendRedirect("http://localhost:9989/" + "admin/user/login.html");
            return false;
        }
        return  true;
    }
}
