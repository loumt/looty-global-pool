package cn.looty.example.生成PPTX.utils;

import lombok.Data;
import org.apache.poi.xslf.usermodel.XSLFTextBox;

@Data
public class ShapeTextBoxVo {
    private XSLFTextBox shape;
    private double x;
    private double y;
    private double w;
    private double h;

    public ShapeTextBoxVo(XSLFTextBox textBox) {
        this.shape = textBox;
        if (textBox.getAnchor() != null) {
            this.x = textBox.getAnchor().getX();
            this.y = textBox.getAnchor().getY();
            this.w = textBox.getAnchor().getWidth();
            this.h = textBox.getAnchor().getHeight();
        }
    }
}
