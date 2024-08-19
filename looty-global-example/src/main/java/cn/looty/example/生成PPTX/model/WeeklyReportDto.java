package cn.looty.example.生成PPTX.model;

import cn.looty.example.生成PPTX.utils.ExponentVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class WeeklyReportDto {
    @ApiModelProperty(value = "周报日期范围")
    private String rangeDate;
    @ApiModelProperty(value = "睿凝产品业绩")
    private List<List<String>> products;
    @ApiModelProperty(value = "乾德500指增产品表格数据")
    private List<ExponentVo> exp500;
    @ApiModelProperty(value = "乾德1000指增产品表格数据")
    private List<ExponentVo> exp1000;
    @ApiModelProperty(value = "量化中性DMA产品表格数据")
    private List<DmaExponentDto> dma;
    @ApiModelProperty(value = "市场风格")
    private String marketStyle;
    @ApiModelProperty(value = "风格因子")
    private String styleFactor;
    @ApiModelProperty(value = "股指期货基差")
    private String futuresBasis;
    @ApiModelProperty(value = "自评")
    private String evaluate;
    @ApiModelProperty(value = "建隆量化中性产品曲线图数据")
    private List<ChartDataDto> cnd;
    @ApiModelProperty(value = "乾德500指增产品")
    private List<ChartDataDto> ce5;
    @ApiModelProperty(value = "乾德1000指增产品")
    private List<ChartDataDto> ce10;
    @ApiModelProperty(value = "量化中性DMA产品中DMA1的数据")
    private List<ChartDataDto> dma1;
    @ApiModelProperty(value = "量化中性DMA产品中DMA1的数据")
    private List<ChartDataDto> dma2;
    @ApiModelProperty(value = "本周子策略及收益")
    private List<WeeklyStrategyDto> wss;
}
