package cn.looty.back.controllers;

import cn.looty.common.base.BaseController;
import cn.looty.common.result.ApiResult;
import cn.looty.common.result.ServiceResult;
import cn.looty.srv.user.model.SysUser;
import cn.looty.srv.user.model.dto.SysUserPageDTO;
import cn.looty.srv.user.service.ISysUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/list")
    public ApiResult list(@RequestBody SysUserPageDTO to){
        ServiceResult<Page<SysUser>> userListResult = sysUserService.page(to);
        return auto(userListResult);
    }

    @PostMapping
    public ApiResult save(@RequestBody SysUser user){
        return success(sysUserService.add(user));
    }


    @PutMapping
    public ApiResult update(@RequestBody SysUser user){
        return success(sysUserService.update(user));
    }
}
