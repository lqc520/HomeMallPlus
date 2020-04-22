package cn.lqcnb.homemall.consumer.admin.controller;

import cn.lqcnb.homemall.api.admin.UsersService;
import cn.lqcnb.homemall.api.admin.dto.Users;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lqc520
 * @Description: 管理员
 * @date 2020/3/17 1:35
 */
@RestController
public class AdminController {
    @Reference(version = "1.0.0")
    private UsersService usersService;

    @GetMapping("test")
    public List<Users> test(){
        return usersService.getList();
    }
}
