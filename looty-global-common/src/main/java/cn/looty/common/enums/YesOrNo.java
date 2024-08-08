package cn.looty.common.enums;

import cn.looty.common.base.BaseEnumCode;

/**
 * @Classname YesOrNo
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/8 23:21
 */
public enum YesOrNo implements BaseEnumCode {
    YES(1, "是"),
    NO(0, "否");

    private Integer code;
    private String type;

    YesOrNo(Integer code, String type) {
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
