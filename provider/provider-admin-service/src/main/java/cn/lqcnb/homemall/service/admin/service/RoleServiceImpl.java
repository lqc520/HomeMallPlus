package cn.lqcnb.homemall.service.admin.service;

import cn.lqcnb.homemall.api.admin.RoleService;
import cn.lqcnb.homemall.api.admin.dto.Role;
import cn.lqcnb.homemall.service.admin.mapper.RoleMapper;
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lqc520
 * @Description: 角色服务
 * @date 2020/3/19 17:00
 * @see cn.lqcnb.homemall.service.admin.service
 */
@Api("角色服务")
@Service(version = "1.0.0")
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleMapper roleMapper;
    /**
     * 获取角色信息
     *
     * @return List<Role>
     */
    @Override
    public List<Role> getList() {
        return roleMapper.selectAll();
    }

    /**
     * 获取用户信息
     *
     * @param role 查询条件
     * @return Role
     */
    @Override
    public Role getRole(Role role) {
        return roleMapper.selectOne(role);
    }

    /**
     * 获取用户角色
     *
     * @param uId 用户id
     * @return List<Role>
     */
    @Override
    public List<Role> getRole(Integer uId) {
        return roleMapper.getRole(uId);
    }

    /**
     * 更新角色信息
     *
     * @param role 用户信息
     * @return boolean
     */
    @Override
    public boolean update(Role role) {
        return roleMapper.updateByPrimaryKeySelective(role)>0;
    }

    /**
     * 删除某个角色
     *
     * @param id id
     * @return delete
     */
    @Override
    public boolean delete(Integer id) {
        return roleMapper.deleteByPrimaryKey(id)>0;
    }

    /**
     * 添加角色
     *
     * @param role 角色信息
     * @return boolean
     */
    @Override
    public boolean add(Role role) {
        return roleMapper.insertSelective(role)>0;
    }

    /**
     * 获取用户数量
     *
     * @return String
     */
    @Override
    public Integer getCount() {
        return roleMapper.selectCount(new Role());
    }


}
