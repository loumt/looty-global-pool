package cn.looty.example.策略工厂模式.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Filename: SysCountryEngageDataTableRow
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-07-30 10:54
 */
@Data
public class SysCountryEngageDataTableRow extends BaseTableRow {

    @ApiModelProperty("类型Id")
    private Long typeId;

    @ApiModelProperty("类型")
    private String typeName;

    @ApiModelProperty("等级Id")
    private Long typeLevelId;

    @ApiModelProperty("等级")
    private String typeLevelName;

    @ApiModelProperty("薪资")
    private Integer salary;

    @ApiModelProperty("职位等级")
    private String jobLevel;
}
