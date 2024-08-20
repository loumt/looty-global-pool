package cn.looty.srv.user.model;

import cn.looty.common.base.BaseModel;
import cn.looty.common.enums.YesOrNo;
import lombok.Data;

/**
 * @Filename: User
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: looty_loumt@hotmail.com
 * @Date: 2024-08-20 14:24
 */
@Data
public class User extends BaseModel {
    private YesOrNo disable;

    private String nickName;

    private String username;

    private String password;
}
