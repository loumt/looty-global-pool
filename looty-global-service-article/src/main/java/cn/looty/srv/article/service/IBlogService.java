package cn.looty.srv.article.service;

import cn.looty.common.result.ServiceResult;
import cn.looty.srv.article.model.Blog;
import cn.looty.srv.article.model.dto.BlogPageDTO;
import cn.looty.srv.article.model.vo.BlogVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @Classname ArticleService
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/30 0:02
 */
public interface IBlogService{

    ServiceResult<BlogVO> detail(Long id);

    ServiceResult<Page<Blog>> page(BlogPageDTO to);

    @interface add{};
    ServiceResult<Blog> add(Blog blog);

    @interface update{}
    ServiceResult<Blog> update(Blog blog);

    ServiceResult<Blog> delete(Long id);
}
