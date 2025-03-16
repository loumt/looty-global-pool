package cn.looty.srv.article.mapper;

import cn.looty.srv.article.model.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Classname BlogMapper
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/30 0:18
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

}
