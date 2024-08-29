package cn.looty.srv.user.model;

import cn.looty.common.base.BaseModel;
import cn.looty.common.enums.PermissionClientType;
import cn.looty.common.enums.PermissionType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Classname SysPermission
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/30 1:03
 */
@Data
@TableName("sys_permission")
@ApiModel(description = "权限")
public class SysPermission extends BaseModel {
    @ApiModelProperty("端")
    @TableField
    private PermissionClientType clientType;

    @ApiModelProperty("类型")
    @TableField
    private PermissionType type;

    @ApiModelProperty("名称")
    @TableField
    private String name;

    @ApiModelProperty("权限")
    @TableField
    private String permission;

    @ApiModelProperty("描述")
    @TableField
    private String desc;

    @ApiModelProperty("排序")
    @TableField
    private Integer sort;

    @ApiModelProperty("父ID")
    @TableField
    private Long parentId;
}
