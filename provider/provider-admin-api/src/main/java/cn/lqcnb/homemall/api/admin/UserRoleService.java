package cn.lqcnb.homemall.api.admin;

import cn.lqcnb.homemall.api.admin.dto.UserRole;

/**
 * @author lqc520
 * @Description: 用户角色接口
 * @date 2020/4/1 14:16
 * @see cn.lqcnb.homemall.api.admin
 */
public interface UserRoleService {

    /**
     * 添加用户角色
     * @return boolean
     */
    boolean add(UserRole userRole);

    /**
     * 修改用户角色
     * @param userRole 参数
     * @return boolean
     */
    boolean update(UserRole userRole);

}
