package cn.looty.api.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Filename: BlogApplication
 * @Description: 启动
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-07-25 16:48
 */
@SpringBootApplication(scanBasePackages = {"cn.looty.api.blog", "cn.looty.common"})
public class BlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }
}
