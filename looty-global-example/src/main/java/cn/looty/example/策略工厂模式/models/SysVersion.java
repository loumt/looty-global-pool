package cn.looty.example.策略工厂模式.models;

import cn.looty.common.enums.YesOrNo;
import cn.looty.example.策略工厂模式.common.BaseEntity;
import cn.looty.example.策略工厂模式.enums.SysVersionTypeEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Filename: SysVersion
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-07-29 15:39
 */
@Data
@ApiModel(description = "版本控制")
@TableName("sys_version")
public class SysVersion extends BaseEntity {

    @ApiModelProperty("id")
    @TableField
    private Long id;
    @ApiModelProperty("名")
    @TableField
    private String name;
    @ApiModelProperty("类型")
    @TableField
    private SysVersionTypeEnum type;
    @ApiModelProperty("当前版本")
    @TableField
    private YesOrNo isCurrent;

}
