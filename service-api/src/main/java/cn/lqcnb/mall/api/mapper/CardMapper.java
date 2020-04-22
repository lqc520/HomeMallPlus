package cn.lqcnb.mall.api.mapper;

import cn.lqcnb.mall.api.entity.Card;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


public interface CardMapper extends Mapper<Card> {

    /**
     * 查询商品信息
     * @param ids 商品id
     * @return List<Card>
     */
    public List<Card> getCards(String[] ids);

    /**
     * 获取用户购物车总价
     * @param memberId 用户id
     * @return sum
     */
    public double getSum(Integer memberId);
}