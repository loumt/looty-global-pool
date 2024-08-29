package cn.looty.srv.article.model;

import cn.looty.common.base.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Classname Tag
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/30 0:30
 */
@Data
@TableName("sys_tag")
@ApiModel(description = "标签")
public class SysTag extends BaseModel {

    @TableField
    @ApiModelProperty("标签")
    private String name;

}
