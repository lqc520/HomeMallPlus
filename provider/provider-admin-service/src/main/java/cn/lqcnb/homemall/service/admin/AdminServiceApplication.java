package cn.lqcnb.homemall.service.admin;

import cn.lqcnb.homemall.configuration.DubboSentinelConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author lqc520
 * @Description: 管理员服务
 * @date 2020/3/17 0:11
 */

@SpringBootApplication(scanBasePackageClasses = {AdminServiceApplication.class, DubboSentinelConfiguration.class})
@MapperScan(basePackages = "cn.lqcnb.homemall.service.admin.mapper")
public class AdminServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminServiceApplication.class);
    }
}
