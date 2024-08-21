package cn.looty.common.base;

import cn.looty.common.enums.ResultCode;
import cn.looty.common.result.ServiceResult;

/**
 * @Filename: BaseController
 * @Description: 基础控制器
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: looty_loumt@hotmail.com
 * @Date: 2024-08-13 14:37
 */
public abstract class BaseController {


    protected ServiceResult success() {
        return ServiceResult.of(ResultCode.SUCCESS);
    }

    protected ServiceResult success(Object data) {
        return ServiceResult.of(ResultCode.SUCCESS, data);
    }

}
