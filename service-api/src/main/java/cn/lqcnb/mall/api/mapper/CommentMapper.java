package cn.lqcnb.mall.api.mapper;

import cn.lqcnb.mall.api.entity.Comment;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface CommentMapper extends Mapper<Comment> {
    /**
     * 获取商品评论信息
     * @param gid 商品id
     * @return List<Map>
     */
    @Select("SELECT * FROM `mall_comment` c,mall_member m where c.member_id=m.id and goods_id=#{gid}")
    public List<Map> getListByGId(Integer gid);
}