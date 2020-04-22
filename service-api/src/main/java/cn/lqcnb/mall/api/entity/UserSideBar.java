package cn.lqcnb.mall.api.entity;

import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;

/**  
  * @Description: ${todo}
  * @author lqc520
  * @date 2020/3/5 12:37
  */
@Data
@Table(name = "mall_user_side_bar")
public class UserSideBar implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    @Column(name = "`name`")
    private String name;

    @Column(name = "url")
    private String url;

    private static final long serialVersionUID = 1L;
}