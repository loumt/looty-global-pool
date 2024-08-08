package cn.looty.common.enums;

/**
 * @Classname ResultCodeEnum
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/8 23:25
 */
public enum ResultCodeEnum {
    SUCCESS(100000, "Success"),
    FAILURE(500000, "Failure");

    private Integer code;
    private String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
