package cn.looty.example.生成PPTX.utils;

import java.awt.*;

public enum RuiNingFontEnum {
    // 微软雅黑_灰色_12
    MICROSOFT_BLACK_12_GRAY("微软雅黑", 12D, 102, 102, 102),
    // 微软雅黑_灰色_12
    SONG_09_GRAY("宋体", 12D, 102, 102, 102),

    WHITE("微软雅黑", 12D, 255, 255, 255),
    ;

    private final String fontType;
    private final double fontSize;
    private final byte[] fontColor;

    RuiNingFontEnum(String fontType, double fontSize, int red, int green, int blue) {
        this.fontType = fontType;
        this.fontSize = fontSize;
        this.fontColor = new byte[]{(byte) red, (byte) green, (byte) blue};
    }

    public String toType() {
        return this.fontType;
    }

    public double toSize() {
        return this.fontSize;
    }

    public byte[] toColor() {
        return this.fontColor;
    }

    public Color getColor() {
        return new Color((int) this.fontColor[0], (int) this.fontColor[1], (int) this.fontColor[2]);
    }
}
