package cn.looty.example.策略工厂模式.handlers;

import cn.looty.example.策略工厂模式.annotations.SysVersionTypeHandler;
import cn.looty.example.策略工厂模式.enums.SysVersionTypeEnum;
import org.springframework.stereotype.Service;

/**
 * @Filename: SysSalarySubsidyService
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-07-30 13:30
 */
@Service
@SysVersionTypeHandler(type = SysVersionTypeEnum.PROFESSION)
public class SysProfessionService extends SysVersionAbstractService {

    @Override
    public void businessDoSomething(Long versionId) {
        //业务逻辑
    }

}
