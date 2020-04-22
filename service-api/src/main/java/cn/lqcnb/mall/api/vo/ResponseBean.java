package cn.lqcnb.mall.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author lqc520
 * @Description: test
 * @date 2020/4/4 23:01
 * @see cn.lqcnb.mall.api.vo
 */
@Data
@AllArgsConstructor
public class ResponseBean {
    //状态码
    private Integer code;
    //返回信息
    private String message;
    //返回的数据
    private Object data;

}
