package cn.looty.srv.article.service;

import cn.looty.common.result.ServiceResult;
import cn.looty.srv.article.model.Article;
import cn.looty.srv.article.model.dto.ArticlePageDTO;
import cn.looty.srv.article.model.vo.ArticleVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @Classname ArticleService
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/30 0:02
 */
public interface IArticleService {

    ServiceResult<ArticleVO> detail(Long id);

    ServiceResult<Page<Article>> page(ArticlePageDTO to);

    @interface add{};
    ServiceResult<Article> add(Article article);

    @interface update{}
    ServiceResult<Article> update(Article article);

    ServiceResult<Article> delete(Long id);
}
