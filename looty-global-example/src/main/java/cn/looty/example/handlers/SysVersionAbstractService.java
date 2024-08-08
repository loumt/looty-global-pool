package cn.looty.example.handlers;

import cn.looty.example.annotations.SysVersionTypeHandler;
import cn.looty.example.enums.SysVersionTypeEnum;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Filename: SysVersionAbstractService
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-07-30 16:26
 */
public abstract class SysVersionAbstractService implements InitializingBean {

    //注入service做业务逻辑

    protected SysVersionTypeEnum getType() {
        return this.getClass().getAnnotation(SysVersionTypeHandler.class).type();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        SysVersionServiceStrategyFactory.register(getType(), this);
    }

    public void valid(Long id) {
        //校验
    }

    //实际实现的业务逻辑
    protected abstract void businessDoSomething(Long id);

    //外部调用
    public final void exec(Long versionId, Long dataId) {
        /**
         * 验证版本是否还在
         */
        valid(versionId);

        /**
         * 业务
         */
        businessDoSomething(dataId);
    }
}
