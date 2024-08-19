package cn.looty.example.生成PPTX.utils;

import lombok.Data;

import java.awt.*;

@Data
public class ProductColumnTextVo {
    private String text;
    private Color textColor;
    private boolean isBold;

    public ProductColumnTextVo(String text) {
        this.text = StringUtil.isEmpty(text) ? "/" : text;
        this.textColor = Color.DARK_GRAY;
        this.isBold = false;
    }


    public ProductColumnTextVo(String text, Color textColor) {
        this.text = StringUtil.isEmpty(text) ? "/" : text;
        this.textColor = textColor;
        this.isBold = true;
    }
}
