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
import javax.validation.constraints.Size;

/**
 * @Filename: Article
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: looty_loumt@hotmail.com
 * @Date: 2024-08-20 14:06
 */
@Data
@TableName("blog")
@ApiModel(description = "博客")
public class Blog extends BaseModel {

    @ApiModelProperty(name = "用户ID")
    @TableField
    private Long userId;

    @ApiModelProperty(name = "标题")
    @TableField
    @NotNull(groups = {IBlogService.add.class, IBlogService.update.class})
    @Size(min = 1, max = 20, groups = {IBlogService.add.class, IBlogService.update.class})
    private String title;

    @ApiModelProperty(name = "标题")
    @TableField
    @NotNull(groups = {IBlogService.add.class, IBlogService.update.class})
    @Size(min = 1, max = 3000, groups = {IBlogService.add.class, IBlogService.update.class})
    private String content;

    @ApiModelProperty(name = "是否禁用")
    @TableField
    private YesOrNo isDisable;

    @ApiModelProperty(name = "是否可见")
    @TableField
    @NotNull(groups = {IBlogService.add.class, IBlogService.update.class})
    private YesOrNo isVisible;
}
