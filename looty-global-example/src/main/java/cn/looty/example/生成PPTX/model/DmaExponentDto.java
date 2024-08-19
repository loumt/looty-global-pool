package cn.looty.example.生成PPTX.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DmaExponentDto {
    @ApiModelProperty(value = "项目名称")
    private String name;
    @ApiModelProperty(value = "DMA1值")
    private String dma1;
    @ApiModelProperty(value = "DMA2")
    private String dma2;
}
