package cn.looty.common.base;

import cn.looty.common.enums.ResultCode;
import cn.looty.common.result.ResultData;

/**
 * @Filename: BaseController
 * @Description: 基础控制器
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: looty_loumt@hotmail.com
 * @Date: 2024-08-13 14:37
 */
public abstract class BaseController {


    protected ResultData success(){
        return ResultData.of(ResultCode.SUCCESS);
    }

}
