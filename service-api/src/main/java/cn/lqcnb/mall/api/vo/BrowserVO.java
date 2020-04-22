package cn.lqcnb.mall.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lqc520
 * @Description: 浏览器
 * @date 2020/3/10 19:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrowserVO implements Serializable {
    String name;
    Integer value;
}
