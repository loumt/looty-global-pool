package cn.looty.example.策略工厂模式.enums;

/**
 * @Classname EmailOriginTypeEnum
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/8 23:52
 */
public enum EmailOriginTypeEnum {
    ORIGIN_ONE(1, "源头1", "1111111@qq.com", "xxxxxxxxxxxxxxxxx"),
    ORIGIN_TWO(2, "源头2", "2222222@qq.com", "xxxxxxxxxxxxxxxxx");

    private Integer code;
    private String origin;

    private String email;
    private String authCode;

    EmailOriginTypeEnum(Integer code, String origin, String email, String authCode) {
        this.code = code;
        this.origin = origin;
        this.email = email;
        this.authCode = authCode;
    }

    public Integer getCode() {
        return code;
    }

    public String getOrigin() {
        return origin;
    }

    public String getEmail() {
        return email;
    }

    public String getAuthCode() {
        return authCode;
    }
}
