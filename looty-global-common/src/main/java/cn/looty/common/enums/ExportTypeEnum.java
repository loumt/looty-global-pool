package cn.looty.common.enums;

import cn.hutool.core.util.RandomUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Filename: ExportTypeEnum
 * @Description: 导出枚举
 * @Version: 1.0
 * @Author: louMT
 * @Email: looty_loumt@hotmail.com
 * @Date: 2024-08-20 15:50
 */
public enum ExportTypeEnum {
    EXAMPLE("EXAMPLE", "示例"),
    ;

    private final String type;
    private final String sheetName;

    ExportTypeEnum(String type, String sheetName) {
        this.type = type;
        this.sheetName = sheetName;
    }

    public String toFileName() {
        StringBuffer fn = new StringBuffer();
        fn.append(this.toSheetName());
        fn.append("_");
        fn.append(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
        fn.append("_");
        fn.append(RandomUtil.randomString(6).toUpperCase());
        return fn.toString();
    }

    public String toSheetName() {
        return this.sheetName;
    }

    public String getType() {
        return this.type;
    }
}
