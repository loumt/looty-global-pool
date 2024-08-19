package cn.looty.example.策略工厂模式.common;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 基础bean 都要集成该实体类
 * @author 53045
 *
 */
public abstract class BaseEntity {
    @ApiModelProperty(value = "主键ID")
    @TableId(type = IdType.AUTO)
    private Long id;
    @ApiModelProperty(value = "删除状态（0--未删除1--已删除）")
    @TableField("del")
	@TableLogic
    private Integer del;
    @ApiModelProperty(value = "添加时间")
    @TableField(value = "add_time", fill = FieldFill.INSERT)
    private Date addTime;
    @ApiModelProperty(value = "更新时间")
    @TableField(value = "up_time", fill = FieldFill.INSERT_UPDATE)
    private Date upTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getDel() {
		return del;
	}
	public void setDel(Integer del) {
		this.del = del;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public Date getUpTime() {
		return upTime;
	}
	public void setUpTime(Date upTime) {
		this.upTime = upTime;
	}
}
