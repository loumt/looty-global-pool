package cn.looty.srv.article.model;

import cn.looty.common.enums.YesOrNo;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Classname UpDown
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/30 0:33
 */
@Data
@TableName("article_up_down")
@ApiModel(description = "点踩/点赞表")
public class ArticleUpDown {
    @TableField
    @ApiModelProperty("用户Id")
    private Long userId;

    @TableField
    @ApiModelProperty("点踩")
    private YesOrNo upDown;

}
