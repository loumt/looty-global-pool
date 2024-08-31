package cn.looty.common.aspect;

import cn.looty.common.result.ServiceResult;
import org.apache.dubbo.rpc.RpcContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName: Log
 * @Description: 日志
 * @Author: Loumt
 * @Email: loumt@keeson.com
 * @Version: v1.0.0
 * @Date: 2021-05-21 17:00
 */
@Component
@Aspect
public class ServiceLogAspect  {
    private static final Long TIME_OUT_REMIND_SETTING = TimeUnit.SECONDS.toMillis(1);
    final static Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);
    private static ThreadLocal<Long> startTimeThreadLocal = new ThreadLocal<Long>();
    private static ThreadLocal<String> methodNameThreadLocal = new ThreadLocal<String>();

    @Pointcut("execution(public * cn.looty.srv.*.service.*Impl.*(..))")
    public void POINTCUT(){}


    @Before("POINTCUT()")
    public void dubboLoggerBefore(JoinPoint point){
        /**
         * 开始时间
         */
        startTimeThreadLocal.set(System.currentTimeMillis());
        String className = point.getSignature().getDeclaringType().getSimpleName();
        String methodName = point.getSignature().getName();
        methodNameThreadLocal.set(methodName);
        String args = getArgs(point);

        String remoteHost = RpcContext.getServerContext().getRemoteHost();

        logger.info("Remote:{} dubbo服务:{} 函数:{} 参数:{}", remoteHost, className, methodName, args);
    }

    @AfterReturning(returning = "result", pointcut = "POINTCUT()")
    public void dubboLoggerAfter(ServiceResult result) {
        long costTime = System.currentTimeMillis() - startTimeThreadLocal.get();
        startTimeThreadLocal.remove();
        String methodName = methodNameThreadLocal.get();
        methodNameThreadLocal.remove();
        if(costTime > TIME_OUT_REMIND_SETTING) {
            logger.info("[超时提醒({})] 函数:{} 结果:{} 用时:{} ms", TIME_OUT_REMIND_SETTING, methodName, result.getCode(), costTime);
        }else{
            logger.info("函数:{} 结果:{} => {} ms", methodName, result.getCode(), costTime);
        }
    }

    private String getArgs(JoinPoint point) {
        String[] parameterNames = ((MethodSignature) point.getSignature()).getParameterNames();
        StringBuilder sb = new StringBuilder();

        if(parameterNames == null) {
            sb.append("空参数");
        }else{
            for (int i = 0; i < parameterNames.length; i++) {
                sb.append(parameterNames[i] + ":" + (point.getArgs()[i] == null ? "": point.getArgs()[i].toString() + "; "));
            }
        }

        return sb.toString();
    }
}
