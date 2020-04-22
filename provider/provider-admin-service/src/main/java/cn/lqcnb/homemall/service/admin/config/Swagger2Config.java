package cn.lqcnb.homemall.service.admin.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author lqc520
 * @Description: swagger2
 * @date 2020/3/31 20:55
 * @see cn.lqcnb.homemall.service.admin.config
 */
@SpringBootConfiguration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 指定对哪些包下的类生成API文档
                //.apis(RequestHandlerSelectors.basePackage("cn.lqcnb.mall.api"))
                // 对有ApiOperation注解的类生成文档
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                // 扫描所有
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("用户模块API文档")
                .description("用户模块API文档")
                .termsOfServiceUrl("http://mall.lqcnb.cn/")
                .version("1.0")
                .build();
    }
}
