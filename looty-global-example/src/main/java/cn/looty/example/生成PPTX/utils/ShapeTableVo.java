package cn.looty.example.生成PPTX.utils;

import lombok.Data;
import org.apache.poi.xslf.usermodel.XSLFTable;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class ShapeTableVo {
    private XSLFTable shape;
    private double x;
    private double y;
    private double w;
    private double h;

    public ShapeTableVo(XSLFTable table) {
        this.shape = table;
        if (table.getAnchor() != null) {
            this.x = table.getAnchor().getX();
            this.y = table.getAnchor().getY();
            this.w = table.getAnchor().getWidth();
            this.h = table.getAnchor().getHeight();
        }
    }

    public BigDecimal getHeight() {
        if (this.shape.getRows().size() > 0) {
            double rowHeight = this.shape.getRows().get(0).getHeight();
            return new BigDecimal(rowHeight).multiply(new BigDecimal(this.shape.getRows().size())).setScale(0, RoundingMode.UP);
        }
        return BigDecimal.ZERO;
    }
}
