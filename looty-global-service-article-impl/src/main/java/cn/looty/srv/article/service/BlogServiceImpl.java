package cn.looty.srv.article.service;

import cn.looty.common.base.BaseBusinessService;
import cn.looty.common.base.BaseService;
import cn.looty.srv.article.mapper.BlogMapper;
import cn.looty.srv.article.model.Blog;
import cn.looty.srv.article.model.dto.BlogPageDTO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Classname ArticleServiceImpl
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/30 0:15
 */
@DubboService
public class BlogServiceImpl extends BaseBusinessService<BlogMapper, Blog> implements IBlogService{

    @Override
    public List<Blog> page(BlogPageDTO to) {
        return list(Wrappers.lambdaQuery());
    }

    @Override
    public Blog add(Blog blog) {
        return null;
    }

    @Override
    public Blog update(Blog blog) {
        return null;
    }
}
