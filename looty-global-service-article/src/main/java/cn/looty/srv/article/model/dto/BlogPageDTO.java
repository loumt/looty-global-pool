package cn.looty.srv.article.model.dto;

import cn.looty.common.base.BasePageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Classname BlogPageDTO
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/30 0:46
 */
@Data
public class BlogPageDTO extends BasePageDTO {

    @ApiModelProperty(name = "用户ID")
    private Long userId;

    @ApiModelProperty(name = "标题模糊匹配")
    private String title;

}
