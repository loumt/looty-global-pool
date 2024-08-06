package cn.looty.example.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Filename: SysSalarySubsidyDataTableRow
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-07-30 10:54
 */
@Data
public class SysSalarySubsidyDataTableRow extends BaseTableRow {

    @ApiModelProperty("类型Id")
    private Long typeId;

    @ApiModelProperty("类型")
    private String typeName;

    @ApiModelProperty("等级Id")
    private Long typeLevelId;

    @ApiModelProperty("等级")
    private String typeLevelName;

    @ApiModelProperty("岗位津贴")
    private Integer salarySubsidy;

    @ApiModelProperty("生活补贴")
    private Integer liveSubsidy;

}
