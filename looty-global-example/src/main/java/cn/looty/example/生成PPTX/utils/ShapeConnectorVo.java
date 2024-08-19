package cn.looty.example.生成PPTX.utils;

import lombok.Data;
import lombok.Getter;
import org.apache.poi.xslf.usermodel.XSLFConnectorShape;

@Getter
public class ShapeConnectorVo {

    private XSLFConnectorShape shape;
    private double x;
    private double y;
    private double w;
    private double h;

    public ShapeConnectorVo(XSLFConnectorShape connectorShape) {
        this.shape = connectorShape;
        if (connectorShape.getAnchor() != null) {
            this.x = connectorShape.getAnchor().getX();
            this.y = connectorShape.getAnchor().getY();
            this.w = connectorShape.getAnchor().getWidth();
            this.h = connectorShape.getAnchor().getHeight();
        }
    }
}
