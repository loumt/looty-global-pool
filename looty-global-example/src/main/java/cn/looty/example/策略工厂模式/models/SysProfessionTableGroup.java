package cn.looty.example.策略工厂模式.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Filename: SysProfessionTableGroup
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-07-31 09:31
 */
@Data
public class SysProfessionTableGroup extends BaseTableGroup<SysProfessionTableRow>{
    @ApiModelProperty("类型名")
    private String typeName;
}
