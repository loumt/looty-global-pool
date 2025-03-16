package cn.looty.srv.user.model;

import cn.looty.common.base.BaseModel;
import cn.looty.common.enums.ClientType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Classname SysRole
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/30 1:03
 */
@Data
@TableName("sys_role")
@ApiModel(description = "角色")
public class SysRole extends BaseModel {
    @ApiModelProperty("端")
    @TableField
    private ClientType clientType;

    @ApiModelProperty("名称")
    @TableField
    private String name;
}
