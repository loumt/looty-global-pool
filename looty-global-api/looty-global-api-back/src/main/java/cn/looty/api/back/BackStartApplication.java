package cn.looty.api.back;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @Filename: BackStartApplication
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: looty_loumt@hotmail.com
 * @Date: 2024-08-13 14:44
 */
@SpringBootApplication(scanBasePackages = {"cn.looty.api.back", "cn.looty.common"})
@EnableDubbo
public class BackStartApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackStartApplication.class, args);
    }
}
