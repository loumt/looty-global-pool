package cn.looty.example.生成PPTX.utils;

import lombok.Getter;
import org.apache.poi.xslf.usermodel.XSLFGroupShape;

@Getter
public class ShapeGroupVo {
    private XSLFGroupShape shape;
    private double x;
    private double y;
    private double w;
    private double h;

    public ShapeGroupVo(XSLFGroupShape groupShape) {
        this.shape = groupShape;
        if (groupShape.getAnchor() != null) {
            this.x = groupShape.getAnchor().getX();
            this.y = groupShape.getAnchor().getY();
            this.w = groupShape.getAnchor().getWidth();
            this.h = groupShape.getAnchor().getHeight();
        }
    }
}
