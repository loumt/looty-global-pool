package cn.looty.srv.user;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Classname UserServiceApplication
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/19 22:23
 */
@SpringBootApplication
@DubboComponentScan(basePackages = {"cn.looty.srv.user"})
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication userServiceProviderApplication = new SpringApplication(UserServiceApplication.class);
        userServiceProviderApplication.setWebApplicationType(WebApplicationType.NONE);
        userServiceProviderApplication.run(args);
    }
}
