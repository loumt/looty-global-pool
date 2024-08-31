package cn.looty.common.base;

import cn.looty.common.enums.CommonResultEnum;
import cn.looty.common.result.ApiResult;
import cn.looty.common.result.ServiceResult;
import org.apache.commons.lang3.StringUtils;

/**
 * @Filename: BaseController
 * @Description: 基础控制器
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: looty_loumt@hotmail.com
 * @Date: 2024-08-13 14:37
 */
public abstract class BaseController {
    protected ApiResult success() {
        return new ApiResult(Boolean.TRUE, CommonResultEnum.SUCCESS.getCode(), CommonResultEnum.SUCCESS.getMessage());
    }

    protected ApiResult auto(ServiceResult rs) {
        if (rs.getCode() == CommonResultEnum.SUCCESS.getCode()) {
            return this.success(rs.getData());
        } else {
            return this.failure(rs);
        }
    }

    protected ApiResult failure(ServiceResult result) {
        return new ApiResult(Boolean.FALSE, result.getCode(), result.getMsg());
    }

    protected ApiResult failure(CommonResultEnum responseEnum) {
        return new ApiResult(Boolean.FALSE, responseEnum.getCode(), responseEnum.getMessage());
    }

    protected ApiResult success(Object data) {
        return new ApiResult(Boolean.TRUE, CommonResultEnum.SUCCESS.getCode(), CommonResultEnum.SUCCESS.getMessage(), data);
    }
}
