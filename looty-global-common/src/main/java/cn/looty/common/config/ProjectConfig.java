package cn.looty.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Filename: ProjectConfig
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: looty_loumt@hotmail.com
 * @Date: 2024-08-13 16:14
 */
@Component
@ConfigurationProperties(prefix = "project")
public class ProjectConfig {

    private static String name;


    private static String version;

    /**
     * @return Returns the name
     */
    public static String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public static void setName(String name) {
        ProjectConfig.name = name;
    }

    /**
     * @return Returns the version
     */
    public static String getVersion() {
        return version;
    }

    /**
     * @param version The version to set.
     */
    public static void setVersion(String version) {
        ProjectConfig.version = version;
    }
}
