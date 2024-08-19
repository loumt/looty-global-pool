package cn.looty.example.生成PPTX.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WeeklyStrategyDto {
    @ApiModelProperty(value = "周策略名称")
    private String wn;
    @ApiModelProperty(value = "周策略值")
    private Double wv;
}
