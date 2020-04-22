package cn.lqcnb.mall.api.controller;

import cn.lqcnb.mall.api.entity.WebSite;
import cn.lqcnb.mall.api.service.WebSiteService;
import cn.lqcnb.mall.common.entity.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lqc520
 * @Description: 网站设置
 * @date 2020/3/12 15:34
 */
@RestController
@Api(tags = "网站信息管理")
@CrossOrigin
@RequestMapping("api/website")
public class WebSiteController {

    @Autowired
    private WebSiteService webSiteService;

    @ApiOperation("获取网站设置信息")
    @GetMapping("get")
    public WebSite get(){
        return webSiteService.get();
    }

    @ApiOperation("修改网站网站设置信息")
    @PatchMapping("update")
    @ApiImplicitParam(name = "webSite" ,value = "网站信息",paramType="query",dataType = "website",required = true)
    public R update(WebSite webSite){
        if(webSiteService.update(webSite)){
            return R.ok();
        }
        return R.error();
    }
}
