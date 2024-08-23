package cn.looty.srv.user;

import cn.looty.common.aspect.ServiceLogAspect;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @Classname UserServiceApplication
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/19 22:23
 */
@SpringBootApplication(scanBasePackages = {"cn.looty.srv.user", "cn.looty.common"})
@EnableDubbo
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication userServiceProviderApplication = new SpringApplication(UserServiceApplication.class);
        userServiceProviderApplication.setWebApplicationType(WebApplicationType.NONE);
        userServiceProviderApplication.run(args);
    }
}
