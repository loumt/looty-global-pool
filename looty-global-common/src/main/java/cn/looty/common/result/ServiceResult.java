package cn.looty.common.result;

import cn.looty.common.base.BaseResultEnum;
import cn.looty.common.enums.CommonResultEnum;
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

    public ServiceResult() {
        this.code = CommonResultEnum.SUCCESS.getCode();
        this.msg = CommonResultEnum.SUCCESS.getMessage();
    }

    public <E extends BaseResultEnum> ServiceResult(E enumCode) {
        this.code = enumCode.getCode();
        this.msg = enumCode.getMessage();
    }

    public <E extends BaseResultEnum> ServiceResult(E enumCode, T data) {
        this.code = enumCode.getCode();
        this.msg = enumCode.getMessage();
        this.data = data;
    }

    public static ServiceResult of() {
        return new ServiceResult();
    }

    public static <E extends BaseResultEnum> ServiceResult of(E enumCode) {
        return new ServiceResult(enumCode);
    }

    public static <E extends BaseResultEnum, T> ServiceResult of(E enumCode, T data) {
        return new ServiceResult(enumCode, data);
    }
}
