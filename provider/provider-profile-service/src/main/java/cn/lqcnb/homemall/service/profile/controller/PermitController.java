package cn.lqcnb.homemall.service.profile.controller;

import cn.lqcnb.homemall.api.admin.UsersService;
import cn.lqcnb.homemall.api.admin.dto.Users;
import cn.lqcnb.homemall.commons.dto.R;
import cn.lqcnb.homemall.commons.utils.TencentSMSUtils.SMSUtils;
import cn.lqcnb.homemall.service.profile.utils.RedisSMSUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author lqc520
 * @Description: 允许通过
 * @date 2020/4/4 11:21
 * @see cn.lqcnb.homemall.service.profile.controller
 */
@RequestMapping("permit")
@RestController
public class PermitController {

    @Resource
    private RedisSMSUtil redisSMSUtil;

    @Resource
    public BCryptPasswordEncoder passwordEncoder;

    @Reference(version="1.0.0")
    private UsersService usersService;


    @ApiOperation(value = "获取腾讯云短信注册验证码")
    @ApiImplicitParam(name = "mobile" ,value = "手机号",paramType="path",required = true)
    @GetMapping("getRegisterCode/{mobile}")
    public R getRegisterCode(@PathVariable String mobile){
        String code = SMSUtils.getRegisterCode(mobile);
        if (code!=null){
            redisSMSUtil.setCodeTime(mobile,code,300);
//            stringRedisTemplate.opsForValue().set("mobile_code:"+mobile,code);
//            stringRedisTemplate.expire("mobile_code:"+mobile,300, TimeUnit.SECONDS);
            return R.ok("验证码已发送 5分钟内有效",code);
        }
        return R.error("获取短信验证码错误");
    }


    @PatchMapping("req")
    @ApiOperation("注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user" ,value = "用户对象",paramType="query"),
            @ApiImplicitParam(name = "code" ,value = "验证码",paramType="query",required = true)
    })
    public R req(Users user, String code){
        String smsCode = redisSMSUtil.getCode(user.getMobile());
//        String SMSCode =stringRedisTemplate.opsForValue().get("mobile_code:"+user.getMobile());
        if(code.equals(smsCode)){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            if(usersService.add(user)){
                return R.ok("注册成功");
            }
            return R.error();

        }else{
            return R.error("验证码错误");
        }
    }


    @ApiOperation("获取加密密码")
    @ApiImplicitParam(name = "password",value = "密码",paramType = "query",required = true)
    @GetMapping("getPassWord")
    public String getPassWord(String password){
        return passwordEncoder.encode(password);
    }
}
