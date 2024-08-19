package cn.looty.example.生成PPTX.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ChartGridDto {
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "值")
    private String value;
}
