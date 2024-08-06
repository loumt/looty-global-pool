package cn.looty.example.handlers;

import cn.looty.example.annotations.SysVersionTypeHandler;
import cn.looty.example.enums.SysVersionTypeEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Filename: SysVersionAbstractService
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-07-30 16:26
 */
public abstract class SysVersionAbstractService implements SysVersionService, InitializingBean {
    @Autowired
    private SysVersionMapper versionMapper;

    protected SysVersionTypeEnum getType() {
        return this.getClass().getAnnotation(SysVersionTypeHandler.class).type();
    }

    protected String getTableName() {
        return this.getClass().getAnnotation(SysVersionTypeHandler.class).tableName();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        SysVersionServiceStrategyFactory.register(getType(), this);
    }

    public SysVersion checkVersion(Long id) {
        SysVersion version = versionMapper.selectById(id);
        if (version == null) throw new SalaryBusinessException(ResultCode.SALARY_VERSION_NOT_FOUND);
        return version;
    }

    protected abstract void removeVersionDetail(Long id);
    public final void safeRemoveVersionDetail(Long versionId, Long dataId) {
        /**
         * 验证版本是否还在
         */
        checkVersion(versionId);

        /**
         * 删除
         */
        removeVersionDetail(dataId);
    }
}
