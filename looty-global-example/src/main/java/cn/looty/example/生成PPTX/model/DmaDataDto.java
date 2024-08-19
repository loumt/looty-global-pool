package cn.looty.example.生成PPTX.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DmaDataDto {
    @ApiModelProperty(value = "日期[格式:yyyy/MM/dd]")
    private String date;
    @ApiModelProperty(value = "dma1号的值")
    private Double dma1;
    @ApiModelProperty(value = "dma1号的值")
    private Double dma2;

    public DmaDataDto(String date, Double dma1, Double dma2) {
        this.date = date;
        this.dma1 = dma1;
        this.dma2 = dma2;
    }

    public DmaDataDto() {

    }
}
