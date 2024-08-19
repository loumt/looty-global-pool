package cn.looty.example.生成PPTX.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ProductBenefitLineDto {
    @ApiModelProperty(value = "产品名称")
    private String productName;
    @ApiModelProperty(value = "表格显示项目")
    private List<ChartGridDto> gridItems;
    @ApiModelProperty(value = "净值数据")
    private List<ChartDataDto> netWorthArr;
    @ApiModelProperty(value = "特别提醒")
    private List<String> warns;
}
