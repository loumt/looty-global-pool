package cn.looty.common.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Classname BaseService
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/19 22:35
 */
public abstract class BaseService<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {
}
