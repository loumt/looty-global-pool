package cn.looty.srv.user.service;

import cn.looty.srv.user.model.SysUser;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Filename: ISysUserService
 * @Description:
 * @Version: 1.0
 * @Author: louMT
 * @Email: looty_loumt@hotmail.com
 * @Date: 2024-08-20 13:59
 */
public interface ISysUserService {

    /**
     * 系统用户列表
     * @return
     */
    List<SysUser> list();

    @interface save{}
    SysUser save(SysUser user);

    @interface update{}
    SysUser update(SysUser user);

    boolean delete(@Min(1) @NotNull Long id);

}
