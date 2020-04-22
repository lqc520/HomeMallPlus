package cn.lqcnb.homemall.service.profile.controller;


import cn.lqcnb.homemall.api.admin.UserRoleService;
import cn.lqcnb.homemall.api.admin.UsersService;
import cn.lqcnb.homemall.api.admin.dto.UserRole;
import cn.lqcnb.homemall.api.admin.dto.Users;
import cn.lqcnb.homemall.commons.dto.LayUI;
import cn.lqcnb.homemall.commons.dto.R;
import cn.lqcnb.homemall.commons.dto.ResponseResult;
import cn.lqcnb.homemall.commons.utils.UploadUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author lqc520
 * @Description: 用户信息服务
 * @date 2020/3/17 22:43
 */
@RestController
@RequestMapping(value = "profile")
@Api("用户模块")
public class UsersController {

    @Reference(version="1.0.0")
    private UsersService usersService;

    @Reference(version = "1.0.0")
    private UserRoleService userRoleService;


    @Resource
    public BCryptPasswordEncoder passwordEncoder;

    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 获取用户信息
     * @param mobile 手机号
     * @return ResponseResult<Users>
     */
    @GetMapping(value = "info/{mobile}")
    public ResponseResult<Users> info(@PathVariable String mobile) {
        Users users = usersService.getUserByMobile(mobile);
        logger.info("获取用户信息"+users);
        return new ResponseResult<Users>(ResponseResult.CodeStatus.OK, "获取个人信息", users);
    }


    @ApiOperation("获取用户信息")
    @ApiImplicitParam(name = "mobile", value = "手机号", paramType = "path",required = true)
    @GetMapping(value = "getInfo/{mobile}")
    public R getInfo(@PathVariable String mobile){
        System.out.println(mobile);
        return R.ok("获取用户信息成功",usersService.getInfo(mobile));
    }

    @ApiOperation("获取个人信息")
    @GetMapping(value = "getUserInfo")
    public ResponseResult<Users> getUserInfo() {
        // 获取认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users users = usersService.getUserByMobile(authentication.getName());
        logger.info("获取用户信息"+users);
        return new ResponseResult<Users>(ResponseResult.CodeStatus.OK, "获取个人信息", users);
    }

    @ApiOperation("echo")
    @GetMapping("echo")
    public String echo(){
        return "ok";
    }

    @ApiOperation("获取后台用户列表")
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping(value = "getList")
    public LayUI getList(){
        return LayUI.ok(usersService.getCount().toString(),usersService.getList());
    }


    /**
     * 获取后台用户信息
     * @return LayUI
     */
    @ApiOperation("获取后台用户列表")
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping(value = "getMapList")
    public LayUI getMapList(){
        return LayUI.ok(usersService.getCount().toString(),usersService.getMapList());
    }


    /**
     * 文件上传
     * @param file 文件
     * @return R
     */
    @PostMapping("/upload")
    public R upload(@RequestParam(value = "file") MultipartFile file){
        if(file.isEmpty()){
            return R.error();
        }
        //阿里云 linux云端部署上传到阿里云oss
        String AliPath = UploadUtils.upLoadOSS(file,"mall");
        return R.ok("ok", AliPath);
    }


    /**
     * 修改后台用户信息
     * @param users 用户
     * @return R
     */
    @Transactional
    @PatchMapping("update")
    public R update(Users users,Integer role){
        //修改用户信息
        if (usersService.update(users)) {
            UserRole userRole = new UserRole();
            userRole.setUserId((long)users.getId());
            userRole.setRoleId((long)role);
            if(userRoleService.update(userRole)){
                return R.ok();
            }
            return R.error("修改用户角色失败");
        }
        return R.error();
    }



    @GetMapping("resetPwd")
    @ApiOperation("重置密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile" ,value = "手机号",paramType="query",required = true),
            @ApiImplicitParam(name = "password" ,value = "密码",paramType="query",required = true),
    })
    public R resetPwd(String mobile,String password){
        Users user = usersService.getUser(mobile);
        if(user!=null){
            Users params = new Users();
            params.setId(user.getId());
            params.setPassword(password);
            if(usersService.update(params)){
                return R.ok();
            }
            return R.error();
        }
        return R.error("验证码错误");
    }



    /**
     * 修改密码
     * @param oldPassword 旧密码
     * @param password 新密码
     * @return R
     */
    @PatchMapping("updatePwd")
    public R updatePassword(String oldPassword,String password){
        // 获取认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String mobile = authentication.getName();
        Users users = usersService.getUser(mobile);
        if (users == null || !passwordEncoder.matches(oldPassword,users.getPassword())) {
            logger.error("用户："+mobile+"：密码错误");
            return R.error("密码错误");
        }
        Users param = new Users();
        param.setId(users.getId());
        param.setPassword(passwordEncoder.encode(password));
        if(usersService.update(param)){
            logger.error("用户："+mobile+"：密码修改成功");
            return R.ok();
        }
        return R.error();
    }


    @ApiOperation("添加")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "users",value = "用户信息",paramType="query",dataType = "users",required = true),
        @ApiImplicitParam(name = "role",value = "角色",paramType="query")
    })
    @PatchMapping("add")
    @Transactional
    public R add(Users users,String role){
        users.setIsDelete(0);
        users.setCreateTime(new Date());
        users.setUpdateTime(new Date());
        if(usersService.add(users)){
//            查询用户id
            Users user = usersService.getUser(users.getMobile());
            UserRole userRole = new UserRole();
            userRole.setUserId(Long.parseLong(user.getId().toString()));
            if(role.isEmpty() || "-1".equals(role)){
                userRole.setRoleId(Long.parseLong("38"));
            }else{
                userRole.setRoleId(Long.parseLong(role));
            }
//            添加用户角色
            userRoleService.add(userRole);
            return R.ok();
        }
        return R.error();
    }
















}
