package cn.looty.srv.user.service;

import cn.looty.common.base.BaseService;
import cn.looty.srv.user.mapper.UserMapper;
import cn.looty.srv.user.model.SysUser;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Classname UserServiceImpl
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/19 21:55
 */
@DubboService(interfaceClass = IUserService.class)
public class UserServiceImpl extends BaseService implements IUserService{
    @Autowired
    private UserMapper userMapper;

    @Override
    public String welcome(String name) {
        List<SysUser> users = userMapper.selectList(Wrappers.lambdaQuery());

        for (SysUser user : users) {
            System.out.println(user.getNickName());
        }

        return "WelCome " + name;
    }
}
