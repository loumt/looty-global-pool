package cn.looty.srv.user.model;

import cn.looty.common.base.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Classname SysToken
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/12/1 16:52
 */
@Data
@TableName("sys_token")
@ApiModel(description = "Token")
public class SysToken extends BaseModel {

    @ApiModelProperty("用户Id")
    @TableField
    private Long userId;

    @ApiModelProperty("TOKEN")
    @TableField
    private String token;

}
