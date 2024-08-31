package cn.looty.common.plugin;

import cn.looty.common.base.BaseResultEnum;
import cn.looty.common.enums.CommonResultEnum;
import cn.looty.common.exception.BusinessException;
import cn.looty.common.result.ServiceResult;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import java.util.Optional;

/**
 * @Classname InvokeExceptionFilter
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/31 22:36
 */
public class InvokeExceptionFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Result result = invoker.invoke(invocation);
        Throwable ex = result.getException();

        // 判断是否是自定义的异常
        if (ex instanceof BusinessException) {
            result.setException(null);
            BusinessException exception = (BusinessException) ex;
            BaseResultEnum resultEnum = exception.getResultEnum();
            BaseResultEnum throwCode = Optional.ofNullable(resultEnum).orElse(CommonResultEnum.FAILURE);
            result.setValue(ServiceResult.of(throwCode));
        } else if (ex instanceof RuntimeException) {
            ex.printStackTrace();
            result.setException(null);
            result.setValue(ServiceResult.of(CommonResultEnum.FAILURE));
        }
        return result;
    }
}
