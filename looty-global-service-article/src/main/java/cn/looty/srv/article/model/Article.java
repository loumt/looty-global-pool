package cn.looty.srv.article.model;

import cn.looty.common.base.BaseModel;
import cn.looty.common.enums.YesOrNo;
import cn.looty.srv.article.service.IArticleService;
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
@TableName("article")
@ApiModel(description = "博客")
public class Article extends BaseModel {

    @ApiModelProperty(name = "用户ID")
    @TableField
    private Long userId;

    @ApiModelProperty(name = "标题")
    @TableField
    @NotNull(groups = {IArticleService.add.class, IArticleService.update.class})
    @Size(min = 1, max = 20, groups = {IArticleService.add.class, IArticleService.update.class})
    private String title;

    @ApiModelProperty(name = "内容")
    @TableField
    @NotNull(groups = {IArticleService.add.class, IArticleService.update.class})
    @Size(min = 1, max = 3000, groups = {IArticleService.add.class, IArticleService.update.class})
    private String content;

    @ApiModelProperty(name = "是否可用，管理端设置，一键禁用")
    @TableField
    private YesOrNo isDisable;

    @ApiModelProperty(name = "完成进度")
    @TableField
    private Integer process;

    @ApiModelProperty(name = "是否可见，作者端设置")
    @TableField
    @NotNull(groups = {IArticleService.add.class, IArticleService.update.class})
    private YesOrNo isVisible;
}
