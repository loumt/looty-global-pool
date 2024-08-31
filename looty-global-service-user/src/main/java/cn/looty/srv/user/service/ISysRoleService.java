package cn.looty.srv.user.service;

import cn.looty.common.result.ServiceResult;
import cn.looty.srv.user.model.SysRole;
import cn.looty.srv.user.model.dto.SysRolePageDTO;
import cn.looty.srv.user.model.vo.SysRoleVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @Classname IRoleService
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/30 1:12
 */
public interface ISysRoleService {
    ServiceResult<SysRoleVO> detail(Long id);

    ServiceResult<Page<SysRole>> page(SysRolePageDTO to);

    ServiceResult<SysRole> add(SysRole role);

    ServiceResult<SysRole> update(SysRole role);

    ServiceResult<Boolean> delete(Long id);

}
