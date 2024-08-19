package cn.looty.example.生成PPTX.utils;

import lombok.Getter;
import org.apache.poi.xslf.usermodel.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;


public class ShapeVo {
    private String code;
    private XSLFShape shape;
    private double x;
    private double y;
    private double w;
    private double h;
    private double originalHeight;

    public ShapeVo(XSLFShape shape, String code) {
        this.shape = shape;
        this.code = code;
        if (shape.getAnchor() != null) {
            this.x = shape.getAnchor().getX();
            this.y = shape.getAnchor().getY();
            this.w = shape.getAnchor().getWidth();
            this.h = shape.getAnchor().getHeight();
            this.originalHeight = shape.getAnchor().getHeight();
        }
    }

    public String getCode() {
        return code;
    }

    public XSLFShape getShape() {
        return shape;
    }

    public XSLFTable getTable() {
        if (this.shape instanceof XSLFTable) {
            return (XSLFTable) this.shape;
        }
        return null;
    }

    public XSLFConnectorShape getConnector() {
        if (this.shape instanceof XSLFConnectorShape) {
            return (XSLFConnectorShape) this.shape;
        }
        return null;
    }

    public XSLFTextBox getTextBox() {
        if (this.shape instanceof XSLFTextBox) {
            return (XSLFTextBox) this.shape;
        }
        return null;
    }

    public XSLFGroupShape getGroup() {
        if (this.shape instanceof XSLFGroupShape) {
            return (XSLFGroupShape) this.shape;
        }
        return null;
    }

    public XSLFAutoShape getAuto() {
        if (this.shape instanceof XSLFAutoShape) {
            return (XSLFAutoShape) this.shape;
        }
        return null;
    }

    public int getX() {
        return doubleToDecimal(this.x).intValue();
    }

    public int getY() {
        return doubleToDecimal(this.y).intValue();
    }

    public int getW() {
        return doubleToDecimal(this.w).intValue();
    }

    public int getH() {
        return doubleToDecimal(this.h).intValue();
    }

    public int getMoveY(BigDecimal poor, int variableValue) {
        if (variableValue > 0) {
            return poor.add(new BigDecimal(this.y)).add(new BigDecimal(variableValue)).setScale(0, RoundingMode.UP).intValue();
        } else {
            return poor.add(new BigDecimal(this.y)).setScale(0, RoundingMode.UP).intValue();
        }
    }

//    public int getMoveY(BigDecimal poor) {
//        return poor.add(new BigDecimal(this.y)).setScale(0, RoundingMode.UP).intValue();
//    }

    private BigDecimal doubleToDecimal(double v) {
        if (v <= 0) {
            return BigDecimal.ZERO;
        } else if (v <= 1) {
            return BigDecimal.ONE;
        } else {
            return new BigDecimal(v).setScale(0, RoundingMode.UP);
        }
    }

    public BigDecimal getTableHeightPoor() {
        if (this.shape instanceof XSLFTable) {
            XSLFTable table = (XSLFTable) this.shape;
            BigDecimal oldHeight = new BigDecimal(this.originalHeight);
            BigDecimal currentHeight = new BigDecimal("0");
            if (table.getRows().size() > 0) {
                for (XSLFTableRow row : table.getRows()) {
                    currentHeight = currentHeight.add(new BigDecimal(row.getHeight()));
                }
            }
            if (currentHeight.compareTo(oldHeight) == 1) {
                return currentHeight.subtract(oldHeight).setScale(0, RoundingMode.UP);
            }
        }
        return BigDecimal.ZERO;
    }

}
