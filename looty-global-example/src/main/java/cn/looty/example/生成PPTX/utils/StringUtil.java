package cn.looty.example.生成PPTX.utils;


import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

public class StringUtil {
    public static boolean isNotEmpty(String value) {
        return (null != value && !"null".equals(value.toLowerCase(Locale.ENGLISH)) && !value.isEmpty());
    }

    public static boolean isEmpty(String value) {
        return !isNotEmpty(value);
    }

    public static boolean containsIgnoreCase(String value, String searchValue) {
        if (isEmpty(value)) {
            return false;
        }
        if (isEmpty(searchValue)) {
            return true;
        }
        return StringUtils.containsIgnoreCase(value, searchValue);
    }
}
