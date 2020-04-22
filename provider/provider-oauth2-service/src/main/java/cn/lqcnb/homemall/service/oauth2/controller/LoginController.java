package cn.lqcnb.homemall.service.oauth2.controller;

import cn.lqcnb.homemall.api.admin.UsersService;
import cn.lqcnb.homemall.api.admin.dto.Users;
import cn.lqcnb.homemall.commons.dto.LayUI;
import cn.lqcnb.homemall.commons.dto.R;
import cn.lqcnb.homemall.commons.dto.ResponseResult;
import cn.lqcnb.homemall.commons.utils.IpUtils;
import cn.lqcnb.homemall.commons.utils.MapperUtils;
import cn.lqcnb.homemall.commons.utils.OkHttpClientUtil;
import cn.lqcnb.homemall.commons.utils.TencentSMSUtils.SMSUtils;
import cn.lqcnb.homemall.service.oauth2.service.ProfileFeign;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import okhttp3.Response;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

//import cn.lqcnb.homemall.service.oauth2.service.ProfileFeign;

/**
 * @author lqc520
 * @Description: 登录控制
 * @date 2020/3/17 10:56
 */
@RestController
@RequestMapping("user")
@Api("用户认证")
public class LoginController {
    private static final String URL_OAUTH_TOKEN = "http://localhost:8088/oauth/token";

    @Value("${provider.oauth2.grant_type}")
    public String oauth2GrantType;

    @Value("${provider.oauth2.client_id}")
    public String oauth2ClientId;

    @Value("${provider.oauth2.client_secret}")
    public String oauth2ClientSecret;

    @Resource(name = "userDetailsServiceBean")
    public UserDetailsService userDetailsService;

    @Resource
    HttpServletRequest request;

    @Resource
    public BCryptPasswordEncoder passwordEncoder;

    @Resource
    public TokenStore tokenStore;

    @Resource
    private ProfileFeign profileFeign;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Reference(version = "1.0.0")
    private UsersService usersService;


    Logger logger = LoggerFactory.getLogger(getClass());


    @ApiOperation("登录")
    @PostMapping("login")
    @ApiImplicitParam(name = "users",value="用户信息",paramType = "query",dataType = "users")
    public ResponseResult<Map<String, Object>> login(Users users){
        logger.info(users.getMobile()+":"+users.getPassword());
        // 封装返回的结果集
        Map<String, Object> result = Maps.newHashMap();
        // 验证密码是否正确
        UserDetails userDetails = userDetailsService.loadUserByUsername(users.getMobile());
       //判断用户信息
        if (userDetails == null || !passwordEncoder.matches(users.getPassword(), userDetails.getPassword())) {
            return new ResponseResult<Map<String, Object>>(ResponseResult.CodeStatus.ILLEGAL_REQUEST, "账号或密码错误", null);
        }
        // 通过 HTTP 客户端请求登录接口
        Map<String, String> params = Maps.newHashMap();
        params.put("username", users.getMobile());
        params.put("password", users.getPassword());
        params.put("grant_type", oauth2GrantType);
        params.put("client_id", oauth2ClientId);
        params.put("client_secret", oauth2ClientSecret);
        try {
            Response response = OkHttpClientUtil.getInstance().postData(URL_OAUTH_TOKEN, params);
            String jsonString = Objects.requireNonNull(response.body()).string();
            Map<String, Object> jsonMap = MapperUtils.json2map(jsonString);
            logger.info(jsonMap.toString());
            String token = String.valueOf(jsonMap.get("access_token"));
            if(token.isEmpty()){
                result.put("token", "熔断降流！！！");
            }else{
                result.put("token", token);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseResult<Map<String, Object>>(ResponseResult.CodeStatus.OK, "登录成功", result);
    }



//        logger.info("getName: "+authentication.getName());
//        logger.info("getAuthorities"+authentication.getAuthorities());
//        logger.info("getDetails"+authentication.getDetails());
    @ApiOperation("获取用户详细信息")
    @PreAuthorize("hasAnyAuthority('user','admin')")
    @GetMapping("info")
    public ResponseResult<Users> info() throws Exception {
        // 获取认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 获取个人信信息
        String jsonString = profileFeign.info(authentication.getName());
        Users umsAdmin = MapperUtils.json2pojoByTree(jsonString, "data", Users.class);
        // 如果触发熔断则返回熔断结果
        if (umsAdmin == null) {
            return MapperUtils.json2pojo(jsonString, ResponseResult.class);
        }
        // 封装并返回结果
        logger.info(umsAdmin.toString());
        return new ResponseResult<Users>(ResponseResult.CodeStatus.OK, "获取用户信息", umsAdmin);
    }



    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("echo")
    public String echo(){
        return profileFeign.echo();
    }


    /**
     * 注销
     *
     * @return {@link ResponseResult}
     */
    @ApiOperation("退出登录")
    @ApiImplicitParam(name = "access_token" ,value = "token",paramType = "query")
    @GetMapping("logout")
    public ResponseResult<Void> logout(String access_token) {
        // 删除 token 以注销
        tokenStore.removeAccessToken(tokenStore.readAccessToken(access_token));
        return new ResponseResult<Void>(ResponseResult.CodeStatus.OK, "用户已注销");
    }


    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("getList")
    public LayUI getList(){
        return profileFeign.getList();
    }



    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("getMapList")
    public LayUI getMapList(){
        return profileFeign.getMapList();
    }


    @PostMapping("updatePwd")
    public R updatePassword(String oldPassword,String password){
        ResponseResult<Users> info = null;
        try {
            info = info();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert info != null;
        Users users = info.getData();
        System.out.println(users);
        if (users == null || !passwordEncoder.matches(oldPassword,users.getPassword())) {
            return R.error("密码错误");
        }
        Users param = new Users();
        param.setId(users.getId());
        param.setPassword(passwordEncoder.encode(password));
        System.out.println(param);
        return profileFeign.update(param);
    }


    @ApiOperation(value = "获取腾讯云短信注册验证码")
    @ApiImplicitParam(name = "mobile" ,value = "手机号",paramType="query",required = true)
    @GetMapping("getRegisterCode")
    public R getRegisterCode(String mobile){
        String code = SMSUtils.getRegisterCode(mobile);
        if (code!=null){
            stringRedisTemplate.opsForValue().set("mobile_code:"+mobile,code);
            stringRedisTemplate.expire("mobile_code:"+mobile,300, TimeUnit.SECONDS);
            return R.ok("验证码已发送 5分钟内有效",code);
        }
        return R.error("获取短信验证码错误");
    }


    @ApiOperation(value = "获取腾讯云短信重置验证码")
    @ApiImplicitParam(name = "mobile" ,value = "手机号",paramType="query",required = true)
    @GetMapping("getResetCode")
    public R getResetCode(String mobile){
        String code = SMSUtils.getReSetCode(mobile);
        if (code!=null){
            stringRedisTemplate.opsForValue().set("mobile_code:"+mobile,code);
            stringRedisTemplate.expire("mobile_code:"+mobile,300, TimeUnit.SECONDS);
            return R.ok("验证码已发送 5分钟内有效",code);
        }
        return R.error("获取短信验证码错误");
    }


    @GetMapping("reset")
    @ApiOperation("重置验证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile" ,value = "手机号",paramType="query",required = true),
            @ApiImplicitParam(name = "code" ,value = "验证码",paramType="query",required = true)
    })
    public R reset(String mobile,String code) {
        String SMSCode = stringRedisTemplate.opsForValue().get("mobile_code:" + mobile);
        if (code.equals(SMSCode)) {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            stringRedisTemplate.opsForValue().set("uuid:"+ IpUtils.getIpAddr(request),uuid,300,TimeUnit.SECONDS);
            Map map = new HashMap();
            map.put("uuid",uuid);
            map.put("mobile",mobile);
            return R.ok("重置成功",map);
        }
        return R.error("验证码错误");
    }


    @PatchMapping("resetPwd")
    public R resetPwd(String password,String uuid,String mobile){
        String cheUuid = stringRedisTemplate.opsForValue().get("uuid:"+IpUtils.getIpAddr(request));
        if(uuid.equals(cheUuid)){
            Users user = usersService.getUser(mobile);
            if(user!=null){
                Users params = new Users();
                params.setId(user.getId());
                params.setPassword(passwordEncoder.encode(password));
                if(usersService.update(params)){
                    return R.ok();
                }
                return R.error();
            }
            return R.error("验证码错误");
        }
        return R.error("验证码错误");
    }


    @ApiOperation(value = "检测手机号是否注册")
    @ApiImplicitParam(name = "mobile" ,value = "手机号",paramType="path")
    @GetMapping("checkMobile/{mobile}")
    public R checkMobile(@PathVariable String mobile){
        Users params = new Users();
        params.setMobile(mobile);
        Users one = usersService.getUser(mobile);
        if(one==null){
            return R.error("您的手机号未注册 将进行注册");
        }else{
            return R.ok();
        }
    }


    @PatchMapping("req")
    @ApiOperation("注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user" ,value = "用户对象",paramType="query"),
            @ApiImplicitParam(name = "code" ,value = "验证码",paramType="query",required = true)
    })
    public R req(Users user,String code){
        String SMSCode =stringRedisTemplate.opsForValue().get("mobile_code:"+user.getMobile());
        if(code.equals(SMSCode)){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            if(usersService.add(user)){
                return R.ok("注册成功");
            }
            return R.error();

        }else{
            return R.error("验证码错误");
        }
    }









    //    @ApiOperation("重置密码")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "mobile" ,value = "手机号",paramType="query",required = true),
//            @ApiImplicitParam(name = "password" ,value = "密码",paramType="query",required = true),
//    })
//    @GetMapping("resetPwd")
//    public R resetPwd(String mobile,String password){
//        return profileFeign.resetPwd(mobile,password);
//    }

}
