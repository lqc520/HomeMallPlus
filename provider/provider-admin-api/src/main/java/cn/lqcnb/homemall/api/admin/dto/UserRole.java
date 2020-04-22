package cn.lqcnb.homemall.api.admin.dto;

import lombok.AllArgsConstructor;
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
 * @date 2020/4/1 14:35
 * @see cn.lqcnb.homemall.api.admin.dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sys_user_role")
public class UserRole implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 用户 ID
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 角色 ID
     */
    @Column(name = "role_id")
    private Long roleId;

    private static final long serialVersionUID = 1L;
}