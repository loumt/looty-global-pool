package cn.looty.example.生成PPTX.utils;

import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AxisParamVo {
    private Double minimum;
    private Double maximum;
    private Double majorUnit;
    private Double minorUnit;
    private List<Double> paramData;
    private String type;

    public Double toMinimum() {
        return this.minimum;
    }

    public Double toMaximum() {
        return this.maximum;
    }

    public Double toMajorUnit() {
        return this.majorUnit;
    }

    public Double toMinorUnit() {
        return this.minorUnit;
    }

    public AxisParamVo(String type) {
        this.type = type;
        this.paramData = new ArrayList<>();
    }

    public void addData(List<Double> data) {
        if (!CollectionUtils.isEmpty(data)) {
            data.forEach(d -> {
                if (d != null) {
                    this.paramData.add(d);
                }
            });
        }
    }

    public void calculate() {
        if (Objects.equals(this.type, RuiNingUtil.AXIS_LEFT_OVERAGE_YIELD)) {
            calculateLeftOverageYield();
        } else if (Objects.equals(this.type, RuiNingUtil.AXIS_RIGHT_OVERAGE_YIELD)) {
            calculateRightOverageYield();
        } else if (Objects.equals(this.type, RuiNingUtil.AXIS_LEFT_NEUTRAL)) {
            calculateLeftNeutral();
        } else if (Objects.equals(this.type, RuiNingUtil.AXIS_RIGHT_NEUTRAL)) {
            calculateRightNeutral();
        } else if (Objects.equals(this.type, RuiNingUtil.AXIS_LEFT_DMA)) {
            calculateLeftDma();
        } else if (Objects.equals(this.type, RuiNingUtil.AXIS_WEEKLY_STRATEGY)) {
            calculateWs();
        }
    }

    /**
     * 本周子策略及收益
     */
    private void calculateWs() {
        this.minimum = -0.005D;
        this.maximum = 0.01D;
        this.majorUnit = 0.005D;
        this.minorUnit = 0.001D;
        if (!CollectionUtils.isEmpty(this.paramData)) {
            Collections.sort(this.paramData);
            int len = this.paramData.size();
            Double min = null;
            for (int i = 0; i < len; i++) {
                if (this.paramData.get(i) != null) {
                    min = this.paramData.get(i);
                    break;
                }
            }
            Double max = null;
            for (int i = len - 1; i >= 0; i--) {
                if (this.paramData.get(i) != null) {
                    max = this.paramData.get(i);
                    break;
                }
            }
            if (min != null && max != null) {
                BigDecimal differenceValue = new BigDecimal("0.0001");
                BigDecimal oldMax = new BigDecimal(max);
                BigDecimal oldMin = new BigDecimal(min);
                BigDecimal decMaximum = oldMax.setScale(8, BigDecimal.ROUND_DOWN);
                BigDecimal decMinimum = oldMin.setScale(8, BigDecimal.ROUND_DOWN);
                while (true) {
                    decMaximum = decMaximum.add(differenceValue);
                    if (decMaximum.compareTo(oldMax) > -1) {
                        break;
                    }
                }
                while (true) {
                    decMinimum = decMinimum.subtract(differenceValue);
                    if (decMinimum.compareTo(oldMin) < 1) {
                        break;
                    }
                }
                BigDecimal poor = decMaximum.subtract(decMinimum).setScale(8, BigDecimal.ROUND_DOWN);
                BigDecimal mu = poor.divide(new BigDecimal(3), 8, BigDecimal.ROUND_HALF_UP);
                decMaximum = decMaximum.setScale(3, BigDecimal.ROUND_HALF_UP);
                mu = mu.setScale(3, BigDecimal.ROUND_HALF_UP);
                this.maximum = decMaximum.doubleValue();
                this.majorUnit = mu.doubleValue();
                decMinimum = decMaximum.subtract(mu.multiply(new BigDecimal(3))).setScale(3, BigDecimal.ROUND_HALF_UP);
                this.minimum = decMinimum.doubleValue();
                this.minorUnit = mu.divide(new BigDecimal(5), 3, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
        }
    }

    /**
     * 量化中性DMA产品，左轴
     */
    private void calculateLeftDma() {
        this.minimum = 0D;
        this.maximum = 0.2D;
        this.majorUnit = 0.04D;
        this.minorUnit = 0.008D;
        if (!CollectionUtils.isEmpty(this.paramData)) {
            Collections.sort(this.paramData);
            int len = this.paramData.size();
            Double min = null;
            for (int i = 0; i < len; i++) {
                if (this.paramData.get(i) != null) {
                    min = this.paramData.get(i);
                    break;
                }
            }
            Double max = null;
            for (int i = len - 1; i >= 0; i--) {
                if (this.paramData.get(i) != null) {
                    max = this.paramData.get(i);
                    break;
                }
            }
            if (min != null && max != null) {
                BigDecimal differenceValue = new BigDecimal("0.01");
                BigDecimal decMinimum = new BigDecimal(min).setScale(2, BigDecimal.ROUND_DOWN);
                if (decMinimum.compareTo(BigDecimal.ZERO) == 1) {
                    decMinimum = BigDecimal.ZERO;
                } else {
                    decMinimum = decMinimum.subtract(differenceValue);
                }
                BigDecimal decMaximum = new BigDecimal(max).setScale(2, BigDecimal.ROUND_DOWN).add(differenceValue);
                BigDecimal poor = decMaximum.subtract(decMinimum);
                BigDecimal mu = poor.divide(new BigDecimal(5), 4, BigDecimal.ROUND_HALF_UP);
                this.minimum = decMinimum.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
                this.maximum = decMaximum.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
                this.majorUnit = mu.doubleValue();
                this.minorUnit = mu.divide(new BigDecimal(6), 4, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
        }
    }

    /**
     * 建隆量化中性产品，左轴
     */
    private void calculateLeftNeutral() {
        this.minimum = 0.8D;
        this.maximum = 1.2D;
        this.majorUnit = 0.1D;
        this.minorUnit = 0.02D;
        if (!CollectionUtils.isEmpty(this.paramData)) {
            Collections.sort(this.paramData);
            int len = this.paramData.size();
            Double min = null;
            for (int i = 0; i < len; i++) {
                if (this.paramData.get(i) != null) {
                    min = this.paramData.get(i);
                    break;
                }
            }
            Double max = null;
            for (int i = len - 1; i >= 0; i--) {
                if (this.paramData.get(i) != null) {
                    max = this.paramData.get(i);
                    break;
                }
            }
            if (min != null && max != null) {
                BigDecimal differenceValue = new BigDecimal("0.1");
                BigDecimal decMinimum = new BigDecimal(min).setScale(2, BigDecimal.ROUND_DOWN).subtract(differenceValue);
                BigDecimal decMaximum = new BigDecimal(max).setScale(2, BigDecimal.ROUND_DOWN).add(differenceValue);
                BigDecimal poor = decMaximum.subtract(decMinimum);
                BigDecimal mu = poor.divide(new BigDecimal(4), 1, BigDecimal.ROUND_HALF_UP);
                this.minimum = decMinimum.setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                this.maximum = decMaximum.setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
                this.majorUnit = mu.doubleValue();
                this.minorUnit = mu.divide(new BigDecimal(5), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
        }
    }

    /**
     * 建隆量化中性产品，右轴
     */
    private void calculateRightNeutral() {
        this.minimum = -0.03D;
        this.maximum = 0.03D;
        this.majorUnit = 0.015D;
        this.minorUnit = 0.003D;
        if (!CollectionUtils.isEmpty(this.paramData)) {
            Collections.sort(this.paramData);
            Double min = null;
            Double max = null;
            int len = this.paramData.size();
            for (int i = 0; i < len; i++) {
                if (this.paramData.get(i) != null) {
                    min = this.paramData.get(i);
                    break;
                }
            }
            for (int i = len - 1; i >= 0; i--) {
                if (this.paramData.get(i) != null) {
                    max = this.paramData.get(i);
                    break;
                }
            }
            if (min != null && max != null) {
                BigDecimal differenceValue = new BigDecimal("0.01");
                BigDecimal decMinimum = new BigDecimal(min).setScale(2, BigDecimal.ROUND_DOWN).subtract(differenceValue);
                BigDecimal decMaximum = new BigDecimal(max).setScale(2, BigDecimal.ROUND_UP).add(differenceValue);
                if (decMinimum.compareTo(BigDecimal.ZERO) == -1 && decMaximum.compareTo(BigDecimal.ZERO) == 1) {
                    BigDecimal abcMinimum = decMinimum.abs();
                    BigDecimal absMaximum = decMaximum.abs();
                    if (abcMinimum.compareTo(absMaximum) == 1) {
                        decMaximum = abcMinimum;
                    } else if (abcMinimum.compareTo(absMaximum) == -1) {
                        decMinimum = new BigDecimal("-" + absMaximum.toPlainString());
                    }
                }
                BigDecimal poor = decMaximum.subtract(decMinimum);
                BigDecimal mu = poor.divide(new BigDecimal(4), 4, BigDecimal.ROUND_DOWN);
                this.minimum = decMinimum.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
                this.maximum = decMaximum.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
                this.majorUnit = mu.doubleValue();
                this.minorUnit = mu.divide(new BigDecimal(5), 4, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
        }
    }

    /**
     * 乾德500/1000指增产品，左轴
     */
    private void calculateLeftOverageYield() {
        this.minimum = 0.64D;
        this.maximum = 1.36D;
        this.majorUnit = 0.18D;
        this.minorUnit = 0.036D;
        setParamForLeft();
    }

    /**
     * 乾德500/1000指增产品，右轴
     */
    private void calculateRightOverageYield() {
        this.minimum = -0.36D;
        this.maximum = 0.36D;
        this.majorUnit = 0.18D;
        this.minorUnit = 0.036D;
        setParamForRight();
    }

    private void setParamForLeft() {
        if (!CollectionUtils.isEmpty(this.paramData)) {
            Collections.sort(this.paramData);
            Double min = null;
            Double max = null;
            int len = this.paramData.size();
            for (int i = 0; i < len; i++) {
                if (this.paramData.get(i) != null) {
                    min = this.paramData.get(i);
                    break;
                }
            }
            for (int i = len - 1; i >= 0; i--) {
                if (this.paramData.get(i) != null) {
                    max = this.paramData.get(i);
                    break;
                }
            }
            if (min != null && max != null) {
                BigDecimal differenceValue = new BigDecimal("0.01");
                BigDecimal decMinimum = new BigDecimal(min).setScale(2, BigDecimal.ROUND_DOWN).subtract(differenceValue);
                BigDecimal decMaximum = new BigDecimal(max).setScale(2, BigDecimal.ROUND_UP).add(differenceValue);
                BigDecimal mu = null;
                if (decMaximum.compareTo(BigDecimal.ONE) == 1 && decMinimum.compareTo(BigDecimal.ONE) == -1) {
                    // 最大值大于1 & 最小值小于1
                    BigDecimal poor = decMaximum.subtract(BigDecimal.ONE);
                    if (BigDecimal.ONE.subtract(decMinimum).compareTo(poor) == 1) {
                        poor = BigDecimal.ONE.subtract(decMinimum);
                    }
                    BigDecimal two = new BigDecimal(2);
                    mu = poor.divide(two, 2, BigDecimal.ROUND_HALF_UP);
                    this.minimum = BigDecimal.ONE.subtract(mu.multiply(two)).doubleValue();
                    this.maximum = BigDecimal.ONE.add(mu.multiply(two)).doubleValue();
                    this.majorUnit = mu.doubleValue();
                } else {
                    BigDecimal poor = decMaximum.subtract(decMinimum);
                    mu = poor.divide(new BigDecimal(4), 2, BigDecimal.ROUND_DOWN);
                    this.minimum = decMinimum.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
                    this.maximum = decMaximum.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
                    this.majorUnit = mu.doubleValue();
                }
                this.minorUnit = mu.divide(new BigDecimal(5), 3, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
        }
    }

    private void setParamForRight() {
        if (!CollectionUtils.isEmpty(this.paramData)) {
            Collections.sort(this.paramData);
            Double min = null;
            Double max = null;
            int len = this.paramData.size();
            for (int i = 0; i < len; i++) {
                if (this.paramData.get(i) != null) {
                    min = this.paramData.get(i);
                    break;
                }
            }
            for (int i = len - 1; i >= 0; i--) {
                if (this.paramData.get(i) != null) {
                    max = this.paramData.get(i);
                    break;
                }
            }
            if (min != null && max != null) {
                BigDecimal differenceValue = new BigDecimal("0.01");
                BigDecimal decMinimum = new BigDecimal(min).setScale(2, BigDecimal.ROUND_DOWN).subtract(differenceValue);
                BigDecimal decMaximum = new BigDecimal(max).setScale(2, BigDecimal.ROUND_UP).add(differenceValue);
                BigDecimal poor = decMaximum.subtract(BigDecimal.ZERO);
                if (BigDecimal.ZERO.subtract(decMinimum).compareTo(poor) == 1) {
                    poor = BigDecimal.ZERO.subtract(decMinimum);
                }
                BigDecimal two = new BigDecimal(2);
                BigDecimal mu = poor.divide(two, 2, BigDecimal.ROUND_HALF_UP);
                this.minimum = BigDecimal.ZERO.subtract(mu.multiply(two)).doubleValue();
                this.maximum = BigDecimal.ZERO.add(mu.multiply(two)).doubleValue();
                this.majorUnit = mu.doubleValue();
                this.minorUnit = mu.divide(new BigDecimal(5), 3, BigDecimal.ROUND_HALF_UP).doubleValue();
//                BigDecimal differenceValue = new BigDecimal("0.01");
//                BigDecimal decMinimum = new BigDecimal(min).setScale(2, BigDecimal.ROUND_DOWN).subtract(differenceValue);
//                BigDecimal decMaximum = new BigDecimal(max).setScale(2, BigDecimal.ROUND_UP).add(differenceValue);
//                BigDecimal poor = decMaximum.subtract(decMinimum);
//                BigDecimal mu = poor.divide(new BigDecimal(4), 2, BigDecimal.ROUND_DOWN);
//                this.minimum = decMinimum.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
//                this.maximum = decMaximum.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
//                this.majorUnit = mu.doubleValue();
//                this.minorUnit = mu.divide(new BigDecimal(5), 3, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
        }
    }

}
