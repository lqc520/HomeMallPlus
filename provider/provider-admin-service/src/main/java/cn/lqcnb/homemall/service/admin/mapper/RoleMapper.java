package cn.lqcnb.homemall.service.admin.mapper;

import cn.lqcnb.homemall.api.admin.dto.Role;
import tk.mybatis.mapper.MyMapper;

import java.util.List;

/**  
  * @Description: ${todo}
  * @author lqc520
  * @date 2020/3/19 16:54
  * @see cn.lqcnb.homemall.service.admin.mapper
  */
public interface RoleMapper extends MyMapper<Role> {
    /**
     * 获取用户角色
     * @param uId 用户id
     * @return  List<Role>
     */
    List<Role> getRole(Integer uId);
}