package cn.lqcnb.homemall.service.admin.mapper;

import cn.lqcnb.homemall.api.admin.dto.Users;
import tk.mybatis.mapper.MyMapper;

import java.util.List;
import java.util.Map;

/**
 * @author lqc520
 * @Description: ${todo}
 * @date 2020/3/21 11:03
 * @see cn.lqcnb.homemall.service.admin.mapper
 */
public interface UsersMapper extends MyMapper<Users> {
    /**
     * 获取用户详细信息
     *
     * @param mobile 手机号
     * @return map
     */
    Map getInfo(String mobile);

    /**
     * 获取用户列表
     * @return List<Map>
     */
    List<Map> getList();
}