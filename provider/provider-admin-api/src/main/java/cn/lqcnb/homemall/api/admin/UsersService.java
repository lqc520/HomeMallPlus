package cn.lqcnb.homemall.api.admin;

import cn.lqcnb.homemall.api.admin.dto.Users;

import java.util.List;
import java.util.Map;

/**
 * @author lqc520
 * @Description: test
 * @date 2020/3/21 2:21
 * @see cn.lqcnb.homemall.api.admin
 */
public interface UsersService {
    /**
     * 获取用户列表
     * @return  List<User>
     */
    List<Users> getList();

    /**
     * 获取用户列表
     * @return List<Map>
     */
    List<Map> getMapList();

    /**
     * 查询某个用户
     * @param id 用户id
     * @return User
     */
    Users getById(Integer id);

    /**
     * 获取用户信息
     * @param user 查询条件
     * @return Users
     */
    Users getUser(Users user);

    /**
     * 获取用户信息
     * @param mobile 手机号
     * @return Users
     */
    Users getUserByMobile(String mobile);

    /**
     * 获取用户信息
     * @param mobile 手机号
     * @return Users
     */
    Users getUser(String mobile);

    /**
     * 获取用户信息
     * @param username 用户名
     * @return Users
     */
    Users getUserName(String username);

    Map Mobile (String mobile);


    /**
     * 更新用户信息
     * @param user 用户信息
     * @return boolean
     */
    boolean update(Users user);



    /**
     * 删除某个用户
     * @param id id
     * @return delete
     */
    boolean delete(Integer id);

    /**
     * 添加用户
     * @param user 用户信息
     * @return boolean
     */
    boolean add(Users user);

    /**
     * 获取用户信息
     * @param mobile 手机号
     * @return Map
     */
    Map getInfo(String mobile);

    /**
     * 获取用户数量
     * @return String
     */
    Integer getCount();
}
