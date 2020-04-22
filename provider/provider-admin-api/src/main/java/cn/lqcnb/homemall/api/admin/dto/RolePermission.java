package cn.lqcnb.homemall.api.admin.dto;

import java.io.Serializable;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lqc520
 * @Description: ${todo}
 * @date 2020/4/1 14:35
 * @see cn.lqcnb.homemall.api.admin.dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sys_role_permission")
public class RolePermission implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 角色 ID
     */
    @Column(name = "role_id")
    private Long roleId;

    /**
     * 权限 ID
     */
    @Column(name = "permission_id")
    private Long permissionId;

    private static final long serialVersionUID = 1L;
}