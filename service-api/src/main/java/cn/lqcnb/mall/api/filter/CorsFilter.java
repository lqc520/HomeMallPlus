//package cn.lqcnb.mall.api.filter;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * @author lqc520
// * @Description: 跨域
// * @date 2020/3/29 23:15
// * @see cn.lqcnb.mall.api.filter
// */
//
//@Order(Ordered.HIGHEST_PRECEDENCE)
//@Configuration
//public class CorsFilter implements Filter {
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        response.setHeader("Access-Control-Allow-Origin","*");
//        response.setHeader("Access-Control-Allow-Credentials","true");
//        response.setHeader("Access-Control-Allow-Methods","POST,GET,OPTIONS,PUT,DELETE,PATCH,HEAD");
//        response.setHeader("Access-Control-Allow-Max-Age","3600");
//        response.setHeader("Access-Control-Allow-Headers","*");
//        if("OPTIONS".equalsIgnoreCase(request.getMethod())){
//            response.setStatus(HttpServletResponse.SC_OK);
//        }else{
//            filterChain.doFilter(servletRequest,servletResponse);
//        }
//    }
//}
