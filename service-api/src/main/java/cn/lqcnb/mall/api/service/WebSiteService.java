package cn.lqcnb.mall.api.service;

import cn.lqcnb.mall.api.entity.WebSite;
import cn.lqcnb.mall.api.mapper.WebSiteMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lqc520
 * @Description: ${todo}
 * @date 2020/3/12 16:06
 */
@Service
public class WebSiteService {

    @Resource
    private WebSiteMapper webSiteMapper;

    public WebSite get() {
        return webSiteMapper.selectAll().get(0);
    }

    public boolean update(WebSite webSite) {
        return webSiteMapper.updateByPrimaryKeySelective(webSite) > 0;
    }

}

