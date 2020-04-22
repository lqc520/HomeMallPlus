package cn.lqcnb.mall.api.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author lqc520
 * @Description: ${todo}
 * @date 2020/3/12 23:30
 */
@Data
@NoArgsConstructor
@Table(name = "mall_email")
public class Email implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    @Column(name = "`host`")
    private String host;

    @Column(name = "username")
    private String username;

    @Column(name = "`password`")
    private String password;

    @Column(name = "port")
    private String port;

    private static final long serialVersionUID = 1L;
}