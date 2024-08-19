package cn.looty.example.策略工厂模式.handlers;


import cn.looty.example.策略工厂模式.enums.SysVersionTypeEnum;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @Filename: SysVersionServiceStrategyFactory
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-07-30 13:23
 */
public class SysVersionServiceStrategyFactory {
    private static Map<SysVersionTypeEnum, SysVersionAbstractService> maps = Maps.newConcurrentMap();
    public static SysVersionAbstractService getByType(SysVersionTypeEnum type){
        return maps.get(type);
    }
    public static void register(SysVersionTypeEnum type, SysVersionAbstractService service){
        maps.put(type, service);
    }
}