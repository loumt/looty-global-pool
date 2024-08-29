package cn.looty.srv.article.service;

import cn.looty.srv.article.model.Blog;
import cn.looty.srv.article.model.dto.BlogPageDTO;

import java.util.List;

/**
 * @Classname ArticleService
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/30 0:02
 */
public interface IBlogService{

    List<Blog> page(BlogPageDTO to);

    @interface add{};
    Blog add(Blog blog);

    @interface update{}
    Blog update(Blog blog);
}
