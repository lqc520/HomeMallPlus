package cn.lqcnb.mall.api.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.http.HttpStatus;

/**
 * @author lqc520
 * @Description: 异常处理
 * @date 2020/3/13 1:00
 */
@SpringBootConfiguration
public class ErrorPageConfig implements ErrorPageRegistrar {
    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {


        if(HttpStatus.MULTI_STATUS.is5xxServerError()){
            registry.addErrorPages(new ErrorPage("/error.html"));
        }else if(HttpStatus.MULTI_STATUS.is4xxClientError()){
            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
            registry.addErrorPages(error404Page);
        }else{
            registry.addErrorPages(new ErrorPage("/error.html"));
        }

    }
}
