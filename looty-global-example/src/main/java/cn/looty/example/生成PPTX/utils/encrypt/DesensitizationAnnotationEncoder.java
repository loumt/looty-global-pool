package cn.looty.example.生成PPTX.utils.encrypt;

import java.lang.annotation.Documented;  
import java.lang.annotation.ElementType;  
import java.lang.annotation.Retention;  
import java.lang.annotation.RetentionPolicy;  
import java.lang.annotation.Target;  


/**
 * 
 * @DESCRIPTION:DesensitizationAnnotation 加密注解  与DesensitizationUtil合用有效
 * @TODO:未实现的事情
 * @author xiaokong
 * @date 2018年3月24日
 * @version 1.0.0
 * @file com.frame.aop.ActionMap.java
 */
@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.METHOD)  
@Documented
public @interface DesensitizationAnnotationEncoder {  
	public final static int ENCODER_TYPE_AES = 1;
	public final static int ENCODER_TYPE_REPLACE = 2;
	public int encoderType();
} 