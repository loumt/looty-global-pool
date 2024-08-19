package cn.looty.example.生成PPTX.utils.encrypt;

import java.lang.annotation.Documented;  
import java.lang.annotation.ElementType;  
import java.lang.annotation.Retention;  
import java.lang.annotation.RetentionPolicy;  
import java.lang.annotation.Target;  


/**
 * 
 * @DESCRIPTION:DesensitizationAnnotation 脱敏注解  与DesensitizationUtil合用有效
 * @TODO:未实现的事情
 * @author xiaokong
 * @date 2018年3月24日
 * @version 1.0.0
 * @file com.frame.aop.ActionMap.java
 */
@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.METHOD)  
@Documented
public @interface DesensitizationAnnotationXingXing {  
	/**不脱敏前几个*/
	public int prefix();
	/**不脱敏后几个*/
	public int suffix();
} 