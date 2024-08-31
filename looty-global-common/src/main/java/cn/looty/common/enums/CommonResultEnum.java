package cn.looty.common.enums;

import cn.looty.common.base.BaseResultEnum;

/**
 * @Classname CommonResultCode
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/31 22:46
 */
public enum CommonResultEnum implements BaseResultEnum {
    SUCCESS(100000, "成功"),
    FAILURE(500000, "系统错误"),

    NOT_FOUND(400000, "未找到资源"),
    NO_PERMISSION(400001, "无权限"),

    ;

    CommonResultEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
