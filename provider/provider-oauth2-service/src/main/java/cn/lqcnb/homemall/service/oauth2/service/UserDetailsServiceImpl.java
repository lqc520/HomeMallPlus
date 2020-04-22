package cn.lqcnb.homemall.service.oauth2.service;


import cn.lqcnb.homemall.api.admin.RoleService;
import cn.lqcnb.homemall.api.admin.UsersService;
import cn.lqcnb.homemall.api.admin.dto.Role;
import cn.lqcnb.homemall.api.admin.dto.Users;
import com.google.common.collect.Lists;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义认证
 * <p>
 * Description:
 * </p>
 *
 * @author lqc520
 * @version v1.0.0
 * @date 2019-07-29 13:28:10

 *
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "$2a$10$WhCuqmyCsYdqtJvM0/J4seCU.xZQHe2snNE5VFUuBGUZWPbtdl3GG";
//    @Override
//    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//        List<GrantedAuthority> grantedAuthorities = Lists.newArrayList();
//        return new User(USERNAME, PASSWORD, grantedAuthorities);
//    }

    Logger logger = LoggerFactory.getLogger(getClass());

    @Reference(version = "1.0.0")
    private UsersService usersService;

    @Reference(version = "1.0.0")
    private RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {
        // 查询用户
        Users admin = usersService.getUserByMobile(mobile);

        // 用户授权uses
        List<GrantedAuthority> grantedAuthorities = Lists.newArrayList();

//        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("user");
//        grantedAuthorities.add(grantedAuthority);

        if(admin != null){
            List<Role> roleList = roleService.getRole(admin.getId());
            roleList.forEach(role -> {
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getEnname());
                grantedAuthorities.add(grantedAuthority);
            });
        }
        assert admin != null;
        return new User(admin.getMobile(), admin.getPassword(), grantedAuthorities);
    }

}
