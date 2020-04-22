package cn.lqcnb.homemall.service.oauth2.service.fallback;

import cn.lqcnb.homemall.api.admin.dto.Users;
import cn.lqcnb.homemall.commons.dto.LayUI;
import cn.lqcnb.homemall.commons.dto.R;
import cn.lqcnb.homemall.commons.dto.ResponseResult;
import cn.lqcnb.homemall.commons.utils.MapperUtils;
import cn.lqcnb.homemall.service.oauth2.service.ProfileFeign;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author lqc520
 * @Description: 熔断
 * @date 2020/3/18 16:56
 */
@Component
public class ProfileFeignFallback implements ProfileFeign {
    public static final String BREAKING_MESSAGE = "请求失败了，请检查您的网络";

    @Override
    public String info(String username) {
        try {
            return MapperUtils.obj2json(new ResponseResult<Void>(ResponseResult.CodeStatus.BREAKING, BREAKING_MESSAGE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String echo() {
        return BREAKING_MESSAGE;
    }

    /**
     * 获取用户信息
     *
     * @return LayUI
     */
    @Override
    public LayUI getList() {
        return LayUI.error("权限不足");
    }

    /**
     * 获取用户信息
     *
     * @return LayUI
     */
    @Override
    public LayUI getMapList() {
        return LayUI.error("权限不足");
    }

    /**
     * 更新信息
     *
     * @param users 用户
     * @return R
     */
    @Override
    public R update(@RequestParam Users users) {
        return R.error("熔断降级");
    }

    /**
     * 重置密码
     *
     * @param mobile   手机号
     * @param password 密码
     * @return R
     */
    @Override
    public R resetPwd(String mobile, String password) {
        return R.error("熔断降级");
    }

}
