package cn.looty.common.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Classname BaseBusinessService
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/30 1:01
 */
public abstract class BaseBusinessService<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {
}
