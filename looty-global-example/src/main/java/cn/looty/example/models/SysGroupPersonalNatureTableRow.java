package cn.looty.example.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Filename: SysGroupPersonalNatureTableRow
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-07-31 15:06
 */
@Data
public class SysGroupPersonalNatureTableRow extends BaseTableRow{
    @ApiModelProperty("版本Id")
    private Long versionId;
    @ApiModelProperty("群体类别")
    private Long groupId;
    @ApiModelProperty("人员性质")
    private Long natureId;

    @ApiModelProperty("公积金缴纳比例，百分比")
    private Integer housingFundPercent;

    @ApiModelProperty("社保类型")
    private Integer societyInsurance;

    @ApiModelProperty("简述")
    private String remark;

}
