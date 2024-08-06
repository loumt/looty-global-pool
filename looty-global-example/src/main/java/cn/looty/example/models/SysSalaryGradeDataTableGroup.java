package cn.looty.example.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Filename: SysSalaryGradeDataTableGroup
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-07-30 11:06
 */
@Data
public class SysSalaryGradeDataTableGroup extends BaseTableGroup<SysSalaryGradeDataTableRow> {
    @ApiModelProperty("名称")
    private String name;

}
