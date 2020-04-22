package cn.lqcnb.mall.api.mapper;

import cn.lqcnb.mall.api.entity.Order;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface OrderMapper extends Mapper<Order> {
    /**
     * 获取订单信息
     * @param map 查询条件
     * @return List<Map>
     */
    public List<Map> getOrder(Map map);

    /**
     * 获取订单信息
     * @param order 查询条件
     * @return List<Map>
     */
    public List<Map> getOrderList(Order order);
}