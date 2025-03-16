package cn.looty.common.enums;

import cn.looty.common.base.BaseEnumCode;

/**
 * @Classname PermissionClientType
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/30 1:06
 */
public enum ClientType implements BaseEnumCode {
    ADMIN_MANAGE(1, "后台管理"),
    BLOG_CLIENT(2, "博客系统"),
    WX_CLIENT(3, "微信端")
    ;

    private Integer code;
    private String name;


    ClientType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
