package cn.looty.example.策略工厂模式.handlers;


import cn.looty.example.策略工厂模式.annotations.SysVersionTypeHandler;
import cn.looty.example.策略工厂模式.enums.SysVersionTypeEnum;
import org.springframework.stereotype.Service;


/**
 * @Filename: SysCountryEngageService
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-07-30 13:32
 */
@Service
@SysVersionTypeHandler(type = SysVersionTypeEnum.COUNTRY_ENGAGE)
public class SysCountryEngageService extends SysVersionAbstractService {

    @Override
    public void businessDoSomething(Long versionId) {
        //业务逻辑
    }

}

