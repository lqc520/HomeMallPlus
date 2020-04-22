package cn.lqcnb.homemall.service.admin.service;

import cn.lqcnb.homemall.api.admin.UserRoleService;
import cn.lqcnb.homemall.api.admin.dto.UserRole;
import cn.lqcnb.homemall.service.admin.mapper.UserRoleMapper;
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @author lqc520
 * @Description: 用户角色服务
 * @date 2020/4/1 14:16
 * @see cn.lqcnb.homemall.service.admin.service
 */
@Api("用户角色服务")
@Service(version = "1.0.0")
public class UserRoleServiceImpl implements UserRoleService {

    @Resource
    UserRoleMapper userRoleMapper;
    /**
     * 添加用户角色
     * @return boolean
     */
    @Override
    public boolean add(UserRole userRole) {
        return userRoleMapper.insert(userRole)>0;
    }

    /**
     * 修改用户角色
     *
     * @param userRole 参数
     * @return boolean
     */
    @Override
    public boolean update(UserRole userRole) {
        Example example = new Example(UserRole.class);
        example.createCriteria().andEqualTo("userId",userRole.getUserId());
        return userRoleMapper.updateByExampleSelective(userRole,example)>0;
    }
}
