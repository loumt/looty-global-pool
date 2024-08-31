package cn.looty.back.controllers;

import cn.looty.common.base.BaseController;
import cn.looty.common.result.ApiResult;
import cn.looty.srv.article.model.Blog;
import cn.looty.srv.article.model.dto.BlogPageDTO;
import cn.looty.srv.article.service.IBlogService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

/**
 * @Classname ArticleController
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/30 0:22
 */
@RestController
@RequestMapping("/api/article")
public class ArticleController extends BaseController {
    @DubboReference
    private IBlogService blogService;

    @PostMapping("/list")
    public ApiResult list(@RequestBody BlogPageDTO to){
        return success(blogService.page(to));
    }


    @PostMapping
    public ApiResult save(@RequestBody Blog blog){
        return success(blogService.add(blog));
    }


    @PutMapping
    public ApiResult update(@RequestBody Blog blog){
        return success(blogService.update(blog));
    }
}
