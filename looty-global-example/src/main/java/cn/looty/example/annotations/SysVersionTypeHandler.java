package cn.looty.example.annotations;

import cn.looty.example.enums.SysVersionTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Filename: SysVersionTypeHandler
 * @Description:
 * @Version: 1.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-07-30 16:29
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SysVersionTypeHandler {
    SysVersionTypeEnum type();
}
