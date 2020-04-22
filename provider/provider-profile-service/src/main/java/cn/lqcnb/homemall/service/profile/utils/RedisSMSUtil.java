package cn.lqcnb.homemall.service.profile.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisSMSUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;

    Logger logger = LoggerFactory.getLogger(getClass());

//    redisTemplate.opsForValue();　　//操作字符串
//    redisTemplate.opsForHash();　　 //操作hash
//    redisTemplate.opsForList();　　 //操作list
//    redisTemplate.opsForSet();　　  //操作set
//    redisTemplate.opsForZSet();　 　//操作有序set
    public RedisSMSUtil(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    /**
     *设置验证码
     * @param mobile 手机号
     * @param code 验证码
     * @param time 验证码有效期
     */
    @Async
    public  void setCodeTime(String mobile,String code,long time){
        redisTemplate.opsForValue().set("mobile_code:"+mobile,code);
        redisTemplate.expire("mobile_code:"+mobile,time, TimeUnit.SECONDS);
    }


    /**
     *获取验证码
     * @param mobile 手机号
     * @return 验证码
     */
    public  String getCode(String mobile){
        return redisTemplate.opsForValue().get("mobile_code:"+mobile);
    }



    /**
     *设置邮箱密匙
     * @param email 邮箱
     * @param code 邮箱验证码
     * @param time 验证码有效期
     */
    @Async
    public  void setEmailCodeTime(String email,String code,long time){
        redisTemplate.opsForValue().set("email_code:"+email,code);
        redisTemplate.expire("email_code:"+email,time, TimeUnit.SECONDS);
    }


    /**
     *获取邮箱密匙
     * @param email 邮箱
     * @return 验证码
     */
    public  String getEmailCode(String email){
        return redisTemplate.opsForValue().get("email_code:" + email);
    }


    /**
     *设置验证码
     * @param ip ip
     * @param code 图形验证码
     * @param time 验证码有效期
     */
    @Async
    public  void setIpCode(String ip,String code,long time){
        redisTemplate.opsForValue().set("ip_code:"+ip,code);
        redisTemplate.expire("ip_code:"+ip,time, TimeUnit.SECONDS);
    }


    /**
     *获取验证码
     * @param ip ip
     * @return 验证码
     */
    public String getIpCode(String ip){
        String code = "";

        if(redisTemplate.hasKey("ip_code:" + ip)){
            code = redisTemplate.opsForValue().get("ip_code:" + ip);
        }
        logger.info("获取到:"+ip+"的验证码为" + code);
       return code;
    }

    /**
     * 设置值
     * @param key
     * @param value
     */
    public void setKeyValue(String key,String value){
        redisTemplate.opsForValue().set(key,value);
    }
    /**
     * 设置值
     * @param key
     */
    public String getValue(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public boolean hasKey(String key){
        return redisTemplate.hasKey(key);
    }

    public void add(String key){
         redisTemplate.boundValueOps(key).increment(1);
    }





}
