package cn.looty.example.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Filename: SysSalarySubsidyDataTableGroup
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-07-30 11:22
 */
@Data
public class SysSalarySubsidyDataTableGroup extends BaseTableGroup<SysSalarySubsidyDataTableRow> {
    @ApiModelProperty("名称")
    private String name;
}
