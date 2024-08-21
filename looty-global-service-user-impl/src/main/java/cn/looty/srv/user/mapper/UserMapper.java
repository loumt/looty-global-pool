package cn.looty.srv.user.mapper;

import cn.looty.srv.user.model.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Filename: UserMapper
 * @Description:
 * @Version: 1.0
 * @Author: louMT
 * @Email: looty_loumt@hotmail.com
 * @Date: 2024-08-20 14:40
 */
@Mapper
public interface UserMapper extends BaseMapper<SysUser> {
}
