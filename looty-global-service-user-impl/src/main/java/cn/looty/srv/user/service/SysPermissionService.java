package cn.looty.srv.user.service;

import cn.looty.common.base.BaseBusinessService;
import cn.looty.common.enums.CommonResultEnum;
import cn.looty.common.result.ServiceResult;
import cn.looty.srv.user.mapper.SysPermissionMapper;
import cn.looty.srv.user.model.SysPermission;
import cn.looty.srv.user.model.dto.SysPermissionPageDTO;
import cn.looty.srv.user.model.vo.SysPermissionVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @Classname SysPermissionService
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/30 1:13
 */
@DubboService(interfaceClass = ISysPermissionService.class, validation = "true", retries = 1)
public class SysPermissionService extends BaseBusinessService<SysPermissionMapper, SysPermission> implements ISysPermissionService{
    @Override
    public ServiceResult<SysPermissionVO> detail(Long id) {
        SysPermission permission = getById(id);

        SysPermissionVO vo = new SysPermissionVO();
        BeanUtils.copyProperties(permission, vo);

        return ServiceResult.of(CommonResultEnum.SUCCESS, vo);
    }

    @Override
    public ServiceResult<Page<SysPermission>> page(SysPermissionPageDTO to) {
        IPage<SysPermission> page = new Page<>(to.getPageNo(), to.getPageSize());
        return ServiceResult.of(CommonResultEnum.SUCCESS, page(page, Wrappers.lambdaQuery()));
    }

    @Override
    public ServiceResult<SysPermission> add(SysPermission permission) {
        save(permission);
        return ServiceResult.of(CommonResultEnum.SUCCESS, permission);
    }

    @Override
    public ServiceResult<SysPermission> update(SysPermission permission) {
        update(permission);
        return ServiceResult.of(CommonResultEnum.SUCCESS, permission);
    }

    @Override
    public ServiceResult<Boolean> delete(Long id) {
        return ServiceResult.of(CommonResultEnum.SUCCESS, removeById(id));
    }
}
