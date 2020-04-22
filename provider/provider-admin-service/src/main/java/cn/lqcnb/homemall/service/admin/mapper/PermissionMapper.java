package cn.lqcnb.homemall.service.admin.mapper;

import cn.lqcnb.homemall.api.admin.dto.Permission;
import tk.mybatis.mapper.MyMapper;

import java.util.List;

/**  
  * @Description: ${todo}
  * @author lqc520
  * @date 2020/3/19 17:23
  * @see cn.lqcnb.homemall.service.admin.mapper
  */
public interface PermissionMapper extends MyMapper<Permission> {
    /**
     * 获取权限信息
     * @param uId 用户id
     * @return List<Permission>
     */
    List<Permission> getPermissionList(Integer uId);
}