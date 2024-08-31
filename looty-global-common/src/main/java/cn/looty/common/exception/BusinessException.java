package cn.looty.common.exception;

import cn.looty.common.base.BaseResultEnum;

/**
 * @Classname BusinessException
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/31 22:38
 */
public final class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private int code;
    private String message;
    private BaseResultEnum resultEnum;

    private BusinessException(String message) {
        this.message = message;
    }

    private BusinessException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    public <E extends BaseResultEnum> BusinessException(E enumCode) {
        this.resultEnum = enumCode;
        this.code = enumCode.getCode();
        this.message = enumCode.getMessage();
    }

    public static <E extends BaseResultEnum> BusinessException of(E enumCode) {
        return new BusinessException(enumCode);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public BaseResultEnum getResultEnum() {
        return resultEnum;
    }
}