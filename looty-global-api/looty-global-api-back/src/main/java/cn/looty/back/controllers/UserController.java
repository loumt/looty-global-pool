package cn.looty.back.controllers;

import cn.looty.common.base.BaseController;
import cn.looty.common.result.ServiceResult;
import cn.looty.srv.user.model.SysUser;
import cn.looty.srv.user.service.ISysUserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

/**
 * @Filename: UserController
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: looty_loumt@hotmail.com
 * @Date: 2024-08-22 14:24
 */
@RestController
@RequestMapping("/api/user")
public class UserController extends BaseController {

    @DubboReference
    private ISysUserService sysUserService;


    @GetMapping("/list")
    public ServiceResult list(){
        return success(sysUserService.list());
    }


    @PostMapping
    public ServiceResult save(@RequestBody SysUser user){
        return success(sysUserService.save(user));
    }


    @PutMapping
    public ServiceResult update(@RequestBody SysUser user){
        return success(sysUserService.update(user));
    }
}
