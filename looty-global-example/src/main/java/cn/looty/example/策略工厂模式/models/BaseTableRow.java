package cn.looty.example.策略工厂模式.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Filename: BaseTableRow
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-07-30 11:00
 */
@Data
public abstract class BaseTableRow {

    @ApiModelProperty("数据Id")
    private Long id;

    @ApiModelProperty(value = "添加时间")
    private Date addTime;
}
