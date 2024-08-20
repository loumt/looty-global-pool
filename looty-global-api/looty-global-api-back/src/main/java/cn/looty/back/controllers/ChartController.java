package cn.looty.back.controllers;

import cn.looty.common.base.BaseController;
import cn.looty.common.result.ResultData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Filename: ChartController
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: looty_loumt@hotmail.com
 * @Date: 2024-08-13 14:35
 */
@RestController
@RequestMapping("/api/chart")
public class ChartController extends BaseController {
    @GetMapping("/test")
    public ResultData test(){
        return success();
    }
}
