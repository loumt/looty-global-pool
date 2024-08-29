package cn.looty.common.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Classname BasePageDTO
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/30 0:47
 */
@Data
public class BasePageDTO {
    @ApiModelProperty(name = "页码", notes = "默认为1")
    private Integer pageNo = 1;

    @ApiModelProperty(name = "分页大小", notes = "默认为10")
    private Integer pageSize = 10;
}
