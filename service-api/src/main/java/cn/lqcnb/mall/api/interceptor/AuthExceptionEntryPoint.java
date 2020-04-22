package cn.lqcnb.mall.api.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lqc520
 * @Description: 没有权限跳转登录
 * @date 2020/3/29 22:49
 * @see cn.lqcnb.mall.api.interceptor
 */
public class AuthExceptionEntryPoint implements AuthenticationEntryPoint {


    @Value("${cn.lqcnb.mall.login}")
    private String loginUrl;

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException)
            throws ServletException {
        try {
            logger.error(authException.getMessage());
            logger.warn("没有权限 跳转登录：" + loginUrl);
            response.sendRedirect(loginUrl);
        } catch (Exception e) {
            throw new ServletException();
        }
    }
}
