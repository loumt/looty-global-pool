package cn.looty.srv.article.model;

import cn.looty.common.base.BaseModel;
import cn.looty.common.enums.YesOrNo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Classname Comment
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/30 0:33
 */
@Data
@TableName("article_comment")
@ApiModel(description = "评论")
public class ArticleComment extends BaseModel {
    @TableField
    @ApiModelProperty("用户Id")
    private Long userId;
    @TableField
    @ApiModelProperty("评论")
    private String comment;
}
