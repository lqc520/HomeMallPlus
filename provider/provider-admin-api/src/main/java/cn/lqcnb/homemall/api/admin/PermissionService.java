package cn.lqcnb.homemall.api.admin;

import cn.lqcnb.homemall.api.admin.dto.Permission;

import java.util.List;

/**
 * @author lqc520
 * @Description: 权限
 * @date 2020/3/19 17:24
 * @see cn.lqcnb.homemall.api.admin
 */
public interface PermissionService {

    /**
     * 获取权限
     * @param uId 用户id
     * @return List<Permission>
     */
    List<Permission> getPermissionList(Integer uId);

}
