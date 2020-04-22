package cn.lqcnb.mall.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lqc520
 * @Description: test
 * @date 2020/4/6 0:17
 * @see cn.lqcnb.mall.api.entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElasticEntity {
    /**
     * 主键标识，用户ES持久化
     */
    private String id;

    /**
     * JSON对象，实际存储数据
     */
    private Goods data;
}
