package cn.looty.back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @Filename: BackStartApplication
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: looty_loumt@hotmail.com
 * @Date: 2024-08-13 14:44
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class BackStartApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackStartApplication.class, args);
    }
}
