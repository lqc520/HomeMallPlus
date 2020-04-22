package cn.lqcnb.mall.api.service;

import cn.lqcnb.mall.api.entity.Email;
import cn.lqcnb.mall.api.mapper.EmailMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
/**  
  * @Description: ${todo}
  * @author lqc520
  * @date 2020/3/12 23:30
  */
@Service
public class EmailService{

    @Resource
    private EmailMapper emailMapper;

    public Email get(){
        return emailMapper.selectByPrimaryKey(1);
    }

    public boolean update(Email email){
        return emailMapper.updateByPrimaryKeySelective(email)>0;
    }

}
