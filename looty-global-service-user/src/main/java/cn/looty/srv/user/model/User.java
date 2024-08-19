package cn.looty.srv.user.model;

import cn.looty.common.base.BaseModel;
import cn.looty.common.enums.Gender;
import cn.looty.common.enums.YesOrNo;
import lombok.Data;

/**
 * @Classname User
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/19 22:45
 */
@Data
public class User extends BaseModel {
    private String username;

    private String password;

    private String nickname;

    private Gender gender;

    private String birthday;

    private String email;

    private String name;

    private YesOrNo disable;
}
