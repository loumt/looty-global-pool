package cn.looty.example.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Filename: BaseTable
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-07-29 17:17
 */
@Data
public abstract class BaseTable<T extends BaseTableGroup> {

    @ApiModelProperty("名称")
    private String tableName;

    @ApiModelProperty("总数")
    private Integer size;

    @ApiModelProperty("组数据")
    private List<T> groups;

}
