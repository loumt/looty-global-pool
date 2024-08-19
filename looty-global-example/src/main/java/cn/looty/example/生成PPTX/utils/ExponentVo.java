package cn.looty.example.生成PPTX.utils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExponentVo {
    @ApiModelProperty(value = "项目名称")
    private String name;
    @ApiModelProperty(value = "上线以来的值")
    private String up;
    @ApiModelProperty(value = "3月迭代以来的值")
    private String down;
}
