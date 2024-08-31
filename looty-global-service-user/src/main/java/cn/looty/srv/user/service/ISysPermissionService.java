package cn.looty.srv.user.service;

import cn.looty.common.result.ServiceResult;
import cn.looty.srv.user.model.SysPermission;
import cn.looty.srv.user.model.SysUser;
import cn.looty.srv.user.model.dto.SysPermissionPageDTO;
import cn.looty.srv.user.model.dto.SysUserPageDTO;
import cn.looty.srv.user.model.vo.SysPermissionVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Classname ISysPermissionService
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/30 1:13
 */
public interface ISysPermissionService {
    ServiceResult<SysPermissionVO> detail(Long id);
    /**
     * 系统用户列表
     * @return
     */
    ServiceResult<Page<SysPermission>> page(SysPermissionPageDTO to);

    @interface save{}
    ServiceResult<SysPermission> add(SysPermission user);

    @interface update{}
    ServiceResult<SysPermission> update(SysPermission user);

    ServiceResult<Boolean> delete(Long id);
}
