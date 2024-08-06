package cn.looty.example.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Filename: SysSalarySenioritySubsidyTableRow
 * @Description: 为更新新建的冗余类
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-07-30 15:58
 */
@Data
public class SysSalarySenioritySubsidyTableRow extends BaseTableRow {
    @ApiModelProperty("最大计算工龄")
    private Integer max;

    @ApiModelProperty("每年补贴额度")
    private Integer amount;

    @ApiModelProperty("上限值")
    private Integer upLimit;
}
