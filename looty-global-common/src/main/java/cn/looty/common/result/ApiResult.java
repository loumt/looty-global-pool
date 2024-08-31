package cn.looty.common.result;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * @Classname ApiResult
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/31 23:47
 */
public final class ApiResult implements Serializable {
    private static final long serialVersionUID = -3309094712103650996L;

    private boolean success;
    private Integer code;
    private String message;
    private Object data;

    public ApiResult() {
    }

    public ApiResult(boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public ApiResult(boolean success, Integer code, String message, Object data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
