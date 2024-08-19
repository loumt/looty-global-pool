package cn.looty.example.生成PPTX.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ChartDataDto {
    @ApiModelProperty(value = "日期[格式:yyyy/MM/dd]")
    private String date;
    @ApiModelProperty(value = "净值")
    private Double nw;
    @ApiModelProperty(value = "本周收益")
    private Double lr;
    @ApiModelProperty(value = "指数")
    private Double ind;
    @ApiModelProperty(value = "累计超额收益")
    private Double cer;
    @ApiModelProperty(value = "累计收益率")
    private Double cr;
}
