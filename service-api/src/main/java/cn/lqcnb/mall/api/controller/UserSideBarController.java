package cn.lqcnb.mall.api.controller;
import cn.lqcnb.mall.api.entity.UserSideBar;
import cn.lqcnb.mall.api.service.UserSideBarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lqc520
 * @Description: 用户模块 侧边栏
 * @date 2020/3/5 12:23
 */
@RestController
@Api(tags = "用户侧边栏模块")
@CrossOrigin
@RequestMapping("api/userSide")
public class UserSideBarController {

    @Autowired
    private UserSideBarService userSideBarService;

    @GetMapping("getList")
    @ApiOperation("获取侧边栏信息")
    public List<UserSideBar> getList(){
        return userSideBarService.getList();
    }
}
