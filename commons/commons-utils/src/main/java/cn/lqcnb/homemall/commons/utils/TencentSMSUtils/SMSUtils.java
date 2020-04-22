package cn.lqcnb.homemall.commons.utils.TencentSMSUtils;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Async
@Component
public class SMSUtils {
    public static String getLoginCode(String tel) {
        return BaseSMS.getSMSCode(tel,
                358891,4);
    }
    public static String getRegisterCode(String tel) {
        return BaseSMS.getSMSCode(tel,
                225482,4);
    }
    public static String getReSetCode(String tel) {
        return BaseSMS.getSMSCode(tel,
                258493,4);
    }
}
