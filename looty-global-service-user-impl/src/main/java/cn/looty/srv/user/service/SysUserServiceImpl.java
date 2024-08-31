package cn.looty.srv.user.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.looty.common.base.BaseBusinessService;
import cn.looty.common.base.BaseService;
import cn.looty.common.enums.CommonResultEnum;
import cn.looty.common.exception.BusinessException;
import cn.looty.common.result.ServiceResult;
import cn.looty.srv.user.enums.UserResultEnum;
import cn.looty.srv.user.mapper.SysUserMapper;
import cn.looty.srv.user.model.SysPermission;
import cn.looty.srv.user.model.SysRole;
import cn.looty.srv.user.model.SysUser;
import cn.looty.srv.user.model.dto.SysUserPageDTO;
import cn.looty.srv.user.model.vo.SysPermissionVO;
import cn.looty.srv.user.model.vo.SysUserVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Classname SysUserServiceImpl
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/19 21:55
 */
//validation 开启参数校验
//retries 失败自动切换，当出现失败，重试其它服务器。通常用于读操作，但重试会带来更长延迟。可通过 retries="2" 来设置重试次数(不含第一次)。
@DubboService(interfaceClass = ISysUserService.class, validation = "true", retries = 1)
public class SysUserServiceImpl extends BaseBusinessService<SysUserMapper, SysUser> implements ISysUserService {

    @Override
    public ServiceResult<SysUserVO> detail(Long id) {
        SysUser user = getById(id);

        SysUserVO vo = new SysUserVO();
        BeanUtils.copyProperties(user, vo);

        return ServiceResult.of(CommonResultEnum.SUCCESS, vo);
    }

    @Override
    public ServiceResult<Page<SysUser>> page(SysUserPageDTO to) {
        IPage<SysUser> page = new Page<>(to.getPageNo(), to.getPageSize());
        return ServiceResult.of(CommonResultEnum.SUCCESS, page(page, Wrappers.lambdaQuery()));
    }

    @Override
    public ServiceResult<SysUser> add(SysUser user) {
        save(user);
        return ServiceResult.of(CommonResultEnum.SUCCESS, user);
    }

    @Override
    public ServiceResult<SysUser> update(SysUser user) {
        updateById(user);
        return ServiceResult.of(CommonResultEnum.SUCCESS, user);
    }

    @Override
    public ServiceResult<Boolean> delete(Long id) {
        return ServiceResult.of(CommonResultEnum.SUCCESS, removeById(id));
    }
}
