package cn.looty.srv.article;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Classname ArticleApplication
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/30 0:12
 */
@SpringBootApplication(scanBasePackages = {"cn.looty.srv.article", "cn.looty.common"})
@EnableDubbo
public class ArticleApplication {
    public static void main(String[] args) {
        SpringApplication userServiceProviderApplication = new SpringApplication(ArticleApplication.class);
        userServiceProviderApplication.setWebApplicationType(WebApplicationType.NONE);
        userServiceProviderApplication.run(args);
    }
}
