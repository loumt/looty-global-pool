package cn.looty.common.enums;

import cn.looty.common.base.BaseEnumCode;

/**
 * @Classname PermissionType
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/30 1:06
 */
public enum PermissionType implements BaseEnumCode {
    CATE_GORY(1, "目录"),
    BUTTON(2, "按钮")
    ;

    private Integer code;
    private String name;


    PermissionType(int code, String name) {
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
