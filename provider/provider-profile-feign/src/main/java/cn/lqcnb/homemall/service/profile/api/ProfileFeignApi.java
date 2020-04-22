package cn.lqcnb.homemall.service.profile.api;

/**
 * @author lqc520
 * @Description: 个人信息管理
 * @date 2020/3/17 23:08
 */

import cn.lqcnb.homemall.service.profile.api.fallback.ProfileFeignApiFallback;
import cn.lqcnb.homemall.configration.FeignRequestConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@Component
@FeignClient(value = "provider-profile-service", path = "profile", configuration = FeignRequestConfiguration.class,fallback = ProfileFeignApiFallback.class)
public interface ProfileFeignApi {
    /**
     * 获取用户信息
     * @param mobile 手机号
     * @return String
     */
    @GetMapping(value = "info/{mobile}")
    String info(@PathVariable String mobile);
}
