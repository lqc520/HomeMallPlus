//package cn.lqcnb.mall.api.aspect;
//
//import lombok.extern.slf4j.Slf4j;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.TimeUnit;
//
///**
// * @author lqc520
// * @Description: pv uv 统计
// * @date 2020/3/6 16:35
// */
//@Aspect
//@Component
//@Slf4j
//public class RpcLogAspect {
//
//    @Resource
//    private RedisTemplate redisTemplate;
//
//    /**
//     * 统计用户UV
//     *
//     * @param jp
//     * @param rvt
//     */
//    @AfterReturning(returning = "rvt", pointcut = "execution(* com.liwen.user.dubbo.UserAuthenticationProvider.verifyAccessToken(..)) || execution(* com.liwen.user.dubbo.UserAuthenticationProvider.getUserIdByToken(..)) ")
//    public void loginRecord(JoinPoint jp, Object rvt) {
//        if (rvt == null) {
//            return;
//        }
//        try {
//            String time = DateTimeUtils.dateToStr(new Date(), DateTimeUtils.FORMAT_DEFAULT_YMD_NS);
//            String key = "user" + time;
//            if (rvt instanceof AccessTokenResult) {
//                AccessTokenResult tokenResult = (AccessTokenResult) rvt;
//                if (tokenResult != null && tokenResult.getUserId() > 0) {
//                    insertHyperLogLog(tokenResult.getUserId(), key);
//                }
//            } else if (rvt instanceof Long && (long) rvt > 0) {
//                insertHyperLogLog((long) rvt, key);
//            }
//        } catch (Exception e) {
//            log.info("统计用户uv失败", e);
//        }
//    }
//
//    private void insertHyperLogLog(Long userId, String key) {
//        CompletableFuture.runAsync(() -> {
//            redisTemplate.expire(key, 30, TimeUnit.DAYS);
//            redisTemplate.opsForHyperLogLog().add(key, userId);
//        });
//    }
//}