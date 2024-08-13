package cn.looty.common.result;

import cn.looty.common.enums.ResultCode;
import lombok.Data;

import java.io.Serializable;

/**
 * @Filename: ResultData
 * @Description: 返回数据
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-07-25 16:20
 */
@Data
public class ResultData<T> implements Serializable {
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

    public ResultData(ResultCode code) {
        this.code = code.getCode();
        this.msg = code.getMessage();
    }

    public static ResultData of(ResultCode code) {
        return new ResultData(code);
    }
}
