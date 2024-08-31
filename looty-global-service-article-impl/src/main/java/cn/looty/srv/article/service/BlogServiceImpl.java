package cn.looty.srv.article.service;

import cn.looty.common.base.BaseBusinessService;
import cn.looty.common.base.BaseService;
import cn.looty.common.enums.CommonResultEnum;
import cn.looty.common.result.ServiceResult;
import cn.looty.srv.article.mapper.BlogMapper;
import cn.looty.srv.article.model.Blog;
import cn.looty.srv.article.model.dto.BlogPageDTO;
import cn.looty.srv.article.model.vo.BlogVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Classname ArticleServiceImpl
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/30 0:15
 */
@DubboService(interfaceClass = IBlogService.class, validation = "true", retries = 1)
public class BlogServiceImpl extends BaseBusinessService<BlogMapper, Blog> implements IBlogService {

    @Override
    public ServiceResult<BlogVO> detail(Long id) {
        Blog blog = getById(id);

        BlogVO vo = new BlogVO();
        BeanUtils.copyProperties(blog, vo);
        return ServiceResult.of(CommonResultEnum.SUCCESS, vo);
    }

    @Override
    public ServiceResult<Page<Blog>> page(BlogPageDTO to) {
        IPage<Blog> page = new Page<>(to.getPageNo(), to.getPageSize());
        return ServiceResult.of(CommonResultEnum.SUCCESS, page(page, Wrappers.lambdaQuery()));
    }

    @Override
    public ServiceResult<Blog> add(Blog blog) {
        save(blog);
        return ServiceResult.of(CommonResultEnum.SUCCESS, blog);
    }

    @Override
    public ServiceResult<Blog> update(Blog blog) {
        updateById(blog);
        return ServiceResult.of(CommonResultEnum.SUCCESS, blog);
    }

    @Override
    public ServiceResult<Blog> delete(Long id) {
        return ServiceResult.of(CommonResultEnum.SUCCESS, removeById(id));
    }
}
