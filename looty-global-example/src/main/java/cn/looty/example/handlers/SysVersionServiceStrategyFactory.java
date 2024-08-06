package cn.looty.example.handlers;


import cn.looty.example.enums.SysVersionTypeEnum;

/**
 * @Filename: SysVersionServiceStrategyFactory
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-07-30 13:23
 */
public class SysVersionServiceStrategyFactory {
    private static Map<SysVersionTypeEnum, SysVersionService> maps = Maps.newConcurrentMap();
    public static SysVersionService getByType(SysVersionTypeEnum type){
        return maps.get(type);
    }
    public static void register(SysVersionTypeEnum type, SysVersionService service){
        maps.put(type, service);
    }
}
