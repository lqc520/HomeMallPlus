package cn.lqcnb.mall.api.config;

import cn.lqcnb.mall.api.interceptor.*;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootConfiguration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(authenticationInterceptor())
                .addPathPatterns("/**");

        registry.addInterceptor(adminAuthenticationInterceptor())
                .addPathPatterns("/**");

//        registry.addInterceptor(new AdminLoginInterceptor())
//                .addPathPatterns("/admin/**")
//                .excludePathPatterns("/admin/user/**")
//                .excludePathPatterns("/css/**", "/img/**", "/js/**", "/plugin/**", "/fonts/**");

        registry.addInterceptor(new AliPayInterceptor()).addPathPatterns("/api/alipay/**");
//
//        registry.addInterceptor(new RequestInterceptor()).addPathPatterns("/**")
//                .excludePathPatterns("/css/**", "/img/**", "/js/**", "/plugin/**", "/fonts/**","/common/**","/webjars/**","/layuiadmin/**");
    }



    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }

    @Bean
    public AdminAuthenticationInterceptor adminAuthenticationInterceptor() {
        return new AdminAuthenticationInterceptor();
    }

//    @Bean
//    public RequestInterceptor requestInterceptor(){
//        return new RequestInterceptor();
//    }



    /**
     * 配置静态资源
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");

    }

}
