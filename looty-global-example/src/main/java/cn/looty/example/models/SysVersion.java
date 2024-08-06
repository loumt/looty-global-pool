package cn.looty.example.models;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Filename: SysVersion
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-07-29 15:39
 */
@Data
@ApiModel(description = "版本控制")
@TableName("sys_version")
public class SysVersion extends BaseEntity {
    public static final Integer IS_CURRENT = 1;
    public static final Integer IS_NOT_CURRENT = 0;


    public static final Integer COUNTRY_ENGAGE = 1;//国聘类型
    public static final Integer SALARY_GRADE = 2;//薪资等级
    public static final Integer SALARY_SUBSIDY = 3;//基础性工资绩效
    public static final Integer SENIORITY_SUBSIDY = 4;//工龄补贴
    public static final Integer GROUP_PERSONAL_NATURE = 5;//群体人员性质

    @ApiModelProperty("名")
    @TableField
    private String name;
    @ApiModelProperty("类型")
    @TableField
    private Integer type;
    @ApiModelProperty("当前版本")
    @TableField
    private Integer isCurrent;

    public boolean isApply(){
        return this.isCurrent!= null && this.isCurrent == 1;
    }

}
