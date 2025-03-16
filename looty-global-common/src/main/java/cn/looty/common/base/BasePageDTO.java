package cn.looty.common.base;

import cn.looty.common.constant.Constants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Classname BasePageDTO
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/30 0:47
 */
@Data
public class BasePageDTO implements Serializable {
    private static final long serialVersionUID = -1;
    @ApiModelProperty(name = "页码", notes = "默认为1")
    private Integer pageNo = Constants.PAGE_NO;

    @ApiModelProperty(name = "分页大小", notes = "默认为10")
    private Integer pageSize = Constants.PAGE_SIZE;
}
