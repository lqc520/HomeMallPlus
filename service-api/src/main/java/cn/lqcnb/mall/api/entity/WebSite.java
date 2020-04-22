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
 * @date 2020/3/12 16:12
 */
@Data
@NoArgsConstructor
@Table(name = "mall_website")
public class WebSite implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    @Column(name = "sitename")
    private String sitename;

    @Column(name = "`domain`")
    private String domain;

    @Column(name = "`cache`")
    private String cache;

    @Column(name = "title")
    private String title;

    @Column(name = "keywords")
    private String keywords;

    @Column(name = "descript")
    private String descript;

    @Column(name = "copyright")
    private String copyright;

    private static final long serialVersionUID = 1L;
}