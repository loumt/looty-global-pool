package cn.looty.srv.user.model;

import cn.looty.common.base.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Classname SysUserRole
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/30 1:03
 */
@Data
@TableName("sys_user_role")
@ApiModel(description = "用户角色")
public class SysUserRole extends BaseModel {
    @ApiModelProperty("用户ID")
    @TableField
    private Long userId;

    @ApiModelProperty("角色Id")
    @TableField
    private Long roleId;
}
