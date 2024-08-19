package cn.looty.example.策略工厂模式.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Filename: SysCountryEngageDataTableGroup
 * @Description: 国聘Table-Group
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-07-30 11:05
 */
@Data
public class SysCountryEngageDataTableGroup extends BaseTableGroup<SysCountryEngageDataTableRow> {
    @ApiModelProperty("名称")
    private String name;

}
