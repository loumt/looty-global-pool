package cn.looty.blog.controller;

import cn.looty.blog.model.ExportExample;
import cn.looty.common.utils.ExportTypeEnum;
import cn.looty.common.result.ServiceResult;
import cn.looty.common.utils.CommonExportExcelUtil;
import cn.looty.common.utils.ExportExcelUtil;
import com.google.common.collect.Lists;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @Filename: TestController
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: looty_loumt@hotmail.com
 * @Date: 2024-08-20 15:46
 */
@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping
    public ServiceResult test() {
        return ServiceResult.of();
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        List<ExportExample> examples = Lists.newArrayList();
        ExportExample example1 = new ExportExample();
        example1.setName("测试");
        example1.setNo("测试");
        example1.setType(1);
        example1.setStart(new Date());
        example1.setEnd(new Date());
        examples.add(example1);
        ExportExcelUtil.export(ExportTypeEnum.EXAMPLE, examples, response);
    }

    @GetMapping("/export/business")
    public void exportBusiness(HttpServletResponse response) {
        CommonExportExcelUtil.example(response);
    }

}
