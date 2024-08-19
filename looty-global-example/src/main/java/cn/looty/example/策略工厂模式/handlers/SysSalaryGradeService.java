package cn.looty.example.策略工厂模式.handlers;


import cn.looty.example.策略工厂模式.annotations.SysVersionTypeHandler;
import cn.looty.example.策略工厂模式.enums.SysVersionTypeEnum;
import org.springframework.stereotype.Service;

/**
 * @Filename: SysSalaryGradeService
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-07-30 13:32
 */
@Service
@SysVersionTypeHandler(type = SysVersionTypeEnum.SALARY_GRADE)
public class SysSalaryGradeService extends SysVersionAbstractService {

    @Override
    public void businessDoSomething(Long versionId) {
        //业务逻辑
    }
}
