package cn.looty.example.生成PPTX.utils;

import cn.hutool.core.util.RandomUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public enum RuiNingColorEnum {
    // 曲线 蓝色
    BLACK(0, 0, 0),
    // 曲线 蓝色
    BLUE(91, 155, 213),
    // 曲线 灰色
    GRAY(191, 191, 191),
    // 曲线 玫红
    ROSE_RED(192, 0, 0),

    // 坐标轴文字/面积 浅灰色
    LIGHT_GRAY(217, 217, 217),

    ;

    private final byte[] tone;

    RuiNingColorEnum(int red, int green, int blue) {
        this.tone = new byte[]{(byte) red, (byte) green, (byte) blue};
    }

    public byte[] tone() {
        return this.tone;
    }
}
