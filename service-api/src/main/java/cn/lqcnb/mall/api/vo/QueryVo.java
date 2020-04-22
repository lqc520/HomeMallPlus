package cn.lqcnb.mall.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author lqc520
 * @Description: 查询
 * @date 2020/4/6 0:54
 * @see cn.lqcnb.mall.api.vo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryVo {
    /**
     * 索引名
     */
    private String idxName;
    /**
     * 需要反射的实体类型，用于对查询结果的封装
     */
    private String className;
    /**
     * 具体条件
     */
    private Map<String, Map<String,Object>> query;
}
