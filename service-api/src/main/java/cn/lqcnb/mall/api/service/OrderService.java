package cn.lqcnb.mall.api.service;

import cn.lqcnb.mall.api.entity.Order;
import cn.lqcnb.mall.api.mapper.OrderMapper;
import cn.lqcnb.mall.common.service.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Author: Lin QiCheng
 * Date: 2019/8/26 16:50
 * To change this template use File | Settings | File Templates.
 * Description:
 * Modify by:
 */
@Service
public class OrderService extends AbstractService<Order> {
    @Resource
    private OrderMapper orderMapper;



    /**
     * 获取订单数据
     * @param map 查询参数
     * @return List<Map>
     */
    public List<Map> getOrder(Map map){
        return orderMapper.getOrder(map);
    }

    public List<Map> getOrderList(Order order){
        return orderMapper.getOrderList(order);
    }
}
