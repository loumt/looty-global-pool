package cn.looty.example.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Filename: SysSalarySenioritySubsidyTable
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-07-29 17:19
 */
@Data
public class SysSalarySenioritySubsidyTable extends BaseTable {
    @ApiModelProperty("最大计算工龄")
    private Integer max;

    @ApiModelProperty("每年补贴额度")
    private Integer amount;

    @ApiModelProperty("上限值")
    private Integer limit;
}
