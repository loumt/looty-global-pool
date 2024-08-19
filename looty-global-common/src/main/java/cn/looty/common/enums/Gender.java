package cn.looty.common.enums;

import cn.looty.common.base.BaseEnumCode;

/**
 * @Classname Gender
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description 性别
 * @Date 2024/8/19 22:48
 */
public enum Gender implements BaseEnumCode {
    MALE(1, "男士"),
    FEMALE(2, "女士");

    private Integer code;
    private String type;

    Gender(Integer code, String type) {
        this.code = code;
        this.type = type;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    public String getType() {
        return type;
    }
}
