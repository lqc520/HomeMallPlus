package cn.lqcnb.mall.api.mapper;

import cn.lqcnb.mall.api.entity.Payment;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;

public interface PaymentMapper extends Mapper<Payment> {
    /**
     * 获取营销额度
     * @param date 时间
     * @return
     */
    public BigDecimal getSum(Integer date);
}