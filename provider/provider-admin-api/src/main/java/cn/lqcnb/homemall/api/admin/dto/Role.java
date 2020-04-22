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
  * @Description: ${todo}
  * @author lqc520
  * @date 2020/3/19 16:54
  * @see cn.lqcnb.homemall.api.admin.dto
  */
@Data
@NoArgsConstructor
@Table(name = "sys_role")
public class Role implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 父角色
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 角色名称
     */
    @Column(name = "`name`")
    private String name;

    /**
     * 角色英文名称
     */

    @Column(name = "enname")
    private String enname;

    /**
     * 备注
     */
    @Column(name = "description")
    private String description;

    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name = "created")
    private Date created;

    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name = "updated")
    private Date updated;

    private static final long serialVersionUID = 1L;
}