package cn.lqcnb.homemall.service.admin.service.fallback;

import cn.lqcnb.homemall.api.admin.dto.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lqc520
 * @Description: 熔断
 * @date 2020/3/19 13:05
 */
public class UsersServiceFallback {
   private final static Logger logger = LoggerFactory.getLogger(UsersServiceFallback.class);


    public static Users getUsersByMobileFallback(String mobile, Throwable ex) {
        logger.warn("Invoke getUserByMobileFallback: " + ex.getClass().getTypeName());
        logger.info("当前手机号"+mobile);
        return new Users();
    }

}
