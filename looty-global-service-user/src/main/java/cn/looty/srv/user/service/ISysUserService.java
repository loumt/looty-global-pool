package cn.looty.srv.user.service;

import cn.looty.common.result.ServiceResult;
import cn.looty.srv.user.model.SysUser;
import cn.looty.srv.user.model.dto.SysUserPageDTO;
import cn.looty.srv.user.model.vo.SysUserVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Filename: ISysUserService
 * @Description:
 * @Version: 1.0
 * @Author: louMT
 * @Email: looty_loumt@hotmail.com
 * @Date: 2024-08-20 13:59
 */
public interface ISysUserService {

    ServiceResult<SysUserVO> detail(Long id);
    /**
     * 系统用户列表
     * @return
     */
    ServiceResult<Page<SysUser>> page(SysUserPageDTO to);

    @interface save{}
    ServiceResult<SysUser> add(SysUser user);

    @interface update{}
    ServiceResult<SysUser> update(SysUser user);

    ServiceResult<Boolean> delete(Long id);

}
