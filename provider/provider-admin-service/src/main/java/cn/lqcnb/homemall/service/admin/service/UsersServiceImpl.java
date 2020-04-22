package cn.lqcnb.homemall.service.admin.service;

import cn.lqcnb.homemall.api.admin.UsersService;
import cn.lqcnb.homemall.api.admin.dto.Users;
import cn.lqcnb.homemall.service.admin.mapper.UsersMapper;
import cn.lqcnb.homemall.service.admin.service.fallback.UsersServiceFallback;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import io.swagger.annotations.Api;
import org.apache.commons.beanutils.BeanUtilsBean2;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lqc520
 * @Description: 管理员服务
 * @date 2020/3/17 0:16
 */
@Api("用户服务")
@Service(version = "1.0.0")
public class UsersServiceImpl implements UsersService {
    @Resource
    UsersMapper usersMapper;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<Users> getList() {
        return usersMapper.selectAll();
    }

    /**
     * 获取用户列表
     *
     * @return List<Map>
     */
    @Override
    public List<Map> getMapList() {
        return usersMapper.getList();
    }

    @Override
    public Users getById(Integer id) {
        return usersMapper.selectByPrimaryKey(id);
    }

    @Override
    public Users getUser(Users user) {
        return usersMapper.selectOne(user);
    }

/**
 * 熔断器的使用
 *
 * <p>
 * 1.  {@link SentinelResource#value()} 对应的是 Sentinel 控制台中的资源，可用作控制台设置【流控】和【降级】操作 <br>
 * 2.  {@link SentinelResource#fallback()} 对应的是 {@link UsersServiceFallback#getUsersByMobileFallback(String, Throwable)}，并且必须为 `static` <br>
 * 3. 如果不设置 {@link SentinelResource#fallbackClass()}，则需要在当前类中创建一个 `Fallback` 函数，函数签名与原函数一致或加一个 {@link Throwable} 类型的参数
 * </p>
 * */
    @Override
    @SentinelResource(value = "getUser", fallback = "getUsersByMobileFallback",fallbackClass = UsersServiceFallback.class)
    public Users getUserByMobile(String mobile) {
        Users param = new Users();
        param.setMobile(mobile);
        return usersMapper.selectOne(param);
    }

    /**
     * 获取用户信息
     *
     * @param mobile 手机号
     * @return Users
     */
    @Override
    public Users getUser(String mobile) {
        Example example =new Example(Users.class);
        example.createCriteria().andEqualTo("mobile", mobile);
        Users users = usersMapper.selectOneByExample(example);
        Users o = null;
        try {
            o = (Users)BeanUtilsBean2.getInstance().cloneBean(users);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        System.out.println(users);
        System.out.println(o);
        return o;
    }

    /**
     * 获取用户信息
     *
     * @param username 用户名
     * @return Users
     */
    @Override
    public Users getUserName(String username) {
        Users param = new Users();
        param.setMobile(username);
        return usersMapper.selectOne(param);
    }

    @Override
    public Map Mobile(String mobile) {
        Users param = new Users();
        param.setMobile(mobile);
        Users users = usersMapper.selectOne(param);
        Map m = new HashMap();
        m.put("mobile",users.getMobile());
        m.put("password",users.getPassword());
        return m;
    }


    @Override
    public boolean update(Users user) {
        return usersMapper.updateByPrimaryKeySelective(user)>0;
    }



    @Override
    public boolean delete(Integer id) {
        return usersMapper.deleteByPrimaryKey(id)>0;
    }

    @Override
    public boolean add(Users user) {
        //加密密码
        if(!StringUtils.isEmpty(user.getPassword())){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }else{
            user.setPassword(passwordEncoder.encode("123456"));
        }
        user.setState(1);
        user.setIsDelete(0);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        return usersMapper.insert(user)>0;
    }

    /**
     * 获取用户信息
     *
     * @param mobile 手机号
     * @return Map
     */
    @Override
    public Map getInfo(String mobile) {
        return usersMapper.getInfo(mobile);
    }

    /**
     * 获取用户数量
     *
     * @return String
     */
    @Override
    public Integer getCount() {
        return usersMapper.selectCount(new Users());
    }
}
