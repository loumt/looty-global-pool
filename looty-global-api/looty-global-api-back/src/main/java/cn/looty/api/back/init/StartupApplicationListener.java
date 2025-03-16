package cn.looty.api.back.init;

import cn.looty.common.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Arrays;
import java.util.Map;


/**
 * @Classname StartupApplicationListener
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/11/22 22:34
 */

@Component
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {


    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
//        printBeans(event.getApplicationContext());

        //打印
       printRequestMappingKeys();
    }

    private void printBeans(ApplicationContext context) {
        String[] beanNames = context.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            if(!beanName.startsWith("org.springframework")){
                System.out.println("Bean: " + beanName);
            }
        }
    }

    private void printRequestMappingKeys() {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo info = entry.getKey();
            HandlerMethod method = entry.getValue();
            info.getPatternsCondition().getPatterns().forEach(pattern -> {
                System.out.println("Mapping=> " + pattern + ", Method=> " + method.getMethod().getName() + "  ClassName=>" + method.getBeanType().getName());
            });
        }
    }
}