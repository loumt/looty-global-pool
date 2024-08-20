package cn.looty.srv.user.service;

import cn.looty.common.base.BaseService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @Classname UserServiceImpl
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/19 21:55
 */
@DubboService(interfaceClass = IUserService.class)
public class UserServiceImpl extends BaseService implements IUserService{
    @Override
    public String welcome(String name) {
        return "WelCome " + name;
    }
}
