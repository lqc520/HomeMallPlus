package cn.lqcnb.homemall.api.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author lqc520
 * @Description: ${todo}
 * @date 2020/3/21 11:03
 * @see cn.lqcnb.homemall.api.admin.dto
 */
@Data
@NoArgsConstructor
@Table(name = "sys_user")
public class Users implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 用户名
     */
    @Column(name = "mobile")
    private String mobile;

    @Column(name = "`password`")
    private String password;

    @Column(name = "email")
    private String email;

    /**
     * 0 激活 1 未激活
     */
    @Column(name = "`state`")
    private Integer state;

    /**
     * 头像
     */
    @Column(name = "avatar")
    private String avatar;

    @Column(name = "nickname")
    private String nickname;

    /**
     * 0 正常   1删除
     */
    @Column(name = "is_delete")
    private Integer isDelete;

    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name = "create_time")
    private Date createTime;

    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name = "update_time")
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}