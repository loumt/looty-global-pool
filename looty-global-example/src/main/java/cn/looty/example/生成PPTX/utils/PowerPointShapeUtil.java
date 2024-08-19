package cn.looty.example.生成PPTX.utils;

import lombok.Getter;
import org.apache.poi.xslf.usermodel.*;
import org.springframework.util.CollectionUtils;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public class PowerPointShapeUtil {
    private Map<String, ShapeVo> shapeMap;
    private BigDecimal poor;
    private int pptWidth;
    private BigDecimal pptHeight;
    private int exp500Row;
    private int exp1000Row;
    private int dmaRow;

    public PowerPointShapeUtil(List<XSLFShape> shapes) {
        this.shapeMap = new HashMap<>();
        this.poor = BigDecimal.ZERO;
        Map<String, PowerPointShapeEnum> ppsMap = Arrays.stream(PowerPointShapeEnum.values())
                .collect(Collectors.toMap(PowerPointShapeEnum::toShapeName, pps -> pps));
        for (XSLFShape shape : shapes) {
            PowerPointShapeEnum pps = null;
            if (ppsMap.containsKey(shape.getShapeName())) {
                pps = ppsMap.get(shape.getShapeName());
            }
            if (pps == null) {
                Class clazz = null;
                String text = null;
                if (shape instanceof XSLFTextBox) {
                    clazz = XSLFTextBox.class;
                    List<XSLFTextParagraph> texts = ((XSLFTextBox) shape).getTextParagraphs();
                    if (!CollectionUtils.isEmpty(texts)) {
                        StringBuffer allText = new StringBuffer();
                        for (XSLFTextParagraph paragraph : texts) {
                            allText.append(paragraph.getText());
                        }
                        text = allText.toString();
                    }
                } else if (shape instanceof XSLFTable) {
                    clazz = XSLFTable.class;
                    XSLFTable table = (XSLFTable) shape;
                    if (table.getRows().size() > 1) {
                        List<XSLFTableCell> cells = table.getRows().get(1).getCells();
                        if (!CollectionUtils.isEmpty(cells)) {
                            List<XSLFTextParagraph> texts = cells.get(0).getTextParagraphs();
                            if (!CollectionUtils.isEmpty(texts)) {
                                text = texts.get(0).getText();
                            }
                        }
                    }
                } else if (shape instanceof XSLFAutoShape) {
                    clazz = XSLFAutoShape.class;
                    XSLFAutoShape auto = (XSLFAutoShape) shape;
                    List<XSLFTextParagraph> texts = auto.getTextParagraphs();
                    if (!CollectionUtils.isEmpty(texts)) {
                        text = texts.get(0).getText();
                    }
                }
                pps = toMoreShape(shape.getShapeName(), text, clazz);
            }
            if (pps != null) {
                this.shapeMap.put(pps.code(), new ShapeVo(shape, pps.code()));
            }
        }
    }

    public void setPageSize(Dimension d) {
        this.pptWidth = d.width;
        this.pptHeight = new BigDecimal(d.height);
    }

    public void setRowCount(int exp500Row, int exp1000Row, int dmaRow) {
        this.exp500Row = exp500Row;
        this.exp1000Row = exp1000Row;
        this.dmaRow = dmaRow;
    }

    public int toPowerPointWidth() {
        return this.pptWidth;
    }

    public int toPowerPointHeight() {
        int height = pptHeight.add(poor).setScale(0, RoundingMode.UP).intValue();
        if (height > 4030) {
            return 4030;
        }
        return height;
    }

    private PowerPointShapeEnum toMoreShape(String shapeName, String text, Class clazz) {
        if (clazz == null || text == null) {
            return null;
        }
        Optional<PowerPointShapeEnum> shapeOptional = Arrays.stream(PowerPointShapeEnum.values())
                .filter(pps -> Objects.equals(pps.toClass(), clazz) &&
                        StringUtil.containsIgnoreCase(pps.toShapeName(), shapeName) &&
                        Objects.equals(text, pps.toText())).findFirst();
        if (shapeOptional.isPresent()) {
            return shapeOptional.get();
        }
        return null;
    }

    public boolean isExist(PowerPointShapeEnum shapeEnum) {
        return this.shapeMap.containsKey(shapeEnum.code()) && this.shapeMap.get(shapeEnum.code()) != null;
    }

    public void moveDown(PowerPointShapeEnum pps) {
        if (!isExist(pps)) {
            return;
        }
        if (Objects.equals(pps, PowerPointShapeEnum.GRID_PRODUCT) ||
                Objects.equals(pps, PowerPointShapeEnum.GRID_QD500) ||
                Objects.equals(pps, PowerPointShapeEnum.GRID_QD1000) ||
                Objects.equals(pps, PowerPointShapeEnum.GRID_DMA)) {
            ShapeVo shapeVo = this.shapeMap.get(pps.code());
            BigDecimal tableHeightPoor = shapeVo.getTableHeightPoor();
            poor = poor.add(tableHeightPoor);
            if (Objects.equals(pps, PowerPointShapeEnum.GRID_PRODUCT)) {
                if (tableHeightPoor.compareTo(BigDecimal.ZERO) == 1) {
                    poor = poor.add(new BigDecimal(32));
                }
                move(toMoveDownForProduct());
            } else if (Objects.equals(pps, PowerPointShapeEnum.GRID_QD500)) {
                move(toMoveDownForQD500());
            } else if (Objects.equals(pps, PowerPointShapeEnum.GRID_QD1000)) {
                move(toMoveDownForQD1000());
            } else if (Objects.equals(pps, PowerPointShapeEnum.GRID_DMA)) {
                move(toMoveDownForDma());
            }
        }
    }

    public void moveDownEvaluate(int observeParagraphCount) {
        if (!isExist(PowerPointShapeEnum.OBS_MARKET_STYLE)) {
            return;
        }
        if (observeParagraphCount <= 6) {
            return;
        }
        double leftMaxY = this.shapeMap.get(PowerPointShapeEnum.OBS_GROUP_FRAME_LEFT.code())
                .getGroup().getAnchor().getMaxY();
        BigDecimal h = toFrameHeight(observeParagraphCount);
        change(h);
        double dottedMaxY = this.shapeMap.get(PowerPointShapeEnum.EVA_DOTTED_LINE_UP.code()).getConnector().getAnchor().getMaxY();
        double dottedY = (h.intValue() - 169) + leftMaxY;
        BigDecimal heightPoor = new BigDecimal(dottedY).subtract(new BigDecimal(dottedMaxY)).setScale(0, RoundingMode.UP);
        poor = poor.add(h.add(heightPoor));
        move(toMoveDownForObserve());
    }

    public void moveDownContent(int evaluateParagraphCount) {
        if (evaluateParagraphCount <= 5) {
            return;
        }
        if (!isExist(PowerPointShapeEnum.EVA_EVALUATE)) {
            return;
        }
        ShapeVo shapeVo = this.shapeMap.get(PowerPointShapeEnum.EVA_EVALUATE.code());
        BigDecimal oldHeight = new BigDecimal(shapeVo.getH());
        BigDecimal eachHeight = oldHeight.divide(new BigDecimal(5), 4, BigDecimal.ROUND_HALF_UP);
        BigDecimal currentHeight = new BigDecimal(evaluateParagraphCount).multiply(eachHeight).setScale(0, RoundingMode.UP);
        if (currentHeight.compareTo(oldHeight) == 1) {
            BigDecimal heightPoor = currentHeight.subtract(oldHeight).setScale(0, RoundingMode.UP);
            poor = poor.add(heightPoor);
            move(toMoveDownForEvaluate());
        }
    }

    private void move(List<PowerPointShapeEnum> shapeEnums) {
        if (CollectionUtils.isEmpty(shapeEnums)) {
            return;
        }
        shapeEnums.forEach(pps -> {
            ShapeVo shapeVo = this.shapeMap.get(pps.code());

            if(shapeVo == null) System.out.println(" shapeMap 中无" + pps.code());
            if(shapeVo != null) moveShape(shapeVo);
        });
    }

    private void change(BigDecimal h) {
        changeFrame(PowerPointShapeEnum.OBS_GROUP_FRAME_RIGHT, h, "矩形 52", "矩形 65");
        changeFrame(PowerPointShapeEnum.OBS_GROUP_FRAME_CENTER, h, "矩形 92", "矩形 94");
        changeFrame(PowerPointShapeEnum.OBS_GROUP_FRAME_LEFT, h, "矩形 13", "矩形 14");
    }

    private void changeFrame(PowerPointShapeEnum shapeEnum, BigDecimal h, String childFrameName, String childLineName) {
        XSLFGroupShape rightShape = (XSLFGroupShape) this.shapeMap.get(shapeEnum.code()).getShape();
        List<XSLFShape> rightShapes = rightShape.getShapes();
        XSLFAutoShape frame = null;
        XSLFAutoShape line = null;
        for (XSLFShape rs : rightShapes) {
            if (Objects.equals(rs.getShapeName(), childFrameName)) {
                frame = (XSLFAutoShape) rs;
            } else if (StringUtil.isNotEmpty(childLineName) && Objects.equals(rs.getShapeName(), childLineName)) {
                line = (XSLFAutoShape) rs;
            }
        }
        setFrame(frame, line, h);
    }

    private BigDecimal toFrameHeight(int observeParagraphCount) {
        return new BigDecimal(observeParagraphCount).multiply(new BigDecimal(13))
                .setScale(0, RoundingMode.UP);
    }

    private int toLineY(double frameY, BigDecimal h) {
        return new BigDecimal(frameY).add(h).subtract(new BigDecimal(1.946299))
                .setScale(0, RoundingMode.UP).intValue();
    }

    private void setFrame(XSLFAutoShape frame, XSLFAutoShape line, BigDecimal h) {
        Rectangle2D frameAnchor = frame.getAnchor();
        double frameY = frameAnchor.getY();
        int x = new BigDecimal(frameAnchor.getX()).setScale(0, RoundingMode.UP).intValue();
        int y = new BigDecimal(frameY).setScale(0, RoundingMode.UP).intValue();
        int w = new BigDecimal(frameAnchor.getWidth()).setScale(0, RoundingMode.UP).intValue();
        frame.setAnchor(new Rectangle(x, y, w, h.intValue()));

        Rectangle2D lineAnchor = line.getAnchor();
        int lx = new BigDecimal(lineAnchor.getX()).setScale(0, RoundingMode.UP).intValue();
        int lw = new BigDecimal(lineAnchor.getWidth()).setScale(0, RoundingMode.UP).intValue();
        line.setAnchor(new Rectangle(lx, toLineY(frameY, h), lw, 2));
    }

    public int toVariableValue(String code) {
        if (RuiNingUtil.toDespCode().contains(code)) {
            if (this.exp500Row > 0) {
                if (Objects.equals(code, PowerPointShapeEnum.QD500_CHART_DESP.code())) {
                    return this.exp500Row * 31;
                } else if (Objects.equals(code, PowerPointShapeEnum.QD500_CHART_CPJZ.code()) ||
                        Objects.equals(code, PowerPointShapeEnum.QD500_CHART_CESYL.code())) {
                    return this.exp500Row * 16;
                }
            }

            if (this.exp1000Row > 0) {
                if (Objects.equals(code, PowerPointShapeEnum.QD1000_CHART_DESP.code())) {
                    return this.exp1000Row * 31;
                } else if (Objects.equals(code, PowerPointShapeEnum.QD1000_CHART_CPJZ.code()) ||
                        Objects.equals(code, PowerPointShapeEnum.QD1000_CHART_CESYL.code())) {
                    return this.exp1000Row * 16;
                }
            }

            if (this.dmaRow > 0) {
                if (Objects.equals(code, PowerPointShapeEnum.DMA_CHART_DESP.code())) {
                    return this.dmaRow * 31;
                } else if (Objects.equals(code, PowerPointShapeEnum.DMA_CHART_CPLJSYL.code())) {
                    return this.dmaRow * 16;
                }
            }
        }
        return 0;
    }

    private void moveShape(ShapeVo shapeVo) {
        Rectangle rectangle = new Rectangle(shapeVo.getX(), shapeVo.getMoveY(this.poor, toVariableValue(shapeVo.getCode())), shapeVo.getW(), shapeVo.getH());
        if (shapeVo.getShape() instanceof XSLFConnectorShape) {
            shapeVo.getConnector().setAnchor(rectangle);
        } else if (shapeVo.getShape() instanceof XSLFTextBox) {
            shapeVo.getTextBox().setAnchor(rectangle);
        } else if (shapeVo.getShape() instanceof XSLFTable) {
            shapeVo.getTable().setAnchor(rectangle);
        } else if (shapeVo.getShape() instanceof XSLFGroupShape) {
            shapeVo.getGroup().setAnchor(rectangle);
        } else if (shapeVo.getShape() instanceof XSLFAutoShape) {
            shapeVo.getAuto().setAnchor(rectangle);
        }
    }

    private List<PowerPointShapeEnum> toMoveDownForProduct() {
        List<PowerPointShapeEnum> shapeEnums = new ArrayList<>();
        shapeEnums.addAll(Arrays.stream(PowerPointShapeEnum.values())
                .filter(pps -> !Objects.equals(pps.code(), "GRID_PRODUCT")).collect(Collectors.toList()));
        return shapeEnums;
    }

    private List<PowerPointShapeEnum> toMoveDownForQD500() {
        List<PowerPointShapeEnum> shapeEnums = new ArrayList<>();
        List<String> groups = Arrays.asList(
                PowerPointShapeEnum.QD1000_DOTTED_LINE_UP.toGroup(),
                PowerPointShapeEnum.DMA_DOTTED_LINE_UP.toGroup(),
                PowerPointShapeEnum.OBS_DOTTED_LINE_UP.toGroup(),
                PowerPointShapeEnum.EVA_DOTTED_LINE_UP.toGroup(),
                PowerPointShapeEnum.REL_DOTTED_LINE_UP.toGroup());
        shapeEnums.addAll(Arrays.stream(PowerPointShapeEnum.values())
                .filter(pps -> groups.contains(pps.toGroup())).collect(Collectors.toList()));
        return shapeEnums;
    }

    private List<PowerPointShapeEnum> toMoveDownForQD1000() {
        List<PowerPointShapeEnum> shapeEnums = new ArrayList<>();
        List<String> groups = Arrays.asList(
                PowerPointShapeEnum.DMA_DOTTED_LINE_UP.toGroup(),
                PowerPointShapeEnum.OBS_DOTTED_LINE_UP.toGroup(),
                PowerPointShapeEnum.EVA_DOTTED_LINE_UP.toGroup(),
                PowerPointShapeEnum.REL_DOTTED_LINE_UP.toGroup());
        shapeEnums.addAll(Arrays.stream(PowerPointShapeEnum.values())
                .filter(pps -> groups.contains(pps.toGroup())).collect(Collectors.toList()));
        return shapeEnums;
    }

    private List<PowerPointShapeEnum> toMoveDownForDma() {
        List<PowerPointShapeEnum> shapeEnums = new ArrayList<>();
        List<String> groups = Arrays.asList(
                PowerPointShapeEnum.OBS_DOTTED_LINE_UP.toGroup(),
                PowerPointShapeEnum.EVA_DOTTED_LINE_UP.toGroup(),
                PowerPointShapeEnum.REL_DOTTED_LINE_UP.toGroup());
        shapeEnums.addAll(Arrays.stream(PowerPointShapeEnum.values())
                .filter(pps -> groups.contains(pps.toGroup())).collect(Collectors.toList()));
        return shapeEnums;
    }

    private List<PowerPointShapeEnum> toMoveDownForObserve() {
        List<PowerPointShapeEnum> shapeEnums = new ArrayList<>();
        List<String> groups = Arrays.asList(
                PowerPointShapeEnum.EVA_DOTTED_LINE_UP.toGroup(),
                PowerPointShapeEnum.REL_DOTTED_LINE_UP.toGroup());
        shapeEnums.addAll(Arrays.stream(PowerPointShapeEnum.values())
                .filter(pps -> groups.contains(pps.toGroup())).collect(Collectors.toList()));
        return shapeEnums;
    }

    private List<PowerPointShapeEnum> toMoveDownForEvaluate() {
        List<PowerPointShapeEnum> shapeEnums = new ArrayList<>();
        shapeEnums.addAll(Arrays.stream(PowerPointShapeEnum.values())
                .filter(pps -> Objects.equals(pps.toGroup(), PowerPointShapeEnum.REL_DOTTED_LINE_UP.toGroup()))
                .collect(Collectors.toList()));
        return shapeEnums;
    }
}
