package cn.looty.srv.user.service;

import cn.looty.common.base.BaseService;
import cn.looty.srv.user.mapper.SysUserMapper;
import cn.looty.srv.user.model.SysUser;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Classname SysUserServiceImpl
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/19 21:55
 */
@DubboService(interfaceClass = ISysUserService.class, validation = "true")
public class SysUserServiceImpl extends BaseService implements ISysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public List<SysUser> list() {
        return  sysUserMapper.selectList(Wrappers.lambdaQuery());
    }

    @Override
    public SysUser save(SysUser user) {
        sysUserMapper.insert(user);
        return user;
    }

    @Override
    public SysUser update(SysUser user) {
        sysUserMapper.updateById(user);
        return user;
    }

    @Override
    public boolean delete(Long id) {
        return sysUserMapper.deleteById(id) > 0;
    }
}
