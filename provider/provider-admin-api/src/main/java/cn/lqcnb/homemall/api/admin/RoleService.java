package cn.lqcnb.homemall.api.admin;

import cn.lqcnb.homemall.api.admin.dto.Role;

import java.util.List;

/**
 * @author lqc520
 * @Description: 角色管理
 * @date 2020/3/19 16:56
 * @see cn.lqcnb.homemall.api.admin
 */
public interface RoleService {

    /**
     * 获取角色信息
     * @return List<Role>
     */
    List<Role> getList();


    /**
     * 获取用户信息
     * @param role 查询条件
     * @return Role
     */
    Role getRole(Role role);

    /**
     * 获取用户角色
     * @param uId 用户id
     * @return  List<Role>
     */
    List<Role> getRole(Integer uId);


    /**
     * 更新角色信息
     * @param role 用户信息
     * @return boolean
     */
    boolean update(Role role);

    /**
     * 删除某个角色
     * @param id id
     * @return delete
     */
    boolean delete(Integer id);

    /**
     * 添加角色
     * @param role 角色信息
     * @return boolean
     */
    boolean add(Role role);

    /**
     * 获取用户数量
     * @return String
     */
    Integer getCount();

}
