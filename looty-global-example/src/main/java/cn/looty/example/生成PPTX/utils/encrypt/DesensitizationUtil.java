package cn.looty.example.生成PPTX.utils.encrypt;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 脱敏工具,与DesensitizationAnnotation注解联合使用有效
 *
 * @author 53045
 */
public class DesensitizationUtil {
    public static final String encryptAesKey = "orz*210621-8725=";

    /**
     * 数据脱敏
     *
     * @param obj
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void desensitizat(Object obj) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if (obj == null) {
            return;
        }
        Class clz = obj.getClass();
        Method[] methods = clz.getMethods();
        for (Method method : methods) {
            if (!method.getName().startsWith("get") || !method.getReturnType().isAssignableFrom(String.class)) {
                continue;
            }
            DesensitizationAnnotationXingXing annotation = method.getAnnotation(DesensitizationAnnotationXingXing.class);
            //星号脱敏
            if (annotation != null) {
                int prefix = annotation.prefix();
                int suffix = annotation.suffix();
                String methodValue = String.valueOf(method.invoke(obj));
                if (methodValue == null) {
                    continue;
                }
                String dealStr = dealXingXing(methodValue, prefix, suffix);
                Method setMethod = clz.getMethod(method.getName().replace("get", "set"), String.class);
                setMethod.invoke(obj, dealStr);
            }
        }
    }

    /**
     * 数据加密保护
     *
     * @param obj
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static int encryptData(Object obj) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if (obj == null) {
            return 0;
        }
        Class clz = obj.getClass();
        Method[] methods = clz.getMethods();
        int eCount = 0;
        for (Method method : methods) {
            if (!method.getName().startsWith("get") || !method.getReturnType().isAssignableFrom(String.class)) {
                continue;
            }
            //数据加解密
            DesensitizationAnnotationEncoder annotationEncoder = method.getAnnotation(DesensitizationAnnotationEncoder.class);
            if (annotationEncoder != null) {
                String methodValue = String.valueOf(method.invoke(obj));
                int type = annotationEncoder.encoderType();
                String encryptStr = null;
                if (type == DesensitizationAnnotationEncoder.ENCODER_TYPE_AES) {
                    try {
                        encryptStr = encryptAes(methodValue);
                        eCount++;
                    } catch (Exception e) {

                    }
                }
                if (type == DesensitizationAnnotationEncoder.ENCODER_TYPE_REPLACE) {
                    try {
                        encryptStr = encryptReplace(methodValue);
                        eCount++;
                    } catch (Exception e) {

                    }
                }
                Method setMethod = clz.getMethod(method.getName().replace("get", "set"), String.class);
                setMethod.invoke(obj, encryptStr);
            }
        }
        return eCount;
    }

    /**
     * 数据解密
     *
     * @param obj
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void dncoderData(Object obj) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (obj == null) {
            return;
        }
        Class clz = obj.getClass();
        Method[] methods = clz.getMethods();
        for (Method method : methods) {
            if (!method.getName().startsWith("get") || !method.getReturnType().isAssignableFrom(String.class)) {
                continue;
            }
            //数据加解密
            DesensitizationAnnotationEncoder annotationEncoder = method.getAnnotation(DesensitizationAnnotationEncoder.class);
            if (annotationEncoder != null) {
                String methodValue = String.valueOf(method.invoke(obj));
                int type = annotationEncoder.encoderType();
                String encryptStr = null;
                if (type == DesensitizationAnnotationEncoder.ENCODER_TYPE_AES) {
                    try {
                        encryptStr = decryptAes(methodValue);
                    } catch (Exception e) {

                    }
                }
                if (type == DesensitizationAnnotationEncoder.ENCODER_TYPE_REPLACE) {
                    try {
                        encryptStr = decryptReplace(methodValue);
                    } catch (Exception e) {

                    }
                }
                Method setMethod = clz.getMethod(method.getName().replace("get", "set"), String.class);
                setMethod.invoke(obj, encryptStr);
            }
        }
    }

    public static String encryptAes(String str) {
        try {
            return AesEncryptUtil.aesEncode(encryptAesKey, str);
        } catch (Exception e) {

        }
        return null;
    }

    public static String decryptAes(String str) {
        try {
            String res = AesEncryptUtil.aesDncode(encryptAesKey, str);
            return res;
        } catch (Exception e) {

        }
        return null;
    }

    public static String encryptReplace(String str) {
        return CnEncryptUtil.replaceCnEncode(str, false);
    }

    public static String decryptReplace(String str) {
        return CnEncryptUtil.replaceCnDncode(str);
    }

    public static String dealXingXing(String str, int prefix, int suffix) {
        if (str == null) {
            return null;
        }
        String res = null;
        if (str.length() > (prefix + suffix)) {
            res = str.substring(0, prefix) + createXingXing(str.length() - prefix - suffix) + str.substring(str.length() - suffix, str.length());
        } else if (str.length() > prefix) {
            res = str.substring(0, prefix) + createXingXing(str.length() - prefix);
        } else {
            res = createXingXing(prefix);
        }
        return res;
    }

    public static String createXingXing(int len) {
        String xing = "";
        for (int i = 0; i < len; i++) {
            xing += "*";
        }
        return xing;
    }
}
