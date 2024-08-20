package cn.looty.srv.user.service;

import cn.looty.srv.user.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Filename: IUserService
 * @Description:
 * @Version: 1.0
 * @Author: louMT
 * @Email: looty_loumt@hotmail.com
 * @Date: 2024-08-20 13:59
 */
public interface IUserService{

    String welcome(String name);

}
