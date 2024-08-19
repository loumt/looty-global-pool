package cn.looty.common.enums;

/**
 * @Classname ResultCode
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/8 23:25
 */
public enum ResultCode {
    SUCCESS(100000, "成功"),
    FAILURE(500000, "系统错误");

    private Integer code;
    private String message;

    ResultCode(Integer code, String message) {
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
