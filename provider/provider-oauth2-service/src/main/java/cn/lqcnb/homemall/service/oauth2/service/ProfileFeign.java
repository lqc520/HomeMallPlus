package cn.lqcnb.homemall.service.oauth2.service;

/**
 * @author lqc520
 * @Description: 个人信息管理
 * @date 2020/3/17 23:08
 */

import cn.lqcnb.homemall.api.admin.dto.Users;
import cn.lqcnb.homemall.commons.dto.LayUI;
import cn.lqcnb.homemall.commons.dto.R;
import cn.lqcnb.homemall.configration.FeignRequestConfiguration;
import cn.lqcnb.homemall.service.oauth2.service.fallback.ProfileFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@FeignClient(value = "provider-profile-service", path = "profile", configuration = FeignRequestConfiguration.class,fallback = ProfileFeignFallback.class)
public interface ProfileFeign {
    /**
     * 获取用户信息
     * @param mobile 手机号
     * @return String
     */
    @GetMapping("info/{mobile}")
    String info(@PathVariable String mobile);

    @GetMapping("echo")
    String echo();

    /**
     * 获取用户信息
     * @return LayUI
     */
    @GetMapping("getList")
    LayUI getList();

    /**
     * 获取用户信息
     * @return LayUI
     */
    @GetMapping("getMapList")
    LayUI getMapList();

    /**
     * 更新信息
     * @param users 用户
     * @return R
     */
    @PostMapping("update")
    R update(@RequestBody Users users);


    /**
     * 重置密码
     * @param mobile 手机号
     * @return R
     */
    @GetMapping("resetPwd")
     R resetPwd(@RequestParam("mobile") String mobile,@RequestParam("password") String password);
}
