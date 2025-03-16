package cn.looty.api.back.controllers;

import cn.looty.common.base.BaseController;
import cn.looty.common.result.ApiResult;
import cn.looty.srv.article.model.Article;
import cn.looty.srv.article.model.dto.ArticlePageDTO;
import cn.looty.srv.article.service.IArticleService;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

/**
 * @Classname ArticleController
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/30 0:22
 */
@RestController
@RequestMapping("/api/article")
@CrossOrigin
public class ArticleController extends BaseController {
    @DubboReference
    private IArticleService articleService;

    @PostMapping("/list")
    public ApiResult page(@RequestBody ArticlePageDTO to){
        System.out.println(ToStringBuilder.reflectionToString(to));
        return auto(articleService.page(to));
    }


    @GetMapping("/{id}")
    public ApiResult detail(@PathVariable Long id){
        System.out.println(id);
        return auto(articleService.detail(id));
    }

    @PostMapping
    public ApiResult save(@RequestBody Article article){
        return success(articleService.add(article));
    }

    @PutMapping
    public ApiResult update(@RequestBody Article article){
        return success(articleService.update(article));
    }
}
