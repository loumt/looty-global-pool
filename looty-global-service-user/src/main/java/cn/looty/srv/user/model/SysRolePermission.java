package cn.looty.srv.user.model;

import cn.looty.common.base.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Classname SysRolePermission
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/30 1:03
 */
@Data
@TableName("sys_role_permission")
@ApiModel(description = "角色权限")
public class SysRolePermission extends BaseModel {
    @ApiModelProperty("权限ID")
    @TableField
    private Long permissionId;

    @ApiModelProperty("角色Id")
    @TableField
    private Long roleId;

}
