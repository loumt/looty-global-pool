package cn.looty.back.controllers;

import cn.looty.common.base.BaseController;
import cn.looty.common.result.ServiceResult;
import cn.looty.srv.user.service.IUserService;
import org.apache.dubbo.config.annotation.DubboReference;
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
    @DubboReference
    private IUserService userService;

    @GetMapping("/test")
    public ServiceResult test(){
        return success(userService.welcome("dubbo"));
    }
}
