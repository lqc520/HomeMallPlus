package cn.lqcnb.homemall.service.profile.controller;

import cn.lqcnb.homemall.api.admin.RoleService;
import cn.lqcnb.homemall.api.admin.dto.Role;
import cn.lqcnb.homemall.commons.dto.LayUI;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lqc520
 * @Description: 角色服务
 * @date 2020/4/1 0:23
 * @see cn.lqcnb.homemall.service.profile.controller
 */
@RestController
@Api("角色模块")
@RequestMapping("role")
public class RoleController {
    @Reference(version = "1.0.0")
    private RoleService roleService;

    @ApiOperation("获取角色列表")
    @GetMapping("getList")
    public List<Role> getList(){
        return roleService.getList();
    }

    @ApiOperation("获取角色列表")
    @GetMapping("getRoleList")
    public LayUI getRoleList(){
        return LayUI.ok(roleService.getCount().toString(),roleService.getList());
    }
}
