package cn.lqcnb.homemall.service.admin.service;

import cn.lqcnb.homemall.api.admin.PermissionService;
import cn.lqcnb.homemall.api.admin.dto.Permission;
import cn.lqcnb.homemall.service.admin.mapper.PermissionMapper;
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lqc520
 * @Description: 权限
 * @date 2020/3/20 10:55
 * @see cn.lqcnb.homemall.service.admin.service
 */
@Api("权限服务")
@Service(version = "1.0.0")
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private PermissionMapper permissionMapper;


    /**
     * 获取权限
     * @param uId 用户id
     * @return List<Permission>
     */
    @Override
    public List<Permission> getPermissionList(Integer uId) {
        return permissionMapper.getPermissionList(uId);
    }
}
