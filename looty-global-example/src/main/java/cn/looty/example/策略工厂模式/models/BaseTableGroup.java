package cn.looty.example.策略工厂模式.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Filename: BaseTableGroup
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-07-30 11:03
 */
@Data
public class BaseTableGroup<T extends BaseTableRow> {

    @ApiModelProperty("行数据")
    private List<T> rows;

}
