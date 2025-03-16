package cn.looty.srv.article.service;

import cn.looty.common.base.BaseBusinessService;
import cn.looty.common.enums.CommonResultEnum;
import cn.looty.common.result.ServiceResult;
import cn.looty.common.utils.StringUtil;
import cn.looty.srv.article.mapper.ArticleMapper;
import cn.looty.srv.article.model.Article;
import cn.looty.srv.article.model.dto.ArticlePageDTO;
import cn.looty.srv.article.model.vo.ArticleVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;

/**
 * @Classname ArticleServiceImpl
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/30 0:15
 */
@DubboService(interfaceClass = IArticleService.class, validation = "true", retries = 1)
public class ArticleServiceImpl extends BaseBusinessService<ArticleMapper, Article> implements IArticleService {

    @Override
    public ServiceResult<ArticleVO> detail(Long id) {
        Article article = getById(id);

        ArticleVO vo = new ArticleVO();
        BeanUtils.copyProperties(article, vo);
        return ServiceResult.of(CommonResultEnum.SUCCESS, vo);
    }

    @Override
    public ServiceResult<Page<Article>> page(ArticlePageDTO to) {
        IPage<Article> page = new Page<>(to.getPageNo(), to.getPageSize());
        LambdaQueryWrapper<Article> qw = Wrappers.lambdaQuery();
        qw.eq(to.getUserId() != null, Article::getUserId, to.getUserId());
        qw.like(StringUtil.isNotEmpty(to.getTitle()), Article::getTitle, to.getTitle());
        IPage<Article> resultPage = page(page, qw);
        return ServiceResult.of(CommonResultEnum.SUCCESS, resultPage);
    }

    @Override
    public ServiceResult<Article> add(Article article) {
        save(article);
        return ServiceResult.of(CommonResultEnum.SUCCESS, article);
    }

    @Override
    public ServiceResult<Article> update(Article article) {
        updateById(article);
        return ServiceResult.of(CommonResultEnum.SUCCESS, article);
    }

    @Override
    public ServiceResult<Article> delete(Long id) {
        return ServiceResult.of(CommonResultEnum.SUCCESS, removeById(id));
    }
}
