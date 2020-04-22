package cn.lqcnb.mall.common.utils;

import com.google.code.kaptcha.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author lqc520
 * @Description: 验证码工具类
 * @date 2020/3/5 20:39
 */
@Component
public class CodeUtils {
    /**
     * 将获取到的前端参数转为string类型
     * @param request
     * @param key
     * @return
     */
    @Autowired
    private  HttpSession session;

    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 验证码校验
     * @param key 验证码
     * @return boolean
     */
    public boolean checkVerifyCode(String key) {
        String verifyCodeExpected = (String) session.getAttribute("capText");
        logger.info("verifyCodeExpected："+verifyCodeExpected,"key:"+key);
        return key.equals(verifyCodeExpected);
    }
}
