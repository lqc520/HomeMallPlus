package cn.lqcnb.homemall.service.profile.api.fallback;

import cn.lqcnb.homemall.service.profile.api.ProfileFeignApi;
import cn.lqcnb.homemall.commons.dto.ResponseResult;
import cn.lqcnb.homemall.commons.utils.MapperUtils;
import org.springframework.stereotype.Component;

/**
 * @author lqc520
 * @Description: 熔断
 * @date 2020/3/18 16:56
 */
@Component
public class ProfileFeignApiFallback implements ProfileFeignApi {
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
}
