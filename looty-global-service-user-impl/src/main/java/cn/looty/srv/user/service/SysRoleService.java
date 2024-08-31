package cn.looty.srv.user.service;

import cn.looty.common.base.BaseBusinessService;
import cn.looty.common.enums.CommonResultEnum;
import cn.looty.common.result.ServiceResult;
import cn.looty.srv.user.mapper.SysRoleMapper;
import cn.looty.srv.user.model.SysPermission;
import cn.looty.srv.user.model.SysRole;
import cn.looty.srv.user.model.dto.SysRolePageDTO;
import cn.looty.srv.user.model.vo.SysPermissionVO;
import cn.looty.srv.user.model.vo.SysRoleVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;

/**
 * @Classname SysRoleService
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/30 1:13
 */
@DubboService(interfaceClass = ISysRoleService.class, validation = "true", retries = 1)
public class SysRoleService extends BaseBusinessService<SysRoleMapper, SysRole> implements ISysRoleService{

    @Override
    public ServiceResult<SysRoleVO> detail(Long id) {
        SysRole role = getById(id);

        SysRoleVO vo = new SysRoleVO();
        BeanUtils.copyProperties(role, vo);

        return ServiceResult.of(CommonResultEnum.SUCCESS, vo);
    }

    @Override
    public ServiceResult<Page<SysRole>> page(SysRolePageDTO to) {
        IPage<SysRole> page = new Page<>(to.getPageNo(), to.getPageSize());
        return ServiceResult.of(CommonResultEnum.SUCCESS, page(page, Wrappers.lambdaQuery()));
    }

    @Override
    public ServiceResult<SysRole> add(SysRole role) {
        save(role);
        return ServiceResult.of(CommonResultEnum.SUCCESS, role);
    }

    @Override
    public ServiceResult<SysRole> update(SysRole role) {
        update(role);
        return ServiceResult.of(CommonResultEnum.SUCCESS, role);
    }

    @Override
    public ServiceResult<Boolean> delete(Long id) {
        return ServiceResult.of(CommonResultEnum.SUCCESS, removeById(id));
    }
}
