package cn.looty.srv.article.model;

import cn.looty.common.base.BaseModel;
import cn.looty.common.enums.YesOrNo;
import cn.looty.srv.article.service.IBlogService;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Classname Category
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/30 0:32
 */
@Data
@TableName("blog_category")
@ApiModel(description = "博客目录")
public class BlogCategory extends BaseModel {

    @TableField
    @ApiModelProperty("用户Id")
    private Long userId;

    @TableField
    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty(name = "是否可见")
    @TableField
    private YesOrNo isVisible;
}
