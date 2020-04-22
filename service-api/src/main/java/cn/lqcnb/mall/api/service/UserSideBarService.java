package cn.lqcnb.mall.api.service;

import cn.lqcnb.mall.api.entity.UserSideBar;
import cn.lqcnb.mall.api.mapper.UserSideBarMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
  * @Description: 用户侧栏服务
  * @author lqc520
  * @date 2020/3/5 12:37
  */
@Service
public class UserSideBarService{

    @Resource
    private UserSideBarMapper userSideBarMapper;

    /**
     * 获取全部数据
     * @return
     */
    public List<UserSideBar> getList(){
        return userSideBarMapper.selectAll();
    }

}
