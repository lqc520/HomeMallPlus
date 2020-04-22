package cn.lqcnb.mall.api.config;

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

@SpringBootConfiguration
@EnableSwagger2
public class Swagger2Config {


    /**
     * 如何知道对哪些类生成api 文档呢
     * @return
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 指定对哪些包下的类生成API文档
                //.apis(RequestHandlerSelectors.basePackage("cn.lqcnb.mall.api"))
                // 对有ApiOperation注解的类生成文档
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("家居商城API文档")
                .description("家居商城API文档")
                .termsOfServiceUrl("http://mall.lqcnb.cn/")
                .version("1.0")
                .build();
    }
}
