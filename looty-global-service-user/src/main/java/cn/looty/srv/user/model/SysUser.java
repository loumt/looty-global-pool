package cn.looty.srv.user.model;

import cn.looty.common.base.BaseModel;
import cn.looty.common.enums.YesOrNo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Filename: SysUser
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: looty_loumt@hotmail.com
 * @Date: 2024-08-20 14:24
 */
@Data
@TableName("sys_user")
@ApiModel(description = "系统用户")
public class SysUser extends BaseModel {
    @ApiModelProperty("昵称")
    @TableField
    private String nickName;

    @ApiModelProperty("账户")
    @TableField
    private String username;

    @ApiModelProperty("密码")
    @TableField
    private String password;

    @ApiModelProperty("是否禁用")
    @TableField
    private YesOrNo isDisable;
}
