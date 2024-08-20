package cn.looty.blog.model;

import cn.looty.common.base.BaseModel;
import lombok.Data;

import java.util.Date;

/**
 * @Filename: ExportExample
 * @Description: 导出测试例子
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: looty_loumt@hotmail.com
 * @Date: 2024-08-20 16:27
 */
@Data
public class ExportExample extends BaseModel {
    private String no;

    private String name;

    private Integer type;

    private Date start;

    private Date end;
}
