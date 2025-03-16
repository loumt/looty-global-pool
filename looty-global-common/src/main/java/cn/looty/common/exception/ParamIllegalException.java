package cn.looty.common.exception;

import cn.looty.common.base.BaseResultEnum;

/**
 * @Classname ParamIllegalException
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/12/1 16:12
 */
public class ParamIllegalException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private int code;
    private String message;

    private ParamIllegalException(String message) {
        this.message = message;
    }

    private ParamIllegalException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

}
