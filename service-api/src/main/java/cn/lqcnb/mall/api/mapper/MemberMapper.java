package cn.lqcnb.mall.api.mapper;

import cn.lqcnb.mall.api.entity.Member;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface MemberMapper extends Mapper<Member> {
    /**
     * 多条件查询用户
     *
     * @return
     */
    public List<Member> getList(Member member);

    /**
     * 获取用户增量
     *
     * @param date 时间
     * @return Map
     */
    public List<Map> getIncrement(@Param("date") Integer date, @Param("limit") Integer limit);
}