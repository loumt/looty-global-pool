package cn.looty.common.result;

import cn.looty.common.enums.ResultCode;
import lombok.Data;

import java.io.Serializable;

/**
 * @Filename: ServiceResult
 * @Description: 返回数据
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-07-25 16:20
 */
@Data
public class ServiceResult<T> implements Serializable {
    private static final long serialVersionUID = -1204002577294901112L;
    /**
     * 业务Code
     */
    private Integer code;

    /**
     * 信息详情
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    public ServiceResult(ResultCode code) {
        this.code = code.getCode();
        this.msg = code.getMessage();
    }

    public ServiceResult(ResultCode code, T data) {
        this.code = code.getCode();
        this.msg = code.getMessage();
        this.data = data;
    }

    public static ServiceResult of(ResultCode code) {
        return new ServiceResult(code);
    }

    public static <T> ServiceResult of(ResultCode code, T data) {
        return new ServiceResult(code, data);
    }
}
