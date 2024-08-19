package cn.looty.example.生成PPTX.utils;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public enum RectangleEnum {
    // 建隆量化中性产品 左折线
    NEUTRAL_LEFT(1200000, 5830000, 4800000, 2200000, 454.324960D),
    // 建隆量化中性产品 右面积
    NEUTRAL_RIGHT(1460300, 5865000, 4160000, 2000000, 454.324960D),
    // 乾德500指增产品 左折线
    EI_500_LEFT(1200000, 8920000, 4800000, 2200000, 706.75D),
    // 乾德500指增产品 右折线
//    EI_500_RIGHT(1580000, 8802000, 4058000, 2200000, 706.75D),
    EI_500_RIGHT(1580000, 8920000, 4178000, 2280000, 706.75D),
    // 乾德1000指增产品 左折线
    EI_1000_LEFT(1200000, 12083000, 4800000, 2200000, 937.10D),
    // 乾德1000指增产品 右折线
    EI_1000_RIGHT(1580000, 12083000, 4058000, 2280000, 937.10D),
//    EI_1000_RIGHT(1580000, 11980000, 4058000, 2200000, 937.10D),
    // 量化中性DMA产品 左折线
    DMA_LEFT(1200000, 15542000, 4710000, 2200000, 1177.224960),
    // 本周子策略及收益
    WEEKLY_STRATEGY(7200000, 5100000, 4000000, 4000000, 400.00D);

    private final int x;
    private final int y;
    private final int w;
    private final int h;
    private final double iy;

    RectangleEnum(int x, int y, int w, int h, double iy) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.iy = iy;
    }


    public Rectangle toPoorSite(BigDecimal poor, int residualValue) {
        int axisY = this.y;
        int height = this.h + residualValue;
        if (poor != null && poor.compareTo(BigDecimal.ZERO) == 1) {
            int py = new BigDecimal(this.y).divide(new BigDecimal(this.iy), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(poor).setScale(0, RoundingMode.UP).intValue();
            axisY += py;
        }
        return new Rectangle(this.x, axisY, this.w, height);
    }
}
