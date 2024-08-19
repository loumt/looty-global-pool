package cn.looty.example.策略工厂模式.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Filename: SysProfessionTableRow
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-07-31 09:29
 */
@Data
public class SysProfessionTableRow extends BaseTableRow {
    @ApiModelProperty("类型Id")
    private Long typeId;
    @ApiModelProperty("类型名")
    private String typeName;
    @ApiModelProperty("类型Id")
    private Long typeLevelId;
    @ApiModelProperty("类型等级名")
    private String typeLevelName;
}
