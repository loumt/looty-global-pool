package cn.looty.example.生成PPTX.utils;

import cn.looty.example.生成PPTX.model.*;
import org.apache.poi.common.usermodel.fonts.FontGroup;
import org.apache.poi.sl.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.*;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xddf.usermodel.text.XDDFFont;
import org.apache.poi.xddf.usermodel.text.XDDFRunProperties;
import org.apache.poi.xslf.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.drawingml.x2006.chart.*;
import org.openxmlformats.schemas.drawingml.x2006.main.*;
import org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape;
import org.openxmlformats.schemas.presentationml.x2006.main.CTShape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ChartUtil {
    private static Logger log = LoggerFactory.getLogger(ChartUtil.class);

    private static String DEFAULT_PPTX_TEMPLATE_FILE_PATH;
    private static String DEFAULT_ATTACHMENT_LOCAL_PATH;
    private static boolean CONTAINS_DATA_AFTER_MARCH;
    private static boolean LINE_CHART_ADAPTIVE_HEIGHT;

    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    private final static SimpleDateFormat monthFormat = new SimpleDateFormat("yy/MM");

    public static void initParam(String defaultAttachmentLocalPath, String defaultPptxTemplateFilePath,
                                 boolean containsDataAfterMarch, boolean lineChartAdaptiveHeight) {
        ChartUtil.DEFAULT_ATTACHMENT_LOCAL_PATH = defaultAttachmentLocalPath;
        ChartUtil.DEFAULT_PPTX_TEMPLATE_FILE_PATH = defaultPptxTemplateFilePath;
        ChartUtil.CONTAINS_DATA_AFTER_MARCH = containsDataAfterMarch;
        ChartUtil.LINE_CHART_ADAPTIVE_HEIGHT = lineChartAdaptiveHeight;
        log.info("图表落地文件路径 >>> " + ChartUtil.DEFAULT_ATTACHMENT_LOCAL_PATH);
        log.info("周报PPT模板文件路径 >>> " + ChartUtil.DEFAULT_PPTX_TEMPLATE_FILE_PATH);
        log.info("表格中是否包含3月迭代以来数据 >>> " + ChartUtil.CONTAINS_DATA_AFTER_MARCH);
        log.info("乾德指增&量化中性产品曲线图是否高度自适应 >>> " + ChartUtil.LINE_CHART_ADAPTIVE_HEIGHT);
    }

    public static void toDownload(String fileName, HttpServletResponse response) {
        response.reset();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        if (isExist(fileName)) {
            loadResponseOutputStream(fileName, response);
        }
    }

    public static void toProductBenefitForExcel(ProductBenefitLineDto body, HttpServletResponse response) {
        String fileName = ChartKeyUtil.newProductBenefitKey(body) + ".XLSX";
        response.reset();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        if (isExist(fileName)) {
            loadResponseOutputStream(fileName, response);
            return;
        }

        OutputStream responseOutputStream = null;
        OutputStream outPutStream = null;
        XSSFWorkbook wb = null;
        try {
            wb = new XSSFWorkbook();
            String sheetName = "产品净值";
            XSSFCellStyle titleStyle = toCellStyle(wb, false, true, "微软雅黑", 13, null, HorizontalAlignment.LEFT);
            XSSFCellStyle textStyle = toCellStyle(wb, true, false, "微软雅黑", 11, null, HorizontalAlignment.LEFT);
            XSSFCellStyle warnStyle = toCellStyle(wb, false, false, "微软雅黑", 8, new XSSFColor(Color.RED), HorizontalAlignment.LEFT);
            XSSFCellStyle remarkStyle = toCellStyle(wb, false, false, "微软雅黑", 9, null, HorizontalAlignment.CENTER);
            XSSFSheet sheet = wb.createSheet(sheetName);
            int startRow = 2;
            addTable(sheet, startRow, 9, body, "产品净值", textStyle, warnStyle, titleStyle, remarkStyle);
            addProductBenefitLine(sheet, body, startRow);
            // 文件落地
            outPutStream = new FileOutputStream(DEFAULT_ATTACHMENT_LOCAL_PATH + File.separator + fileName);
            wb.write(outPutStream);
            outPutStream.flush();

            // 将文件输出
            responseOutputStream = response.getOutputStream();
            wb.write(responseOutputStream);
            responseOutputStream.flush();
        } catch (Exception e) {
            log.info("PRODUCT BENEFIT ERROR >>> " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (wb != null) {
                    wb.close();
                }
                if (outPutStream != null) {
                    outPutStream.close();
                }
                if (responseOutputStream != null) {
                    responseOutputStream.close();
                }
            } catch (IOException e) {
                log.info("PRODUCT BENEFIT CLOSE STREAM ERROR >>> " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void toWeeklyReportForPowerPoint(WeeklyReportDto body, HttpServletResponse response) {
        String fileName = ChartKeyUtil.newWeeklyReportKey(body) + ".PPTX";
        response.reset();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.setHeader("reportFileKey", fileName);
        if (isExist(fileName)) {
            loadResponseOutputStream(fileName, response);
            return;
        }

        OutputStream responseOutputStream = null;
        OutputStream outPutStream = null;
        XMLSlideShow pptx = null;
        try {
            FileInputStream fileInput = new FileInputStream(DEFAULT_PPTX_TEMPLATE_FILE_PATH);
            pptx = new XMLSlideShow(fileInput);
            pptCommentHandle(pptx, body, getRuiningVo(body));
            outPutStream = new FileOutputStream(DEFAULT_ATTACHMENT_LOCAL_PATH + File.separator + fileName.toUpperCase(Locale.ENGLISH));
            pptx.write(outPutStream);
            outPutStream.flush();

            //将文件输出
            responseOutputStream = response.getOutputStream();
            pptx.write(responseOutputStream);
            responseOutputStream.flush();
        } catch (Exception e) {
            log.info("WEEKLY REPORT ERROR >>> " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (pptx != null) {
                    pptx.close();
                }
                if (outPutStream != null) {
                    outPutStream.close();
                }
                if (responseOutputStream != null) {
                    responseOutputStream.close();
                }
            } catch (IOException e) {
                log.info("WEEKLY REPORT CLOSE STREAM ERROR >>> " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    private static void loadResponseOutputStream(String fileName, HttpServletResponse response) {
        InputStream fis = null;
        OutputStream responseOutputStream = null;
        try {
            fis = new BufferedInputStream(new FileInputStream(DEFAULT_ATTACHMENT_LOCAL_PATH + File.separator + fileName));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            responseOutputStream = new BufferedOutputStream(response.getOutputStream());
            responseOutputStream.write(buffer);
            responseOutputStream.flush();
        } catch (IOException e) {
            log.info("LOADING LOCAL FILE >>> " + e.getMessage());
            log.info("LOCAL FILE NAME>>> " + fileName);
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (responseOutputStream != null) {
                    responseOutputStream.close();
                }
            } catch (IOException e) {
                log.info("LOADING LOCAL FILE CLOSE STREAM ERROR >>> " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static boolean isExist(String fileName) {
        File file = new File(DEFAULT_ATTACHMENT_LOCAL_PATH + File.separator + fileName);
        return file.exists() && file.isFile();
    }

    private static void addTable(XSSFSheet sheet, int startRow, int startCell,
                                 ProductBenefitLineDto body, String remark,
                                 XSSFCellStyle textStyle, XSSFCellStyle warnStyle, XSSFCellStyle titleStyle, XSSFCellStyle remarkStyle) {
        if (CollectionUtils.isEmpty(body.getGridItems()) && CollectionUtils.isEmpty(body.getWarns())) {
            return;
        }

        int rowCount = -1;
        for (int i = 0; i < startRow; i++) {
            rowCount++;
            Row row = sheet.createRow(rowCount);
            row.setHeight((short) 400);
            if (rowCount == 0) {
                Cell titleCell = row.createCell(0);
                titleCell.setCellValue("·" + body.getProductName());
                titleCell.setCellStyle(titleStyle);
            }
            sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 10));
        }

        if (!CollectionUtils.isEmpty(body.getGridItems())) {
            for (int i = 0; i < body.getGridItems().size(); i++) {
                rowCount++;
                ChartGridDto item = body.getGridItems().get(i);
                Row row = sheet.createRow(rowCount);
                row.setHeight((short) 400);

                Cell nameCell = row.createCell(startCell);
                nameCell.setCellValue(item.getName());
                nameCell.setCellStyle(textStyle);
                sheet.setColumnWidth(startCell, 4000);

                Cell valueCell = row.createCell(startCell + 1);
                valueCell.setCellValue(item.getValue());
                valueCell.setCellStyle(textStyle);
                sheet.setColumnWidth(startCell + 1, 8000);
            }
        }

        if (!CollectionUtils.isEmpty(body.getWarns())) {
            for (int i = 0; i < body.getWarns().size(); i++) {
                rowCount++;
                Row row = sheet.createRow(rowCount);
                row.setHeight((short) 400);
                Cell warnCell = row.createCell(startCell);
                warnCell.setCellValue(body.getWarns().get(i));
                warnCell.setCellStyle(warnStyle);
                sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, startCell, startCell + 1));
            }
        }

        if (rowCount < 11) {
            for (int i = rowCount + 1; i < 12; i++) {
                Row row = sheet.createRow(i);
                row.setHeight((short) 400);
            }
        }

        if (StringUtil.isNotEmpty(remark)) {
            Row row = sheet.getRow(startRow);
            Cell remarkCell = row.createCell(0);
            remarkCell.setCellValue(remark);
            remarkStyle.setRotation((short) 0xff);
            remarkCell.setCellStyle(remarkStyle);
            sheet.addMergedRegion(new CellRangeAddress(startRow, startRow + 9, 0, 0));
        }
    }

    private static void addProductBenefitLine(XSSFSheet sheet, ProductBenefitLineDto body, int startRow) {
        List<ChartDataDto> netWorthArr = body.getNetWorthArr();
        int len = CollectionUtils.isEmpty(netWorthArr) ? 0 : body.getNetWorthArr().size();
        String[] dateList = new String[len];
        Double[] netWorthList = new Double[len];
        if (!CollectionUtils.isEmpty(netWorthArr)) {
            for (int i = 0; i < len; i++) {
                dateList[i] = toDate(netWorthArr.get(i).getDate(), dateFormat, monthFormat);
                netWorthList[i] = netWorthArr.get(i).getNw();
            }
        }

        // 创建一个画布
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 1, startRow, 8, startRow + 10);
        // 创建一个chart对象
        XSSFChart chart = drawing.createChart(anchor);
        CTPlotArea plotArea = chart.getCTChart().getPlotArea();

        int skipNum = dateList.length / 6;
        // 分类轴标(X轴),标题位置
        XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        CTCatAx catAx = plotArea.getCatAxArray(0);
        catAx.addNewTickLblSkip().setVal(skipNum);    // label only every second mark
        catAx.addNewTickMarkSkip().setVal(skipNum);  // show only every second mark
        catAx.addNewLblOffset().setVal(100);    // label offset to the axis, possible values: 0 to 1000
        // X轴，深灰色
        catAx.addNewSpPr().addNewLn().addNewSolidFill().addNewSrgbClr().setVal(new byte[]{(byte) 217, (byte) 217, (byte) 217});
        setFont(bottomAxis.getOrAddTextProperties(), RuiNingFontEnum.SONG_09_GRAY);

        // 值(Y轴)轴,标题位置
        List<Double> nws = toSortByAsc(body.getNetWorthArr().stream()
                .map(ChartDataDto::getNw).collect(Collectors.toList()));
        Double minimum = 0.97D;
        Double maximum = 1.15D;
        Double majorUnit = 0.02D;
        Double minorUnit = 0.004D;
        if (!CollectionUtils.isEmpty(nws)) {
            Double originalMin = nws.get(0);
            Double originalMax = nws.get(nws.size() - 1);
            BigDecimal oldMin = new BigDecimal(originalMin);
            BigDecimal oldMax = new BigDecimal(originalMax);
            BigDecimal poor = new BigDecimal(0.001);
            BigDecimal min = oldMin.subtract(poor).setScale(3, BigDecimal.ROUND_HALF_UP);
            BigDecimal max = oldMax.add(poor).setScale(3, BigDecimal.ROUND_HALF_UP);
            BigDecimal nine = new BigDecimal(9);
            BigDecimal interval = max.subtract(min).divide(nine, 3, BigDecimal.ROUND_HALF_UP);

            while (true) {
                BigDecimal newMax = interval.multiply(nine).add(min).setScale(3, BigDecimal.ROUND_HALF_UP);
                if (newMax.compareTo(newMax) > -1) {
                    max = newMax;
                    break;
                }
                interval = interval.add(poor);
            }
            minimum = min.stripTrailingZeros().doubleValue();
            maximum = max.stripTrailingZeros().doubleValue();
            majorUnit = interval.stripTrailingZeros().doubleValue();
            minorUnit = interval.divide(new BigDecimal(5), 4, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().doubleValue();
        }

        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        leftAxis.setMinimum(minimum);
        leftAxis.setMaximum(maximum);
        leftAxis.setMajorUnit(majorUnit);
        leftAxis.setMinorUnit(minorUnit);
        leftAxis.setOrientation(AxisOrientation.MIN_MAX);
        leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
        leftAxis.setMajorTickMark(AxisTickMark.NONE);
        leftAxis.crossAxis(bottomAxis);
        setFont(leftAxis.getOrAddTextProperties(), RuiNingFontEnum.SONG_09_GRAY);

        CTValAx leftAx = plotArea.getValAxArray(0);
        // Y轴，无颜色
        leftAx.addNewSpPr().addNewLn().addNewNoFill();
        // Y轴网格线，浅灰色
        CTChartLines gridLine = leftAx.addNewMajorGridlines();
        gridLine.addNewSpPr().addNewLn().addNewSolidFill().addNewSrgbClr().setVal(new byte[]{(byte) 217, (byte) 217, (byte) 217});

        XDDFCategoryDataSource categoryData = XDDFDataSourcesFactory.fromArray(dateList);
        XDDFNumericalDataSource<Double> netWorthData = XDDFDataSourcesFactory.fromArray(netWorthList);
        // LINE：折线图，
        XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
        // 图表加载数据，折线1
        XDDFLineChartData.Series netWorthSeries = (XDDFLineChartData.Series) data.addSeries(categoryData, netWorthData);
        // 折线图例标题
        netWorthSeries.setTitle("", null);
        // 直线
        netWorthSeries.setSmooth(false);
        // 设置标记大小
        netWorthSeries.setMarkerSize((short) 3);
        // 设置标记样式，星星
        netWorthSeries.setMarkerStyle(MarkerStyle.NONE);
        setLineSeriesStyle(netWorthSeries, new byte[]{(byte) 192, (byte) 0, (byte) 0}, false, null);
        chart.getCTChartSpace().addNewSpPr().addNewLn().addNewNoFill();     // 外框不显示
        org.openxmlformats.schemas.drawingml.x2006.chart.CTManualLayout ctManualLayout = plotArea.getLayout().addNewManualLayout();
        ctManualLayout.addNewX().setVal(0);
        ctManualLayout.addNewY().setVal(0);
        ctManualLayout.addNewW().setVal(1);
        ctManualLayout.addNewH().setVal(1);
        // 绘制
        chart.plot(data);
    }

    private static XSSFCellStyle toCellStyle(XSSFWorkbook wb, boolean isBorder, boolean isBold, String fontName,
                                             int fontSize, XSSFColor fontColor, HorizontalAlignment align) {
        XSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(align);
        cellStyle.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
        if (isBorder) {
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
        }

        XSSFFont font = wb.createFont();
        font.setFontName(fontName);
        font.setFontHeightInPoints((short) fontSize);
        if (fontColor != null) {
            font.setColor(fontColor);
        }
        font.setBold(isBold);
        cellStyle.setFont(font);
        return cellStyle;
    }

    private static void setFont(XDDFRunProperties theme, RuiNingFontEnum font) {
        theme.setFontSize(font.toSize());
        theme.setFillProperties(new XDDFSolidFillProperties(XDDFColor.from(font.toColor())));
        theme.setFonts(new XDDFFont[]{new XDDFFont(FontGroup.EAST_ASIAN, font.toType(), null, null, null)});
    }

    private static void setLineSeries(XDDFLineChartData.Series series, String title) {
        // 设置系列名
        if (StringUtil.isNotEmpty(title)) {
            series.setTitle(title, null);
        } else {
            series.setTitle("", null);
        }
        //折线是否圆滑
        series.setSmooth(true);
        //折线点大小
        series.setMarkerSize((short) 2);
        //折线点样式
        series.setMarkerStyle(MarkerStyle.NONE);
    }

    private static void setLineSeriesStyle(XDDFChartData.Series series, byte[] colorValue, boolean isDottedLine, Double width) {
        XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(colorValue));
        XDDFLineProperties line = new XDDFLineProperties();
        line.setFillProperties(fill);
        if (isDottedLine) {
            line.setPresetDash(new XDDFPresetLineDash(PresetLineDash.SYSTEM_DASH_DOT_DOT));
        }
        if (width != null) {
            line.setWidth(width);
        }
        XDDFShapeProperties properties = series.getShapeProperties();
        if (properties == null) {
            properties = new XDDFShapeProperties();
        }
        properties.setLineProperties(line);
        series.setShapeProperties(properties);
    }

    private static List<Double> toSortByAsc(List<Double> values) {
        if (CollectionUtils.isEmpty(values)) {
            return values;
        }

        Collections.sort(values);
        return values;
    }

    private static RuiningVo getRuiningVo(WeeklyReportDto body) {
        RuiningVo vo = new RuiningVo();
        vo.setRangeDate(body.getRangeDate());
        if (StringUtil.isNotEmpty(body.getMarketStyle())) {
            vo.setMarketStyle(body.getMarketStyle());
        }
        if (StringUtil.isNotEmpty(body.getStyleFactor())) {
            vo.setStyleFactor(body.getStyleFactor());
        }
        if (StringUtil.isNotEmpty(body.getFuturesBasis())) {
            vo.setFuturesBasis(body.getFuturesBasis());
        }
        if (StringUtil.isNotEmpty(body.getEvaluate())) {
            vo.setEvaluate(body.getEvaluate());
        }

        vo.setProducts(body.getProducts());
        ExponentRatioVo exp500 = new ExponentRatioVo();
        if (!CollectionUtils.isEmpty(body.getExp500())) {
            ExponentVo exponent = body.getExp500().get(0);
            exp500.setItemName(exponent.getName());
            exp500.setItemUp(exponent.getUp());
            exp500.setItemDown(exponent.getDown());
            exp500.setExponents(new ArrayList<>());
            if (body.getExp500().size() > 1) {
                for (int i = 1; i < body.getExp500().size(); i++) {
                    exp500.getExponents().add(body.getExp500().get(i));
                }
            }
        }
        vo.setExp500(exp500);

        ExponentRatioVo exp1000 = new ExponentRatioVo();
        if (!CollectionUtils.isEmpty(body.getExp1000())) {
            ExponentVo exponent = body.getExp1000().get(0);
            exp1000.setItemName(exponent.getName());
            exp1000.setItemUp(exponent.getUp());
            exp1000.setItemDown(exponent.getDown());
            exp1000.setExponents(new ArrayList<>());
            if (body.getExp1000().size() > 1) {
                for (int i = 1; i < body.getExp1000().size(); i++) {
                    exp1000.getExponents().add(body.getExp1000().get(i));
                }
            }
        }
        vo.setExp1000(exp1000);

        ExponentRatioVo dma = new ExponentRatioVo();
        if (!CollectionUtils.isEmpty(body.getDma())) {
            DmaExponentDto exponent = body.getDma().get(0);
            dma.setItemName(exponent.getName());
            dma.setItemUp(exponent.getDma1());
            dma.setItemDown(exponent.getDma2());
            dma.setExponents(new ArrayList<>());
            if (body.getDma().size() > 1) {
                for (int i = 1; i < body.getDma().size(); i++) {
                    DmaExponentDto exp = body.getDma().get(i);
                    ExponentVo dmaExp = new ExponentVo();
                    dmaExp.setName(exp.getName());
                    dmaExp.setUp(exp.getDma1());
                    dmaExp.setDown(exp.getDma2());
                    dma.getExponents().add(dmaExp);
                }
            }
        }
        vo.setDma(dma);
        String rangeDateForCe5 = toRangeDate(body.getCe5());
        String rangeDateForCe10 = toRangeDate(body.getCe10());
        String rangeDateForDma = toRangeDate(body.getDma2());
        vo.paramLoading(ChartUtil.LINE_CHART_ADAPTIVE_HEIGHT, rangeDateForCe5, rangeDateForCe10, rangeDateForDma);

        return vo;
    }

    private static String toRangeDate(List<ChartDataDto> data) {
        if (CollectionUtils.isEmpty(data) && data.size() > 0) {
            return "/";
        }

        ChartDataDto start = data.get(0);
        ChartDataDto end = data.get(data.size() - 1);
        return RuiNingUtil.stringToString2(start.getDate()) + "-" + RuiNingUtil.stringToString2(end.getDate());
    }

    public static String toDate(String day, SimpleDateFormat dateFormat, SimpleDateFormat monthFormat) {
        if (StringUtil.isEmpty(day)) {
            return "";
        }
        try {
            return monthFormat.format(dateFormat.parse(day));
        } catch (Exception e) {
            return "";
        }
    }

    private static void pptCommentHandle(XMLSlideShow pptx, WeeklyReportDto body, RuiningVo data) {
        try {
            Map<String, String> paramMap = data.toParamMap();
            PowerPointUtil powerPointUtil = new PowerPointUtil(pptx);
            // 遍历幻灯片
            List<XSLFSlide> slideList = pptx.getSlides();
            for (XSLFSlide slide : slideList) {
                // 1. 替换段落占位符
                // 1.1 获取所有的shape，并解析为文本段落
                List<XSLFShape> shapes = slide.getShapes();
                PowerPointShapeUtil powerPointShapeUtil = new PowerPointShapeUtil(shapes);
                powerPointShapeUtil.setPageSize(pptx.getPageSize());
                int exp500Row = data.getRowCountMap().get("EXP_500_ROW") - 5;
                int exp1000Row = data.getRowCountMap().get("EXP_1000_ROW") - 5;
                int dmaRow = data.getRowCountMap().get("DMA_ROW") - 5;
                powerPointShapeUtil.setRowCount(exp500Row, exp1000Row, dmaRow);
                List<XSLFTextParagraph> paragraphsFromSlide = new ArrayList<>();
                for (XSLFShape shape : shapes) {
                    List<XSLFTextParagraph> textParagraphs = powerPointUtil.parseParagraph(shape);
                    paragraphsFromSlide.addAll(textParagraphs);
                }

                // 1.2 替换文本段落中的占位符
                for (XSLFTextParagraph paragraph : paragraphsFromSlide) {
                    powerPointUtil.replaceTagInParagraph(paragraph, paramMap, -1);
                }

                // 2. 替换表格内占位符
                // 2.1 循环获取到表格的单元格，并获取到文本段落
                List<XSLFTable> allTableFromSlide = powerPointUtil.getAllTableFromSlide(slide);
                for (XSLFTable xslfTableRows : allTableFromSlide) {
                    List<XSLFTableRow> rows = xslfTableRows.getRows();
                    String tableName = null;
                    for (XSLFTableRow row : rows) {
                        for (XSLFTableCell cell : row.getCells()) {
                            List<XSLFTextParagraph> textParagraphs = cell.getTextParagraphs();
                            for (XSLFTextParagraph textParagraph : textParagraphs) {
                                String key = textParagraph.getText();
                                powerPointUtil.replaceTagInParagraph(textParagraph, paramMap, -1);
                                if (key != null && data.isLineSpacing(key)) {
                                    textParagraph.setLineSpacing(90d);
                                }
                                if (key != null && data.isAddTableRow(key)) {
                                    tableName = key;
                                }
                            }
                        }
                    }

                    if (StringUtil.isNotEmpty(tableName) && data.isAddTableRow(tableName)) {
                        boolean isMergeCell = false;
                        if (!ChartUtil.CONTAINS_DATA_AFTER_MARCH &&
                                (Objects.equals(tableName, "${exp10.item}")
                                        || Objects.equals(tableName, "${exp5.item}")
                                        || Objects.equals(tableName, PowerPointShapeEnum.GRID_DMA.toText()))) {
                            isMergeCell = true;
                        }
                        List<ExponentVo> vos = data.toExponents(tableName);
                        if (!CollectionUtils.isEmpty(vos)) {
                            addRows(xslfTableRows, vos, isMergeCell);
                        }
                    }
                }
                ShapeVo productShape = powerPointShapeUtil.getShapeMap().get(PowerPointShapeEnum.GRID_PRODUCT.code());
                if (productShape != null) {
                    addProductColumn(productShape.getTable(), data.isExistWeeklyYield(), data.getRemoveCellCount(), data.getCellCount(), data.toMaxTextLengths());
                }


                powerPointShapeUtil.moveDown(PowerPointShapeEnum.GRID_PRODUCT);
                int p = powerPointShapeUtil.getPoor().intValue();
                BigDecimal qd500Poor = powerPointShapeUtil.getPoor();
                powerPointShapeUtil.moveDown(PowerPointShapeEnum.GRID_QD500);
                BigDecimal qd1000Poor = powerPointShapeUtil.getPoor();
                powerPointShapeUtil.moveDown(PowerPointShapeEnum.GRID_QD1000);
                BigDecimal dmaPoor = powerPointShapeUtil.getPoor();
                powerPointShapeUtil.moveDown(PowerPointShapeEnum.GRID_DMA);
                powerPointShapeUtil.moveDownEvaluate(data.getObserveParagraphCount());
                powerPointShapeUtil.moveDownContent(data.getEvaluateParagraphCount());

                // 建隆量化中性产品曲线
                addChartForNeutral(pptx, slide, body.getCnd(), qd500Poor, powerPointShapeUtil);
                // 乾德500指增产品
                addChartForExponentialIncrease(pptx, slide, body.getCe5(), "EI500", qd500Poor, powerPointShapeUtil, exp500Row);
                // 乾德1000指增产品
                addChartForExponentialIncrease(pptx, slide, body.getCe10(), "EI1000", qd1000Poor, powerPointShapeUtil, exp1000Row);
                // 量化中性DMA产品
                addChartForDma(pptx, slide, body.getDma2(), dmaPoor, powerPointShapeUtil, dmaRow);
//                addChartForDma(pptx, slide, mergeDma(body.getDma1(), body.getDma2()), dmaPoor, powerPointShapeUtil, dmaRow);
                // 本周子策略及收益
                addChartForWeeklyStrategy(pptx, slide, body.getWss(), qd500Poor, p);
                pptx.setPageSize(new Dimension(powerPointShapeUtil.toPowerPointWidth(), powerPointShapeUtil.toPowerPointHeight()));
            }
        } catch (Exception e) {
            log.info("WEEKLY REPORT GENERATE ERROR >>> " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void addProductColumn(XSLFTable table, boolean isExistWeeklyYield, int removeCellCount, int cellCount, List<Integer> maxTextLengths) {
        XSLFTableRow tableRow1 = table.getRows().get(0);
        XSLFTableRow tableRow2 = table.getRows().get(1);
        XSLFTableRow tableRow3 = table.getRows().get(2);
        XSLFTableRow tableRow4 = table.getRows().get(3);
        XSLFTableRow tableRow5 = table.getRows().get(4);
        int valueColumnCount = 15;
        if (cellCount == 1) {
            tableRow1.mergeCells(0, valueColumnCount);
            tableRow2.mergeCells(0, valueColumnCount);
            tableRow3.mergeCells(0, valueColumnCount);
            tableRow4.mergeCells(0, valueColumnCount);
            tableRow5.mergeCells(0, valueColumnCount);
        } else if (cellCount == 2) {
            if (isExistWeeklyYield) {
                tableRow1.mergeCells(0, 2);
                tableRow2.mergeCells(0, 2);
                tableRow3.mergeCells(0, 2);
                tableRow4.mergeCells(0, 2);
                tableRow5.mergeCells(0, 2);
                tableRow1.mergeCells(3, valueColumnCount);
                tableRow2.mergeCells(3, valueColumnCount);
                tableRow3.mergeCells(3, valueColumnCount);
                tableRow4.mergeCells(3, valueColumnCount);
                tableRow5.mergeCells(3, valueColumnCount);
            } else {
                tableRow1.mergeCells(1, valueColumnCount);
                tableRow2.mergeCells(1, valueColumnCount);
                tableRow3.mergeCells(1, valueColumnCount);
                tableRow4.mergeCells(1, valueColumnCount);
                tableRow5.mergeCells(1, valueColumnCount);
            }
        } else if (cellCount == 3) {
            if (isExistWeeklyYield) {
                tableRow1.mergeCells(1, 2);
                tableRow2.mergeCells(1, 2);
                tableRow3.mergeCells(1, 2);
                tableRow4.mergeCells(1, 2);
                tableRow5.mergeCells(1, 2);
                tableRow1.mergeCells(3, valueColumnCount);
                tableRow2.mergeCells(3, valueColumnCount);
                tableRow3.mergeCells(3, valueColumnCount);
                tableRow4.mergeCells(3, valueColumnCount);
                tableRow5.mergeCells(3, valueColumnCount);
            } else {
                tableRow1.mergeCells(2, valueColumnCount);
                tableRow2.mergeCells(2, valueColumnCount);
                tableRow3.mergeCells(2, valueColumnCount);
                tableRow4.mergeCells(2, valueColumnCount);
                tableRow5.mergeCells(2, valueColumnCount);
            }
        } else {
            if (!isExistWeeklyYield) {
                int weeklyYieldColumnIndex = 3;
                int firstColumnIndex = 2;
                if (cellCount <= 2) {
                    firstColumnIndex = 1;
                }
                tableRow1.mergeCells(firstColumnIndex, weeklyYieldColumnIndex);
                tableRow2.mergeCells(firstColumnIndex, weeklyYieldColumnIndex);
                tableRow3.mergeCells(firstColumnIndex, weeklyYieldColumnIndex);
                tableRow4.mergeCells(firstColumnIndex, weeklyYieldColumnIndex);
                tableRow5.mergeCells(firstColumnIndex, weeklyYieldColumnIndex);
            }
            if (removeCellCount > 0) {
                valueColumnCount -= removeCellCount;
                int lastColumnIndex = 15;
                int firstColumnIndex = 15 - removeCellCount;
                tableRow1.mergeCells(firstColumnIndex, lastColumnIndex);
                tableRow2.mergeCells(firstColumnIndex, lastColumnIndex);
                tableRow3.mergeCells(firstColumnIndex, lastColumnIndex);
                tableRow4.mergeCells(firstColumnIndex, lastColumnIndex);
                tableRow5.mergeCells(firstColumnIndex, lastColumnIndex);
            }
            int count = isExistWeeklyYield ? valueColumnCount : valueColumnCount - 1;
            double columnWidth = new BigDecimal("735.5905511811").divide(new BigDecimal(count), 2, BigDecimal.ROUND_DOWN).doubleValue();

            int textCount = 0;
            int headCount = 0;
            if (cellCount == 10) {
                textCount = 9;
                headCount = 5;
            } else if (cellCount == 11) {
                textCount = 8;
                headCount = 4;
            } else if (cellCount == 12 || cellCount == 13) {
                textCount = 6;
                headCount = 3;
            } else if (cellCount > 13) {
                textCount = 6;
                headCount = 3;
            }

            if (!CollectionUtils.isEmpty(maxTextLengths) && maxTextLengths.size() == 5 &&
                    textCount > 0 && headCount > 0) {
                List<Integer> rowTextHeights = new ArrayList<>();
                for (int i = 0; i < maxTextLengths.size(); i++) {
                    int divisor = textCount;
                    if (i == 0) {
                        divisor = headCount;
                    }
                    if (maxTextLengths.get(i) > 0) {
                        int len = maxTextLengths.get(i);
                        int textRow = len / divisor;
                        if (len % divisor > 0) {
                            textRow++;
                        }
                        if (textRow == 1 || textRow == 2) {
                            rowTextHeights.add(0);
                        } else if (textRow == 3) {
                            rowTextHeights.add(61);
                        } else if (textRow == 4) {
                            rowTextHeights.add(75);
                        } else {
                            rowTextHeights.add(94);
                        }
                    } else {
                        rowTextHeights.add(0);
                    }
                }

                if (rowTextHeights.size() > 0) {
                    for (int i = 0; i < rowTextHeights.size(); i++) {
                        Integer h = rowTextHeights.get(i);
                        if (h != null && h > 0) {
                            table.getRows().get(i).setHeight(h);
                        }
                    }
                }
//                tableRow1.setHeight(94D);
//                tableRow2.setHeight(94D);
//                tableRow3.setHeight(94D);
//                tableRow4.setHeight(94D);

//            } else {
//                double height = tableRow1.getHeight();
//                if (cellCount < 6) {
//                    height = 55D;
//                } else if (cellCount < 10) {
//                    height = 75D;
//                } else if (cellCount >= 10 && cellCount < 14) {
//                    height = 100D;
//                } else if (cellCount >= 14) {
//                    height = 120D;
//                }
//                tableRow1.setHeight(height);
//                tableRow2.setHeight(height);
//                tableRow3.setHeight(height);
//                tableRow4.setHeight(height);
//                tableRow5.setHeight(height);
            }

            for (int c = 1; c < 16; c++) {
                if (!isExistWeeklyYield && c == 3) {
                    table.setColumnWidth(3, 0.01D);
                } else if (c <= valueColumnCount) {
                    table.setColumnWidth(c, columnWidth);
                } else {
                    table.setColumnWidth(c, 0.01D);
                }
            }
        }
    }

    private static void addChartForWeeklyStrategy(XMLSlideShow pptx, XSLFSlide slide, List<WeeklyStrategyDto> wss, BigDecimal poor, int p) {
        try {
            // 数据
            if (CollectionUtils.isEmpty(wss)) {
                return;
            }
            // 创建图表
            XSLFChart chart = pptx.createChart();
            // 创建数据表格
            XSSFSheet sheet = chart.getWorkbook().createSheet("WeeklyStrategy");
            int maxRow = wss.size();
            List<Double> wsParam = new ArrayList<>();
            for (int row = 1; row <= maxRow; row++) {
                int index = row - 1;
                WeeklyStrategyDto ws = wss.get(index);
                Double wv = 0D;
                String percent = "0\n";
                if (ws.getWv() != null) {
                    wv = ws.getWv();
                    percent = new BigDecimal(ws.getWv()).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString() + "%\n";
                }
                String wn = StringUtil.isEmpty(ws.getWn()) ? percent : percent + ws.getWn();

                XSSFRow rowData = sheet.getRow(row);
                if (rowData == null) {
                    rowData = sheet.createRow(row);
                }
                Cell wnCell = rowData.getCell(0);
                if (wnCell == null) {
                    wnCell = rowData.createCell(0);
                }
                wnCell.setCellValue(wn);

                Cell wvCell = rowData.getCell(1);
                if (wvCell == null) {
                    wvCell = rowData.createCell(1);
                }
                wvCell.setCellValue(wv);
                wsParam.add(wv);
            }

            RuiNingFontEnum microsoftFont = RuiNingFontEnum.MICROSOFT_BLACK_12_GRAY;
            // X轴
            XDDFCategoryAxis categoryAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
            setMajorFont(categoryAxis.getOrAddTextProperties());
            categoryAxis.setCrosses(AxisCrosses.MIN);
            // 标签样式
            XDDFRunProperties xAxisTheme = categoryAxis.getOrAddTextProperties();
            xAxisTheme.setFontSize(12D);
            xAxisTheme.setBold(true);
            xAxisTheme.setFillProperties(new XDDFSolidFillProperties(XDDFColor.from(microsoftFont.toColor())));
            xAxisTheme.setFonts(new XDDFFont[]{new XDDFFont(FontGroup.EAST_ASIAN, microsoftFont.toType(), null, null, null)});

            AxisParamVo leftAxisParam = new AxisParamVo(RuiNingUtil.AXIS_WEEKLY_STRATEGY);
            leftAxisParam.addData(wsParam);
            leftAxisParam.calculate();
            // Y轴
            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.RIGHT);
            leftAxis.crossAxis(categoryAxis);
            categoryAxis.crossAxis(leftAxis);
            leftAxis.setMinimum(leftAxisParam.toMinimum());
            leftAxis.setMaximum(leftAxisParam.toMaximum());
            leftAxis.setMajorUnit(leftAxisParam.toMajorUnit());
            leftAxis.setMinorUnit(leftAxisParam.toMinorUnit());
            leftAxis.setCrosses(AxisCrosses.MAX);
            leftAxis.setMajorTickMark(AxisTickMark.NONE);
            RuiNingFontEnum font = RuiNingFontEnum.WHITE;
            setFont(leftAxis.getOrAddTextProperties(), font);
            // 标签样式
            XDDFRunProperties yAxisTheme = leftAxis.getOrAddTextProperties();
            yAxisTheme.setFontSize(font.toSize());
            yAxisTheme.setFillProperties(new XDDFSolidFillProperties(XDDFColor.from(font.toColor())));
            yAxisTheme.setFonts(new XDDFFont[]{new XDDFFont(FontGroup.EAST_ASIAN, font.toType(), null, null, null)});
            leftAxis.setVisible(false);

            CTPlotArea plotArea = chart.getCTChart().getPlotArea();
            CTValAx leftAx = plotArea.getValAxArray(0);
            // 左轴轴线无颜色
            leftAx.addNewSpPr().addNewLn().addNewNoFill();
            // 网格线，浅灰色
            CTChartLines gridLine = leftAx.addNewMajorGridlines();
            gridLine.addNewSpPr().addNewLn().addNewSolidFill().addNewSrgbClr().setVal(RuiNingColorEnum.LIGHT_GRAY.tone());

            XDDFRadarChartData radar = (XDDFRadarChartData) chart.createData(ChartTypes.RADAR, categoryAxis, leftAxis);
            radar.setStyle(RadarStyle.FILLED);
            XDDFCategoryDataSource xAxisSource = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, maxRow, 0, 0));
            XDDFNumericalDataSource<Double> yAxisSource = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, maxRow, 1, 1));
            XDDFRadarChartData.Series series = (XDDFRadarChartData.Series) radar.addSeries(xAxisSource, yAxisSource);

            XDDFLineProperties lineProperties = new XDDFLineProperties();
            lineProperties.setWidth(3D);
            lineProperties.setFillProperties(new XDDFSolidFillProperties(XDDFColor.from(RuiNingColorEnum.BLUE.tone())));
            series.setLineProperties(lineProperties);

            XDDFShapeProperties shapeFillProperties = new XDDFShapeProperties();
            shapeFillProperties.setFillProperties(new XDDFSolidFillProperties(XDDFColor.from(RuiNingColorEnum.BLUE.tone())));
            series.setShapeProperties(shapeFillProperties);

            CTRadarChart radarSeries = plotArea.getRadarChartArray(0);
            CTRadarSer radarSer = radarSeries.getSerArray(0);
//            CTDLbls ctdLbls = radarSer.addNewDLbls();

            CTShapeProperties radarShape = radarSer.addNewSpPr();
            CTSolidColorFillProperties radarFill = radarShape.addNewSolidFill();
            CTSRgbColor radarRgb = radarFill.addNewSrgbClr();
            radarRgb.setVal(RuiNingColorEnum.BLUE.tone());
            radarRgb.addNewAlpha().setVal(80000);

            // 设置标记颜色
            CTMarker marker = radarSer.addNewMarker();
            marker.addNewSymbol().setVal(STMarkerStyle.NONE);
            CTShapeProperties shapeProperties = marker.addNewSpPr();
            // 边框颜色
            CTLineProperties borderProperties = shapeProperties.addNewLn();
            CTSolidColorFillProperties borderColor = borderProperties.addNewSolidFill();
            borderColor.addNewSrgbClr().setVal(RuiNingColorEnum.ROSE_RED.tone());
            // 填充颜色
            CTSolidColorFillProperties fillProperties = shapeProperties.addNewSolidFill();
            fillProperties.addNewSrgbClr().setVal(RuiNingColorEnum.GRAY.tone());
            chart.plot(radar);
            slide.addChart(chart, RectangleEnum.WEEKLY_STRATEGY.toPoorSite(poor, 0));

            XSLFTextBox textBox = slide.createTextBox();
            textBox.clearText();
            Rectangle anchor = new Rectangle(630, 470 + p, 100, 100);
            textBox.setAnchor(anchor);
            XSLFTextParagraph paragraph = textBox.addNewTextParagraph();
            paragraph.setTextAlign(TextParagraph.TextAlign.RIGHT);
            paragraph.setLineSpacing(169D);
            List<String> texts = Arrays.asList("1.0%\n", "0.5%\n", "0.0%\n", "-0.5%");
            for (String text : texts) {
                XSLFTextRun run = paragraph.addNewTextRun();
                run.setText(text);
                run.setFontFamily(microsoftFont.toType());
                run.setFontSize(microsoftFont.toSize());
                run.setFontColor(microsoftFont.getColor());
            }
        } catch (Exception e) {
            log.info("生成本周子策略及收益雷达图异常 >>> " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static List<DmaDataDto> mergeDma(List<ChartDataDto> dma1, List<ChartDataDto> dma2) {
        if (CollectionUtils.isEmpty(dma1) && CollectionUtils.isEmpty(dma2)) {
            return new ArrayList<>();
        }
        Set<String> date = new HashSet<>();
        date.addAll(dma1.stream().filter(dma -> StringUtil.isNotEmpty(dma.getDate()))
                .map(ChartDataDto::getDate).collect(Collectors.toSet()));
        date.addAll(dma2.stream().filter(dma -> StringUtil.isNotEmpty(dma.getDate()))
                .map(ChartDataDto::getDate).collect(Collectors.toSet()));

        List<DmaDataDto> data = new ArrayList<>();
        if (!CollectionUtils.isEmpty(date)) {
            List<String> startingDates = new ArrayList<>(date);
            Collections.sort(startingDates);
            for (String startingDate : startingDates) {
                List<ChartDataDto> startingDma1 = dma1.stream().filter(dma -> Objects.equals(startingDate, dma.getDate())).collect(Collectors.toList());
                List<ChartDataDto> startingDma2 = dma2.stream().filter(dma -> Objects.equals(startingDate, dma.getDate())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(startingDma1) && CollectionUtils.isEmpty(startingDma2)) {
                    continue;
                }

                int len = startingDma1.size();
                if (startingDma2.size() > len) {
                    len = startingDma2.size();
                }

                for (int i = 0; i < len; i++) {
                    Double dma1Val = null;
                    Double dma2Val = null;
                    if (startingDma1.size() > i) {
                        dma1Val = startingDma1.get(i).getCr();
                    }
                    if (startingDma2.size() > i) {
                        dma2Val = startingDma2.get(i).getCr();
                    }
                    data.add(new DmaDataDto(startingDate, dma1Val, dma2Val));
                }
            }
        }
        return data;
    }

    private static void addChartForDma(XMLSlideShow pptx, XSLFSlide slide, List<ChartDataDto> dmaData, BigDecimal poor, PowerPointShapeUtil powerPointShapeUtil, Integer rowCount) {
        try {
            // 数据
            if (CollectionUtils.isEmpty(dmaData)) {
                slide.removeShape(powerPointShapeUtil.getShapeMap().get(PowerPointShapeEnum.DMA_CHART_CPLJSYL.code()).getShape());
                slide.removeShape(powerPointShapeUtil.getShapeMap().get(PowerPointShapeEnum.DMA_CHART_DESP.code()).getShape());
                return;
            }
            // 创建图表
            XSLFChart chart = pptx.createChart();
            // 创建数据表格
            XSSFSheet sheet = chart.getWorkbook().createSheet("DMA");
            List<String> dates = dmaData.stream().map(ChartDataDto::getDate).collect(Collectors.toList());
            List<Double> dma1 = dmaData.stream().map(ChartDataDto::getCr).collect(Collectors.toList());
            List<Double> dma2 = dmaData.stream().map(ChartDataDto::getCr).collect(Collectors.toList());
            // 填充表格数据
            int maxRow = dates.size();
            for (int row = 1; row <= maxRow; row++) {
                XSSFRow rowData = sheet.getRow(row);
                if (rowData == null) {
                    rowData = sheet.createRow(row);
                }

                int index = row - 1;
                Cell dateCell = rowData.getCell(0);
                if (dateCell == null) {
                    dateCell = rowData.createCell(0);
                }
                dateCell.setCellValue(RuiNingUtil.dateToMonth(RuiNingUtil.stringToDate(dates.get(index))));
                if (index == 0) {
                    Double val1 = dma1.get(index) == null ? 0D : dma1.get(index);
                    setValue(rowData, 1, val1);

                    Double val2 = dma2.get(index) == null ? 0D : dma2.get(index);
                    setValue(rowData, 2, val2);
                } else {
                    setValue(rowData, 1, dma1.get(index));
                    setValue(rowData, 2, dma2.get(index));
                }
            }

            // 设置空数据时，图表生成时跳过
            CTPlotArea plotArea = chart.getCTChart().getPlotArea();
            chart.getCTChart().addNewDispBlanksAs().setVal(STDispBlanksAs.GAP);
            // 建立X轴
            XDDFCategoryAxis categoryAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
            setMajorFont(categoryAxis.getOrAddTextProperties());
            categoryAxis.setCrosses(AxisCrosses.MIN);
            int skipNum = maxRow / 5;
            CTCatAx catAx = plotArea.getCatAxArray(0);
            catAx.addNewTickLblSkip().setVal(skipNum);
            catAx.addNewTickMarkSkip().setVal(skipNum);
            catAx.addNewLblOffset().setVal(100);
            catAx.addNewSpPr().addNewLn().addNewSolidFill().addNewSrgbClr().setVal(RuiNingColorEnum.LIGHT_GRAY.tone());
            setFont(categoryAxis.getOrAddTextProperties(), RuiNingFontEnum.MICROSOFT_BLACK_12_GRAY);

            AxisParamVo axisParam = new AxisParamVo(RuiNingUtil.AXIS_LEFT_DMA);
//            axisParam.addData(dma1);
            axisParam.addData(dma2);
            axisParam.calculate();

            // 建立左轴，折线
            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
            leftAxis.crossAxis(categoryAxis);
            categoryAxis.crossAxis(leftAxis);
            leftAxis.setNumberFormat("0%");
            leftAxis.setMinimum(axisParam.toMinimum());
            leftAxis.setMaximum(axisParam.toMaximum());
            leftAxis.setMajorUnit(axisParam.toMajorUnit());
            leftAxis.setMinorUnit(axisParam.toMinorUnit());
            leftAxis.setCrosses(AxisCrosses.MAX);
            leftAxis.setMajorTickMark(AxisTickMark.NONE);
            setFont(leftAxis.getOrAddTextProperties(), RuiNingFontEnum.WHITE);
            CTValAx leftAx = plotArea.getValAxArray(0);
            // 左轴轴线无颜色
            leftAx.addNewSpPr().addNewLn().addNewNoFill();
            // 网格线，浅灰色
            CTChartLines gridLine = leftAx.addNewMajorGridlines();
            gridLine.addNewSpPr().addNewLn().addNewSolidFill().addNewSrgbClr().setVal(RuiNingColorEnum.LIGHT_GRAY.tone());

            // 建立右轴，右折线
            XDDFValueAxis rightAxis = chart.createValueAxis(AxisPosition.RIGHT);
            rightAxis.crossAxis(categoryAxis);
            categoryAxis.crossAxis(rightAxis);
            rightAxis.setNumberFormat("0%");
            rightAxis.setMinimum(axisParam.toMinimum());
            rightAxis.setMaximum(axisParam.toMaximum());
            rightAxis.setMajorUnit(axisParam.toMajorUnit());
            rightAxis.setMinorUnit(axisParam.toMinorUnit());
            rightAxis.setCrosses(AxisCrosses.MIN);
            rightAxis.setMajorTickMark(AxisTickMark.NONE);
            setFont(rightAxis.getOrAddTextProperties(), RuiNingFontEnum.MICROSOFT_BLACK_12_GRAY);
            CTValAx rightAx = plotArea.getValAxArray(1);
            rightAx.addNewSpPr().addNewLn().addNewNoFill();
            chart.createData(ChartTypes.LINE, categoryAxis, rightAxis);

            XDDFDataSource<String> dateCategory = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, maxRow, 0, 0));
//            XDDFNumericalDataSource<Double> dma1Values = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, maxRow, 1, 1));
            XDDFNumericalDataSource<Double> dma2Values = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, maxRow, 2, 2));
            // 建立折现图的数据集
            XDDFChartData lineData = chart.createData(ChartTypes.LINE, categoryAxis, leftAxis);
            // 左轴，产品净值
//            XDDFLineChartData.Series dma1Series = (XDDFLineChartData.Series) lineData.addSeries(dateCategory, dma1Values);
//            setLineSeries(dma1Series, null);
//            setLineSeriesStyle(dma1Series, RuiNingColorEnum.BLUE.tone(), false, 2D);
//            dma1Series.plot();
            // 左轴，3月迭代以来的产品净值
            XDDFLineChartData.Series dma2Series = (XDDFLineChartData.Series) lineData.addSeries(dateCategory, dma2Values);
            setLineSeries(dma2Series, null);
            setLineSeriesStyle(dma2Series, RuiNingColorEnum.ROSE_RED.tone(), false, 1.75D);
            dma2Series.plot();
            //设置图表的位置和宽高
            int residualValue = 0;
            if (rowCount > 0) {
                residualValue = rowCount * 410000;
            }
            slide.addChart(chart, RectangleEnum.DMA_LEFT.toPoorSite(poor, residualValue));
        } catch (Exception e) {
            log.info("生成量化中性DMA产品曲线异常 >>> " + e.getMessage());
            e.printStackTrace();
        }
    }

//    private static void addChartForDma(XMLSlideShow pptx, XSLFSlide slide, List<DmaDataDto> dma, BigDecimal poor, PowerPointShapeUtil powerPointShapeUtil, Integer rowCount) {
//        try {
//            // 数据
//            if (CollectionUtils.isEmpty(dma)) {
//                slide.removeShape(powerPointShapeUtil.getShapeMap().get(PowerPointShapeEnum.DMA_CHART_CPLJSYL.code()).getShape());
//                slide.removeShape(powerPointShapeUtil.getShapeMap().get(PowerPointShapeEnum.DMA_CHART_DESP.code()).getShape());
//                return;
//            }
//            // 创建图表
//            XSLFChart chart = pptx.createChart();
//            // 创建数据表格
//            XSSFSheet sheet = chart.getWorkbook().createSheet("DMA");
//            List<String> dates = dma.stream().map(DmaDataDto::getDate).collect(Collectors.toList());
//            List<Double> dma1 = dma.stream().map(DmaDataDto::getDma1).collect(Collectors.toList());
//            List<Double> dma2 = dma.stream().map(DmaDataDto::getDma2).collect(Collectors.toList());
//            // 填充表格数据
//            int maxRow = dates.size();
//            for (int row = 1; row <= maxRow; row++) {
//                XSSFRow rowData = sheet.getRow(row);
//                if (rowData == null) {
//                    rowData = sheet.createRow(row);
//                }
//
//                int index = row - 1;
//                Cell dateCell = rowData.getCell(0);
//                if (dateCell == null) {
//                    dateCell = rowData.createCell(0);
//                }
//                dateCell.setCellValue(RuiNingUtil.dateToMonth(RuiNingUtil.stringToDate(dates.get(index))));
//                if (index == 0) {
//                    Double val1 = dma1.get(index) == null ? 0D : dma1.get(index);
//                    setValue(rowData, 1, val1);
//
//                    Double val2 = dma2.get(index) == null ? 0D : dma2.get(index);
//                    setValue(rowData, 2, val2);
//                } else {
//                    setValue(rowData, 1, dma1.get(index));
//                    setValue(rowData, 2, dma2.get(index));
//                }
//            }
//
//            // 设置空数据时，图表生成时跳过
//            CTPlotArea plotArea = chart.getCTChart().getPlotArea();
//            chart.getCTChart().addNewDispBlanksAs().setVal(STDispBlanksAs.GAP);
//            // 建立X轴
//            XDDFCategoryAxis categoryAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
//            setMajorFont(categoryAxis.getOrAddTextProperties());
//            categoryAxis.setCrosses(AxisCrosses.MIN);
//            int skipNum = maxRow / 5;
//            CTCatAx catAx = plotArea.getCatAxArray(0);
//            catAx.addNewTickLblSkip().setVal(skipNum);
//            catAx.addNewTickMarkSkip().setVal(skipNum);
//            catAx.addNewLblOffset().setVal(100);
//            catAx.addNewSpPr().addNewLn().addNewSolidFill().addNewSrgbClr().setVal(RuiNingColorEnum.LIGHT_GRAY.tone());
//            setFont(categoryAxis.getOrAddTextProperties(), RuiNingFontEnum.MICROSOFT_BLACK_12_GRAY);
//
//            AxisParamVo axisParam = new AxisParamVo(RuiNingUtil.AXIS_LEFT_DMA);
////            axisParam.addData(dma1);
//            axisParam.addData(dma2);
//            axisParam.calculate();
//
//            // 建立左轴，折线
//            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
//            leftAxis.crossAxis(categoryAxis);
//            categoryAxis.crossAxis(leftAxis);
//            leftAxis.setNumberFormat("0%");
//            leftAxis.setMinimum(axisParam.toMinimum());
//            leftAxis.setMaximum(axisParam.toMaximum());
//            leftAxis.setMajorUnit(axisParam.toMajorUnit());
//            leftAxis.setMinorUnit(axisParam.toMinorUnit());
//            leftAxis.setCrosses(AxisCrosses.MAX);
//            leftAxis.setMajorTickMark(AxisTickMark.NONE);
//            setFont(leftAxis.getOrAddTextProperties(), RuiNingFontEnum.WHITE);
//            CTValAx leftAx = plotArea.getValAxArray(0);
//            // 左轴轴线无颜色
//            leftAx.addNewSpPr().addNewLn().addNewNoFill();
//            // 网格线，浅灰色
//            CTChartLines gridLine = leftAx.addNewMajorGridlines();
//            gridLine.addNewSpPr().addNewLn().addNewSolidFill().addNewSrgbClr().setVal(RuiNingColorEnum.LIGHT_GRAY.tone());
//
//            // 建立右轴，右折线
//            XDDFValueAxis rightAxis = chart.createValueAxis(AxisPosition.RIGHT);
//            rightAxis.crossAxis(categoryAxis);
//            categoryAxis.crossAxis(rightAxis);
//            rightAxis.setNumberFormat("0%");
//            rightAxis.setMinimum(axisParam.toMinimum());
//            rightAxis.setMaximum(axisParam.toMaximum());
//            rightAxis.setMajorUnit(axisParam.toMajorUnit());
//            rightAxis.setMinorUnit(axisParam.toMinorUnit());
//            rightAxis.setCrosses(AxisCrosses.MIN);
//            rightAxis.setMajorTickMark(AxisTickMark.NONE);
//            setFont(rightAxis.getOrAddTextProperties(), RuiNingFontEnum.MICROSOFT_BLACK_12_GRAY);
//            CTValAx rightAx = plotArea.getValAxArray(1);
//            rightAx.addNewSpPr().addNewLn().addNewNoFill();
//            chart.createData(ChartTypes.LINE, categoryAxis, rightAxis);
//
//            XDDFDataSource<String> dateCategory = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, maxRow, 0, 0));
////            XDDFNumericalDataSource<Double> dma1Values = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, maxRow, 1, 1));
//            XDDFNumericalDataSource<Double> dma2Values = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, maxRow, 2, 2));
//            // 建立折现图的数据集
//            XDDFChartData lineData = chart.createData(ChartTypes.LINE, categoryAxis, leftAxis);
//            // 左轴，产品净值
////            XDDFLineChartData.Series dma1Series = (XDDFLineChartData.Series) lineData.addSeries(dateCategory, dma1Values);
////            setLineSeries(dma1Series, null);
////            setLineSeriesStyle(dma1Series, RuiNingColorEnum.BLUE.tone(), false, 2D);
////            dma1Series.plot();
//            // 左轴，3月迭代以来的产品净值
//            XDDFLineChartData.Series dma2Series = (XDDFLineChartData.Series) lineData.addSeries(dateCategory, dma2Values);
//            setLineSeries(dma2Series, null);
//            setLineSeriesStyle(dma2Series, RuiNingColorEnum.ROSE_RED.tone(), false, 2D);
//            dma2Series.plot();
//            //设置图表的位置和宽高
//            int residualValue = 0;
//            if (rowCount > 0) {
//                residualValue = rowCount * 410000;
//            }
//            slide.addChart(chart, RectangleEnum.DMA_LEFT.toPoorSite(poor, residualValue));
//        } catch (Exception e) {
//            log.info("生成量化中性DMA产品曲线异常 >>> " + e.getMessage());
//            e.printStackTrace();
//        }
//    }


    private static void addChartForExponentialIncrease(XMLSlideShow pptx, XSLFSlide slide, List<ChartDataDto> exponentialIncrease, String sheetName, BigDecimal poor, PowerPointShapeUtil powerPointShapeUtil, Integer rowCount) {
        try {
            // 数据
            if (CollectionUtils.isEmpty(exponentialIncrease)) {
                if (Objects.equals("EI500", sheetName)) {
                    slide.removeShape(powerPointShapeUtil.getShapeMap().get(PowerPointShapeEnum.QD500_CHART_CPJZ.code()).getShape());
                    slide.removeShape(powerPointShapeUtil.getShapeMap().get(PowerPointShapeEnum.QD500_CHART_CESYL.code()).getShape());
                    slide.removeShape(powerPointShapeUtil.getShapeMap().get(PowerPointShapeEnum.QD500_CHART_DESP.code()).getShape());
                } else {
                    slide.removeShape(powerPointShapeUtil.getShapeMap().get(PowerPointShapeEnum.QD1000_CHART_CPJZ.code()).getShape());
                    slide.removeShape(powerPointShapeUtil.getShapeMap().get(PowerPointShapeEnum.QD1000_CHART_CESYL.code()).getShape());
                    slide.removeShape(powerPointShapeUtil.getShapeMap().get(PowerPointShapeEnum.QD1000_CHART_DESP.code()).getShape());
                }
                return;
            }

            long deadlineTime = RuiNingUtil.toDeadlineTime();
            // 创建图表
            XSLFChart chart = pptx.createChart();
            // 创建数据表格
            XSSFSheet sheet = chart.getWorkbook().createSheet(sheetName);
            List<String> dates = exponentialIncrease.stream().map(ChartDataDto::getDate).collect(Collectors.toList());
            List<Double> netWorth = exponentialIncrease.stream().map(ChartDataDto::getNw).collect(Collectors.toList());
            List<Double> exponent = exponentialIncrease.stream().map(ChartDataDto::getInd).collect(Collectors.toList());
            List<Double> cer = exponentialIncrease.stream().map(ChartDataDto::getCer).collect(Collectors.toList());
            // 填充表格数据
            int maxRow = dates.size();
            for (int row = 1; row <= maxRow; row++) {
                XSSFRow rowData = sheet.getRow(row);
                if (rowData == null) {
                    rowData = sheet.createRow(row);
                }

                int index = row - 1;
                Cell dateCell = rowData.getCell(0);
                if (dateCell == null) {
                    dateCell = rowData.createCell(0);
                }
                Date date = RuiNingUtil.stringToDate(dates.get(index));
                boolean isPosterior = RuiNingUtil.isPosterior(date, deadlineTime);
                dateCell.setCellValue(RuiNingUtil.dateToMonth(date));
                setValue(rowData, 1, netWorth.get(index));
                if (index == 0) {
                    setValue(rowData, 2, netWorth.get(index));
                } else if (isPosterior) {
                    setValue(rowData, 2, netWorth.get(index));
                } else {
                    setValue(rowData, 2, null);
                }
                setValue(rowData, 3, exponent.get(index));
                if (index == 0) {
                    setValue(rowData, 4, exponent.get(index));
                } else if (isPosterior) {
                    setValue(rowData, 4, exponent.get(index));
                } else {
                    setValue(rowData, 4, null);
                }
                setValue(rowData, 5, cer.get(index));
                if (index == 0) {
                    setValue(rowData, 6, cer.get(index));
                } else if (isPosterior) {
                    setValue(rowData, 6, cer.get(index));
                } else {
                    setValue(rowData, 6, null);
                }
            }

            // 设置空数据时，图表生成时跳过
            CTPlotArea plotArea = chart.getCTChart().getPlotArea();
            chart.getCTChart().addNewDispBlanksAs().setVal(STDispBlanksAs.GAP);
            // 建立X轴
            XDDFCategoryAxis categoryAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
            setMajorFont(categoryAxis.getOrAddTextProperties());
            categoryAxis.setCrosses(AxisCrosses.MIN);
            int skipNum = maxRow / 5;
            CTCatAx catAx = plotArea.getCatAxArray(0);
            catAx.addNewTickLblSkip().setVal(skipNum);
            catAx.addNewTickMarkSkip().setVal(skipNum);
            catAx.addNewLblOffset().setVal(100);
            catAx.addNewSpPr().addNewLn().addNewSolidFill().addNewSrgbClr().setVal(RuiNingColorEnum.LIGHT_GRAY.tone());
            setFont(categoryAxis.getOrAddTextProperties(), RuiNingFontEnum.MICROSOFT_BLACK_12_GRAY);

            AxisParamVo leftAxisParam = new AxisParamVo(RuiNingUtil.AXIS_LEFT_OVERAGE_YIELD);
            leftAxisParam.addData(netWorth);
            leftAxisParam.addData(exponent);
            leftAxisParam.addData(cer);

            leftAxisParam.calculate();
            // 建立左轴，折线
            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
            leftAxis.setMinimum(leftAxisParam.toMinimum());
            leftAxis.setMaximum(leftAxisParam.toMaximum());
            leftAxis.setMajorUnit(leftAxisParam.toMajorUnit());
            leftAxis.setMinorUnit(leftAxisParam.toMinorUnit());
            leftAxis.setCrosses(AxisCrosses.MIN);
            leftAxis.setMajorTickMark(AxisTickMark.NONE);
            setFont(leftAxis.getOrAddTextProperties(), RuiNingFontEnum.MICROSOFT_BLACK_12_GRAY);
            CTValAx leftAx = plotArea.getValAxArray(0);
            // 左轴轴线无颜色
            leftAx.addNewSpPr().addNewLn().addNewNoFill();
            // 网格线，浅灰色
            CTChartLines gridLine = leftAx.addNewMajorGridlines();
            gridLine.addNewSpPr().addNewLn().addNewSolidFill().addNewSrgbClr().setVal(RuiNingColorEnum.LIGHT_GRAY.tone());

            XDDFDataSource<String> dateCategory = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, maxRow, 0, 0));
            XDDFNumericalDataSource<Double> netWorthValues = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, maxRow, 1, 1));
            XDDFNumericalDataSource<Double> posteriorNetWorthValues = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, maxRow, 2, 2));
            // 建立折现图的数据集
            XDDFChartData lineData = chart.createData(ChartTypes.LINE, categoryAxis, leftAxis);
            // 左轴，产品净值
            XDDFLineChartData.Series netWorthSeries = (XDDFLineChartData.Series) lineData.addSeries(dateCategory, netWorthValues);
            setLineSeries(netWorthSeries, null);
            setLineSeriesStyle(netWorthSeries, RuiNingColorEnum.ROSE_RED.tone(), CONTAINS_DATA_AFTER_MARCH, 1.75D);
            netWorthSeries.plot();
            if (CONTAINS_DATA_AFTER_MARCH) {
                // 左轴，3月迭代以来的产品净值
                XDDFLineChartData.Series posteriorNetWorthSeries = (XDDFLineChartData.Series) lineData.addSeries(dateCategory, posteriorNetWorthValues);
                setLineSeries(posteriorNetWorthSeries, null);
                setLineSeriesStyle(posteriorNetWorthSeries, RuiNingColorEnum.ROSE_RED.tone(), false, 1.75D);
                posteriorNetWorthSeries.plot();
            }

            XDDFNumericalDataSource<Double> exponentValues = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, maxRow, 3, 3));
            XDDFNumericalDataSource<Double> posteriorExponentValues = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, maxRow, 4, 4));
            // 左轴，指数
            XDDFLineChartData.Series exponentSeries = (XDDFLineChartData.Series) lineData.addSeries(dateCategory, exponentValues);
            setLineSeries(exponentSeries, null);
            setLineSeriesStyle(exponentSeries, RuiNingColorEnum.BLUE.tone(), CONTAINS_DATA_AFTER_MARCH, 1.75D);
            exponentSeries.plot();
            if (CONTAINS_DATA_AFTER_MARCH) {
                // 左轴，3月迭代以来的指数
                XDDFLineChartData.Series posteriorExponentSeries = (XDDFLineChartData.Series) lineData.addSeries(dateCategory, posteriorExponentValues);
                setLineSeries(posteriorExponentSeries, null);
                setLineSeriesStyle(posteriorExponentSeries, RuiNingColorEnum.BLUE.tone(), false, 1.75D);
                posteriorExponentSeries.plot();
            }

            XDDFNumericalDataSource<Double> cerValues = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, maxRow, 5, 5));
            // 右轴，累计超额收益
            XDDFLineChartData.Series cerSeries = (XDDFLineChartData.Series) lineData.addSeries(dateCategory, cerValues);
            setLineSeries(cerSeries, null);
            setLineSeriesStyle(cerSeries, RuiNingColorEnum.BLACK.tone(), true, 1.25D);
            cerSeries.plot();

            // 建立右轴，右折线
//            AxisParamVo rightAxisParam = new AxisParamVo(RuiNingUtil.AXIS_RIGHT_OVERAGE_YIELD);
//            rightAxisParam.addData(cer);
//            rightAxisParam.calculate();
            XDDFValueAxis rightAxis = chart.createValueAxis(AxisPosition.RIGHT);
            rightAxis.crossAxis(categoryAxis);
            categoryAxis.crossAxis(rightAxis);
            rightAxis.setNumberFormat("0%");
//            rightAxis.setMinimum(rightAxisParam.toMinimum());
//            rightAxis.setMaximum(rightAxisParam.toMaximum());
//            rightAxis.setMajorUnit(rightAxisParam.toMajorUnit());
//            rightAxis.setMinorUnit(rightAxisParam.toMinorUnit());
            rightAxis.setMinimum(leftAxisParam.toMinimum() - 1);
            rightAxis.setMaximum(leftAxisParam.toMaximum() - 1);
            rightAxis.setMajorUnit(leftAxisParam.toMajorUnit());
            rightAxis.setMinorUnit(leftAxisParam.toMinorUnit());
            rightAxis.setCrosses(AxisCrosses.MAX);
            setFont(rightAxis.getOrAddTextProperties(), RuiNingFontEnum.MICROSOFT_BLACK_12_GRAY);
            CTValAx rightAx = plotArea.getValAxArray(1);
            rightAx.addNewSpPr().addNewLn().addNewNoFill();
            chart.createData(ChartTypes.LINE, categoryAxis, rightAxis);
            //设置图表的位置和宽高
//            Rectangle rightSite = null;
            int residualValue = 0;
            if (rowCount > 0) {
                residualValue = rowCount * 400000;
            }
            if (Objects.equals("EI500", sheetName)) {
                slide.addChart(chart, RectangleEnum.EI_500_LEFT.toPoorSite(poor, residualValue));
//                rightSite = RectangleEnum.EI_500_RIGHT.toPoorSite(poor, residualValue);
            } else {
                slide.addChart(chart, RectangleEnum.EI_1000_LEFT.toPoorSite(poor, residualValue));
//                rightSite = RectangleEnum.EI_1000_RIGHT.toPoorSite(poor, residualValue);
            }
//            addExponentialIncreaseForRightLine(pptx, slide, sheet, rightAxisParam, maxRow, rightSite);
        } catch (Exception e) {
            log.info("生成乾德" + sheetName + "指增产品曲线异常 >>> " + e.getMessage());
            e.printStackTrace();
        }
    }

//
//    private static void addChartForExponentialIncrease2(XMLSlideShow pptx, XSLFSlide slide, List<ChartDataDto> exponentialIncrease, String sheetName, BigDecimal poor, PowerPointShapeUtil powerPointShapeUtil, Integer rowCount) {
//        try {
//            // 数据
//            if (CollectionUtils.isEmpty(exponentialIncrease)) {
//                if (Objects.equals("EI500", sheetName)) {
//                    slide.removeShape(powerPointShapeUtil.getShapeMap().get(PowerPointShapeEnum.QD500_CHART_CPJZ.code()).getShape());
//                    slide.removeShape(powerPointShapeUtil.getShapeMap().get(PowerPointShapeEnum.QD500_CHART_CESYL.code()).getShape());
//                    slide.removeShape(powerPointShapeUtil.getShapeMap().get(PowerPointShapeEnum.QD500_CHART_DESP.code()).getShape());
//                } else {
//                    slide.removeShape(powerPointShapeUtil.getShapeMap().get(PowerPointShapeEnum.QD1000_CHART_CPJZ.code()).getShape());
//                    slide.removeShape(powerPointShapeUtil.getShapeMap().get(PowerPointShapeEnum.QD1000_CHART_CESYL.code()).getShape());
//                    slide.removeShape(powerPointShapeUtil.getShapeMap().get(PowerPointShapeEnum.QD1000_CHART_DESP.code()).getShape());
//                }
//                return;
//            }
//
//            long deadlineTime = RuiNingUtil.toDeadlineTime();
//            // 创建图表
//            XSLFChart chart = pptx.createChart();
//            // 创建数据表格
//            XSSFSheet sheet = chart.getWorkbook().createSheet(sheetName);
//            List<String> dates = exponentialIncrease.stream().map(ChartDataDto::getDate).collect(Collectors.toList());
//            List<Double> netWorth = exponentialIncrease.stream().map(ChartDataDto::getNw).collect(Collectors.toList());
//            List<Double> exponent = exponentialIncrease.stream().map(ChartDataDto::getInd).collect(Collectors.toList());
//            List<Double> cer = exponentialIncrease.stream().map(ChartDataDto::getCer).collect(Collectors.toList());
//            // 填充表格数据
//            int maxRow = dates.size();
//            for (int row = 1; row <= maxRow; row++) {
//                XSSFRow rowData = sheet.getRow(row);
//                if (rowData == null) {
//                    rowData = sheet.createRow(row);
//                }
//
//                int index = row - 1;
//                Cell dateCell = rowData.getCell(0);
//                if (dateCell == null) {
//                    dateCell = rowData.createCell(0);
//                }
//                Date date = RuiNingUtil.stringToDate(dates.get(index));
//                boolean isPosterior = RuiNingUtil.isPosterior(date, deadlineTime);
//                dateCell.setCellValue(RuiNingUtil.dateToMonth(date));
//                setValue(rowData, 1, netWorth.get(index));
//                if (index == 0) {
//                    setValue(rowData, 2, netWorth.get(index));
//                } else if (isPosterior) {
//                    setValue(rowData, 2, netWorth.get(index));
//                } else {
//                    setValue(rowData, 2, null);
//                }
//                setValue(rowData, 3, exponent.get(index));
//                if (index == 0) {
//                    setValue(rowData, 4, exponent.get(index));
//                } else if (isPosterior) {
//                    setValue(rowData, 4, exponent.get(index));
//                } else {
//                    setValue(rowData, 4, null);
//                }
//                setValue(rowData, 5, cer.get(index));
//                if (index == 0) {
//                    setValue(rowData, 6, cer.get(index));
//                } else if (isPosterior) {
//                    setValue(rowData, 6, cer.get(index));
//                } else {
//                    setValue(rowData, 6, null);
//                }
//            }
//
//            // 设置空数据时，图表生成时跳过
//            CTPlotArea plotArea = chart.getCTChart().getPlotArea();
//            chart.getCTChart().addNewDispBlanksAs().setVal(STDispBlanksAs.GAP);
//            // 建立X轴
//            XDDFCategoryAxis categoryAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
//            setMajorFont(categoryAxis.getOrAddTextProperties());
//            categoryAxis.setCrosses(AxisCrosses.MIN);
//            int skipNum = maxRow / 5;
//            CTCatAx catAx = plotArea.getCatAxArray(0);
//            catAx.addNewTickLblSkip().setVal(skipNum);
//            catAx.addNewTickMarkSkip().setVal(skipNum);
//            catAx.addNewLblOffset().setVal(100);
//            catAx.addNewSpPr().addNewLn().addNewSolidFill().addNewSrgbClr().setVal(RuiNingColorEnum.LIGHT_GRAY.tone());
//            setFont(categoryAxis.getOrAddTextProperties(), RuiNingFontEnum.MICROSOFT_BLACK_12_GRAY);
//
//            AxisParamVo leftAxisParam = new AxisParamVo(RuiNingUtil.AXIS_LEFT_OVERAGE_YIELD);
//            leftAxisParam.addData(netWorth);
//            leftAxisParam.addData(exponent);
//            leftAxisParam.calculate();
//            // 建立左轴，折线
//            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
//            leftAxis.setMinimum(leftAxisParam.toMinimum());
//            leftAxis.setMaximum(leftAxisParam.toMaximum());
//            leftAxis.setMajorUnit(leftAxisParam.toMajorUnit());
//            leftAxis.setMinorUnit(leftAxisParam.toMinorUnit());
//            leftAxis.setCrosses(AxisCrosses.MIN);
//            leftAxis.setMajorTickMark(AxisTickMark.NONE);
//            setFont(leftAxis.getOrAddTextProperties(), RuiNingFontEnum.MICROSOFT_BLACK_12_GRAY);
//            CTValAx leftAx = plotArea.getValAxArray(0);
//            // 左轴轴线无颜色
//            leftAx.addNewSpPr().addNewLn().addNewNoFill();
//            // 网格线，浅灰色
//            CTChartLines gridLine = leftAx.addNewMajorGridlines();
//            gridLine.addNewSpPr().addNewLn().addNewSolidFill().addNewSrgbClr().setVal(RuiNingColorEnum.LIGHT_GRAY.tone());
//
//            XDDFDataSource<String> dateCategory = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, maxRow, 0, 0));
//            XDDFNumericalDataSource<Double> netWorthValues = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, maxRow, 1, 1));
//            XDDFNumericalDataSource<Double> posteriorNetWorthValues = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, maxRow, 2, 2));
//            // 建立折现图的数据集
//            XDDFChartData lineData = chart.createData(ChartTypes.LINE, categoryAxis, leftAxis);
//            // 左轴，产品净值
//            XDDFLineChartData.Series netWorthSeries = (XDDFLineChartData.Series) lineData.addSeries(dateCategory, netWorthValues);
//            setLineSeries(netWorthSeries, null);
//            setLineSeriesStyle(netWorthSeries, RuiNingColorEnum.ROSE_RED.tone(), CONTAINS_DATA_AFTER_MARCH, 1.25D);
//            netWorthSeries.plot();
//            if (CONTAINS_DATA_AFTER_MARCH) {
//                // 左轴，3月迭代以来的产品净值
//                XDDFLineChartData.Series posteriorNetWorthSeries = (XDDFLineChartData.Series) lineData.addSeries(dateCategory, posteriorNetWorthValues);
//                setLineSeries(posteriorNetWorthSeries, null);
//                setLineSeriesStyle(posteriorNetWorthSeries, RuiNingColorEnum.ROSE_RED.tone(), false, 1.75D);
//                posteriorNetWorthSeries.plot();
//            }
//
//            XDDFNumericalDataSource<Double> exponentValues = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, maxRow, 3, 3));
//            XDDFNumericalDataSource<Double> posteriorExponentValues = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, maxRow, 4, 4));
//            // 左轴，指数
//            XDDFLineChartData.Series exponentSeries = (XDDFLineChartData.Series) lineData.addSeries(dateCategory, exponentValues);
//            setLineSeries(exponentSeries, null);
//            setLineSeriesStyle(exponentSeries, RuiNingColorEnum.BLUE.tone(), CONTAINS_DATA_AFTER_MARCH, 1.25D);
//            exponentSeries.plot();
//            if (CONTAINS_DATA_AFTER_MARCH) {
//                // 左轴，3月迭代以来的指数
//                XDDFLineChartData.Series posteriorExponentSeries = (XDDFLineChartData.Series) lineData.addSeries(dateCategory, posteriorExponentValues);
//                setLineSeries(posteriorExponentSeries, null);
//                setLineSeriesStyle(posteriorExponentSeries, RuiNingColorEnum.BLUE.tone(), false, 1.75D);
//                posteriorExponentSeries.plot();
//            }
//
//            // 建立右轴，右折线
//            AxisParamVo rightAxisParam = new AxisParamVo(RuiNingUtil.AXIS_RIGHT_OVERAGE_YIELD);
//            rightAxisParam.addData(cer);
//            rightAxisParam.calculate();
//            XDDFValueAxis rightAxis = chart.createValueAxis(AxisPosition.RIGHT);
//            rightAxis.crossAxis(categoryAxis);
//            categoryAxis.crossAxis(rightAxis);
//            rightAxis.setNumberFormat("0%");
//            rightAxis.setMinimum(rightAxisParam.toMinimum());
//            rightAxis.setMaximum(rightAxisParam.toMaximum());
//            rightAxis.setMajorUnit(rightAxisParam.toMajorUnit());
//            rightAxis.setMinorUnit(rightAxisParam.toMinorUnit());
//            rightAxis.setCrosses(AxisCrosses.MAX);
//            setFont(rightAxis.getOrAddTextProperties(), RuiNingFontEnum.MICROSOFT_BLACK_12_GRAY);
//            CTValAx rightAx = plotArea.getValAxArray(1);
//            rightAx.addNewSpPr().addNewLn().addNewNoFill();
//            chart.createData(ChartTypes.LINE, categoryAxis, rightAxis);
//            //设置图表的位置和宽高
//            Rectangle rightSite = null;
//            int residualValue = 0;
//            if (rowCount > 0) {
//                residualValue = rowCount * 400000;
//            }
//            if (Objects.equals("EI500", sheetName)) {
//                slide.addChart(chart, RectangleEnum.EI_500_LEFT.toPoorSite(poor, residualValue));
//                rightSite = RectangleEnum.EI_500_RIGHT.toPoorSite(poor, residualValue);
//            } else {
//                slide.addChart(chart, RectangleEnum.EI_1000_LEFT.toPoorSite(poor, residualValue));
//                rightSite = RectangleEnum.EI_1000_RIGHT.toPoorSite(poor, residualValue);
//            }
//            addExponentialIncreaseForRightLine(pptx, slide, sheet, rightAxisParam, maxRow, rightSite);
//        } catch (Exception e) {
//            log.info("生成乾德" + sheetName + "指增产品曲线异常 >>> " + e.getMessage());
//            e.printStackTrace();
//        }
//    }

    private static void addExponentialIncreaseForRightLine(XMLSlideShow pptx, XSLFSlide slide, XSSFSheet sheet, AxisParamVo rightAxisParam, int maxRow, Rectangle2D rightSite) {
        XSLFChart chart = pptx.createChart();
        chart.getCTChart().addNewDispBlanksAs().setVal(STDispBlanksAs.GAP);

        XDDFCategoryAxis categoryAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        XDDFDataSource<String> dateCategory = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, maxRow, 0, 0));

        XDDFValueAxis rightAxis = chart.createValueAxis(AxisPosition.LEFT);
        rightAxis.setMinimum(rightAxisParam.toMinimum());
        rightAxis.setMaximum(rightAxisParam.toMaximum());
        rightAxis.setMajorUnit(rightAxisParam.toMajorUnit());
        rightAxis.setMinorUnit(rightAxisParam.toMinorUnit());
        rightAxis.setCrosses(AxisCrosses.MIN);
        categoryAxis.setVisible(false);
        rightAxis.setVisible(false);

        // 建立面积图的数据集
        XDDFLineChartData lineData = (XDDFLineChartData) chart.createData(ChartTypes.LINE, categoryAxis, rightAxis);
        XDDFNumericalDataSource<Double> cerValues = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, maxRow, 5, 5));
        XDDFNumericalDataSource<Double> posteriorCerValues = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, maxRow, 6, 6));
        // 右轴，累计超额收益
        XDDFLineChartData.Series cerSeries = (XDDFLineChartData.Series) lineData.addSeries(dateCategory, cerValues);
        setLineSeries(cerSeries, null);
        setLineSeriesStyle(cerSeries, RuiNingColorEnum.GRAY.tone(), CONTAINS_DATA_AFTER_MARCH, 1.25D);
        cerSeries.plot();
        if (CONTAINS_DATA_AFTER_MARCH) {
            // 左轴，3月迭代以来的累计超额收益
            XDDFLineChartData.Series posteriorCerSeries = (XDDFLineChartData.Series) lineData.addSeries(dateCategory, posteriorCerValues);
            setLineSeries(posteriorCerSeries, null);
            setLineSeriesStyle(posteriorCerSeries, RuiNingColorEnum.BLACK.tone(), false, 1.75D);
            posteriorCerSeries.plot();
        }
        // 绘制右轴面积
        chart.plot(lineData);
        slide.addChart(chart, rightSite);
    }

    private static void addChartForNeutral(XMLSlideShow pptx, XSLFSlide slide, List<ChartDataDto> neutralData, BigDecimal poor, PowerPointShapeUtil powerPointShapeUtil) {
        try {
            // 数据
            if (CollectionUtils.isEmpty(neutralData)) {
                slide.removeShape(powerPointShapeUtil.getShapeMap().get(PowerPointShapeEnum.JL_CHART_CPJZ.code()).getShape());
                slide.removeShape(powerPointShapeUtil.getShapeMap().get(PowerPointShapeEnum.JL_CHART_ZDSYL.code()).getShape());
                slide.removeShape(powerPointShapeUtil.getShapeMap().get(PowerPointShapeEnum.JL_CHART_DESP.code()).getShape());
                return;
            }
            long deadlineTime = RuiNingUtil.toDeadlineTime();
            // 创建图表
            XSLFChart chart = pptx.createChart();
            // 创建数据表格
            XSSFSheet sheet = chart.getWorkbook().createSheet("neutral");
            List<String> dates = neutralData.stream().map(ChartDataDto::getDate).collect(Collectors.toList());
            List<Double> netWorth = neutralData.stream().map(ChartDataDto::getNw).collect(Collectors.toList());
            List<Double> lr = neutralData.stream().map(ChartDataDto::getLr).collect(Collectors.toList());
            // 填充表格数据
            int maxRow = dates.size();
            for (int row = 1; row <= maxRow; row++) {
                XSSFRow rowData = sheet.getRow(row);
                if (rowData == null) {
                    rowData = sheet.createRow(row);
                }
                int index = row - 1;
                Cell dateCell = rowData.getCell(0);
                if (dateCell == null) {
                    dateCell = rowData.createCell(0);
                }

                Date date = RuiNingUtil.stringToDate(dates.get(index));
                boolean isPosterior = RuiNingUtil.isPosterior(date, deadlineTime);
                dateCell.setCellValue(RuiNingUtil.dateToMonth(date));
                setValue(rowData, 1, netWorth.get(index));
                if (index == 0) {
                    setValue(rowData, 2, netWorth.get(index));
                } else if (isPosterior) {
                    setValue(rowData, 2, netWorth.get(index));
                } else {
                    setValue(rowData, 2, null);
                }
                setValue(rowData, 3, lr.get(index));
                if (index == 0) {
                    setValue(rowData, 4, lr.get(index));
                } else if (isPosterior) {
                    setValue(rowData, 4, lr.get(index));
                } else {
                    setValue(rowData, 4, null);
                }
            }

            // 设置空数据时，图表生成时跳过
            CTPlotArea plotArea = chart.getCTChart().getPlotArea();
            chart.getCTChart().addNewDispBlanksAs().setVal(STDispBlanksAs.GAP);

            AxisParamVo rightAxisParam = new AxisParamVo(RuiNingUtil.AXIS_RIGHT_NEUTRAL);
            rightAxisParam.addData(lr);
            rightAxisParam.calculate();
            // 创建面积图
            addNeutralForArea(pptx, slide, sheet, rightAxisParam, maxRow, poor);
            // 创建折线图
            addNeutralForLine(chart, slide, sheet, plotArea, rightAxisParam, maxRow, netWorth, poor);
        } catch (Exception e) {
            log.info("生成建隆量化中性产品曲线异常 >>> " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void addNeutralForArea(XMLSlideShow pptx, XSLFSlide slide, XSSFSheet sheet, AxisParamVo rightAxisParam, int maxRow, BigDecimal poor) {
        XSLFChart chart = pptx.createChart();
        CTPlotArea plotArea = chart.getCTChart().getPlotArea();
        chart.getCTChart().addNewDispBlanksAs().setVal(STDispBlanksAs.GAP);

        XDDFCategoryAxis categoryAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        XDDFDataSource<String> dateCategory = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, maxRow, 0, 0));
        // 建立左轴，折线
        XDDFValueAxis rightAxis = chart.createValueAxis(AxisPosition.LEFT);
        rightAxis.setMinimum(rightAxisParam.toMinimum());
        rightAxis.setMaximum(rightAxisParam.toMaximum());
        rightAxis.setMajorUnit(rightAxisParam.toMajorUnit());
        rightAxis.setMinorUnit(rightAxisParam.toMinorUnit());
        rightAxis.setCrosses(AxisCrosses.MIN);
        categoryAxis.setVisible(false);
        rightAxis.setVisible(false);

        // 建立面积图的数据集
        XDDFAreaChartData areaData = (XDDFAreaChartData) chart.createData(ChartTypes.AREA, categoryAxis, rightAxis);
        XDDFNumericalDataSource<Double> lrValues = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, maxRow, 3, 3));
        XDDFNumericalDataSource<Double> posteriorLrValues = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, maxRow, 4, 4));
        // 右轴，周度收益率
        if (CONTAINS_DATA_AFTER_MARCH) {
            XDDFAreaChartData.Series lrSeries = (XDDFAreaChartData.Series) areaData.addSeries(dateCategory, lrValues);
            lrSeries.setTitle("", null);
            XDDFAreaChartData.Series posteriorLrSeries = (XDDFAreaChartData.Series) areaData.addSeries(dateCategory, posteriorLrValues);
            posteriorLrSeries.setTitle("", null);
            CTAreaChart areaSeries = plotArea.getAreaChartArray(0);
            setAreaSeriesColor(areaSeries, Arrays.asList(
                    RuiNingColorEnum.LIGHT_GRAY.tone(),
                    RuiNingColorEnum.BLUE.tone()
            ));
        } else {
            XDDFAreaChartData.Series lrSeries = (XDDFAreaChartData.Series) areaData.addSeries(dateCategory, lrValues);
            lrSeries.setTitle("", null);
            CTAreaChart areaSeries = plotArea.getAreaChartArray(0);
            setAreaSeriesColor(areaSeries, Arrays.asList(
                    RuiNingColorEnum.BLUE.tone()
            ));
        }
        // 绘制右轴面积
        chart.plot(areaData);
        slide.addChart(chart, RectangleEnum.NEUTRAL_RIGHT.toPoorSite(poor, 0));
    }

    private static void addNeutralForLine(XSLFChart chart, XSLFSlide slide, XSSFSheet sheet, CTPlotArea plotArea, AxisParamVo rightAxisParam, int maxRow, List<Double> netWorth, BigDecimal poor) {
        // 建立X轴
        XDDFCategoryAxis categoryAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        setMajorFont(categoryAxis.getOrAddTextProperties());
        categoryAxis.setCrosses(AxisCrosses.MIN);
        int skipNum = maxRow / 5;
        CTCatAx catAx = plotArea.getCatAxArray(0);
        catAx.addNewTickLblSkip().setVal(skipNum);
        catAx.addNewTickMarkSkip().setVal(skipNum);
        catAx.addNewLblOffset().setVal(100);
        catAx.addNewSpPr().addNewLn().addNewSolidFill().addNewSrgbClr().setVal(RuiNingColorEnum.LIGHT_GRAY.tone());
        setFont(categoryAxis.getOrAddTextProperties(), RuiNingFontEnum.MICROSOFT_BLACK_12_GRAY);

        AxisParamVo leftAxisParam = new AxisParamVo(RuiNingUtil.AXIS_LEFT_NEUTRAL);
        leftAxisParam.addData(netWorth);
        leftAxisParam.calculate();
        // 建立左轴，折线
        XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
        leftAxis.setMinimum(leftAxisParam.toMinimum());
        leftAxis.setMaximum(leftAxisParam.toMaximum());
        leftAxis.setMajorUnit(leftAxisParam.toMajorUnit());
        leftAxis.setMinorUnit(leftAxisParam.toMinorUnit());
        leftAxis.setCrosses(AxisCrosses.MIN);
        leftAxis.setMajorTickMark(AxisTickMark.NONE);
        setFont(leftAxis.getOrAddTextProperties(), RuiNingFontEnum.MICROSOFT_BLACK_12_GRAY);
        CTValAx leftAx = plotArea.getValAxArray(0);
        // 左轴轴线无颜色
        leftAx.addNewSpPr().addNewLn().addNewNoFill();
        // 网格线，浅灰色
        CTChartLines gridLine = leftAx.addNewMajorGridlines();
        gridLine.addNewSpPr().addNewLn().addNewSolidFill().addNewSrgbClr().setVal(RuiNingColorEnum.LIGHT_GRAY.tone());

        XDDFDataSource<String> dateCategory = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, maxRow, 0, 0));
        XDDFNumericalDataSource<Double> netWorthValues = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, maxRow, 1, 1));
        XDDFNumericalDataSource<Double> posteriorNetWorthValues = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, maxRow, 2, 2));

        // 建立折现图的数据集
        XDDFChartData lineData = chart.createData(ChartTypes.LINE, categoryAxis, leftAxis);
        // 左轴，产品净值
        XDDFLineChartData.Series netWorthSeries = (XDDFLineChartData.Series) lineData.addSeries(dateCategory, netWorthValues);
        setLineSeries(netWorthSeries, null);
        setLineSeriesStyle(netWorthSeries, new byte[]{(byte) 192, (byte) 0, (byte) 0}, CONTAINS_DATA_AFTER_MARCH, 1.5D);
        netWorthSeries.plot();

        if (CONTAINS_DATA_AFTER_MARCH) {
            // 左轴，3月迭代以来的产品净值
            XDDFLineChartData.Series posteriorNetWorthSeries = (XDDFLineChartData.Series) lineData.addSeries(dateCategory, posteriorNetWorthValues);
            setLineSeries(posteriorNetWorthSeries, null);
            setLineSeriesStyle(posteriorNetWorthSeries, new byte[]{(byte) 192, (byte) 0, (byte) 0}, false, 1.75D);
            posteriorNetWorthSeries.plot();
        }

        // 建立右轴，面积
        XDDFValueAxis rightAxis = chart.createValueAxis(AxisPosition.RIGHT);
        rightAxis.crossAxis(categoryAxis);
        categoryAxis.crossAxis(rightAxis);
        rightAxis.setNumberFormat("0%");
        rightAxis.setMinimum(rightAxisParam.toMinimum());
        rightAxis.setMaximum(rightAxisParam.toMaximum());
        rightAxis.setMajorUnit(rightAxisParam.toMajorUnit());
        rightAxis.setMinorUnit(rightAxisParam.toMinorUnit());
        rightAxis.setCrosses(AxisCrosses.MAX);
        setFont(rightAxis.getOrAddTextProperties(), RuiNingFontEnum.MICROSOFT_BLACK_12_GRAY);
        CTValAx rightAx = plotArea.getValAxArray(1);
        rightAx.addNewSpPr().addNewLn().addNewNoFill();

        chart.createData(ChartTypes.AREA, categoryAxis, rightAxis);
        //设置图表的位置和宽高
        slide.addChart(chart, RectangleEnum.NEUTRAL_LEFT.toPoorSite(poor, 0));
    }

    private static void setAreaSeriesColor(CTAreaChart areaSeries, List<byte[]> colors) {
        for (int i = 0; i < colors.size(); i++) {
            CTAreaSer lrAreaSer = areaSeries.getSerArray(i);
            CTShapeProperties lrAreaShape = lrAreaSer.addNewSpPr();
            CTSolidColorFillProperties lrAreaFill = lrAreaShape.addNewSolidFill();
            CTSRgbColor lrRgb = lrAreaFill.addNewSrgbClr();
            lrRgb.setVal(colors.get(i));
            // 透明度 0- 100000
            lrRgb.addNewAlpha().setVal(100000);
        }
    }

    private static void setMajorFont(XDDFRunProperties theme) {
        theme.setFontSize(12D);
        theme.setFillProperties(new XDDFSolidFillProperties(XDDFColor.from(new byte[]{(byte) 64, (byte) 64, (byte) 64})));
        theme.setFonts(new XDDFFont[]{new XDDFFont(FontGroup.EAST_ASIAN, "微软雅黑", null, null, null)});
    }

    private static void setValue(XSSFRow rowData, int cellIndex, Double value) {
        Cell cell = rowData.getCell(cellIndex);
        if (cell == null) {
            cell = rowData.createCell(cellIndex);
        }
        if (value == null) {
            cell.setCellType(CellType.BLANK);
            cell.setBlank();
        } else {
            cell.setCellValue(value);
        }
    }

    private static CTPlotArea getChartPlotArea(XSLFChart chart) {
        return chart.getCTChart().getPlotArea();
    }

    public static void addRows(XSLFTable table, List<ExponentVo> vos, boolean isMergeCell) {
        int rowCount = 1;
        Color tint = new Color(238, 228, 210);
        Color dark = new Color(220, 200, 164);
        for (ExponentVo vo : vos) {
            if (rowCount % 2 == 0) {
                addRow(table, vo, tint, isMergeCell);
            } else {
                addRow(table, vo, dark, isMergeCell);
            }
            rowCount++;
        }
        if (isMergeCell) {
            table.getRows().get(0).mergeCells(1, 2);
            table.getRows().get(1).mergeCells(1, 2);
        }
    }

    public static void addRow(XSLFTable table, ExponentVo vo, Color fillColor, boolean isMergeCell) {
        XSLFTableRow row = table.addRow();
        row.setHeight(RuiNingUtil.DEFAULT_GRID_ROW_HEIGHT);
        addCell(row, vo.getName(), fillColor, 14.5D, true);
        addCell(row, vo.getUp(), fillColor, 12.8D, false);
        addCell(row, vo.getDown(), fillColor, 12.8D, false);
        if (isMergeCell) {
            row.mergeCells(1, 2);
        }
    }

    public static void addCell(XSLFTableRow row, String value, Color fillColor, double fontSize, boolean isBold) {
        XSLFTableCell cell = row.addCell();
        cell.clearText();
        cell.setFillColor(fillColor);
        setBorder(cell, 1.0, Color.WHITE);
        cell.setHorizontalCentered(true);
        XSLFTextParagraph paragraph = cell.addNewTextParagraph();
        paragraph.setLineSpacing(120D);
        XSLFTextRun run = paragraph.addNewTextRun();
        run.setText(value);
        run.setFontFamily("微软雅黑");
        run.setFontSize(fontSize);
        run.setBold(isBold);
        run.setFontColor(Color.DARK_GRAY);
    }

    private static void setBorder(XSLFTableCell cell, double width, Color color) {
        cell.setBorderColor(TableCell.BorderEdge.top, color);
        cell.setBorderWidth(TableCell.BorderEdge.top, width);
        cell.setBorderColor(TableCell.BorderEdge.left, color);
        cell.setBorderWidth(TableCell.BorderEdge.left, width);
        cell.setBorderColor(TableCell.BorderEdge.right, color);
        cell.setBorderWidth(TableCell.BorderEdge.right, width);
        cell.setBorderColor(TableCell.BorderEdge.bottom, color);
        cell.setBorderWidth(TableCell.BorderEdge.bottom, width);
    }




    public static String convert(String fileName, String pptxFilePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(pptxFilePath);
        try(XMLSlideShow oneSlideShow =  new XMLSlideShow(fileInputStream)) {
            String xmlFontFormat = "<xml-fragment xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\" " +
                    "xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\">" +
                    "<a:rPr lang=\"zh-CN\" altLang=\"en-US\" dirty=\"0\" smtClean=\"0\"> " +
                    "<a:latin typeface=\"+mj-ea\"/> " +
                    "</a:rPr>" +
                    "</xml-fragment>";
            Dimension onePPTPageSize = oneSlideShow.getPageSize();
            List<XSLFSlide> pptPageXSLFSLiseList = oneSlideShow.getSlides();

            //设置字体，解决中文乱码问题
            CTGroupShape oneCTGroupShape = pptPageXSLFSLiseList.get(0).getXmlObject().getCSld().getSpTree();
            for (CTShape ctShape : oneCTGroupShape.getSpList()) {
                CTTextBody oneCTTextBody = ctShape.getTxBody();
                if (null == oneCTTextBody) {
                    continue;
                }
                CTTextParagraph[] oneCTTextParagraph = oneCTTextBody.getPArray();
                CTTextFont oneCTTextFont = null;
                try {
                    oneCTTextFont = CTTextFont.Factory.parse(xmlFontFormat);
                } catch (XmlException e) {

                }
                if (oneCTTextFont == null) {
                    continue;
                }
                for (CTTextParagraph ctTextParagraph : oneCTTextParagraph) {
                    CTRegularTextRun[] onrCTRegularTextRunArray = ctTextParagraph.getRArray();
                    for (CTRegularTextRun ctRegularTextRun : onrCTRegularTextRunArray) {
                        CTTextCharacterProperties oneCTTextCharacterProperties = ctRegularTextRun.getRPr();
                        oneCTTextCharacterProperties.setLatin(oneCTTextFont);
                    }
                }
            }
            for(XSLFShape shape : pptPageXSLFSLiseList.get(0).getShapes() ){
                if (shape instanceof XSLFTextShape){
                    XSLFTextShape txtshape = (XSLFTextShape)shape ;
                    for ( XSLFTextParagraph textPara : txtshape.getTextParagraphs() ){
                        List<XSLFTextRun> textRunList = textPara.getTextRuns();
                        for(XSLFTextRun textRun: textRunList) {
                            textRun.setFontFamily("simsun");
                        }
                    }
                }
            }
            BufferedImage oneBufferedImage = new BufferedImage(onePPTPageSize.width, onePPTPageSize.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D oneGraphics2D = oneBufferedImage.createGraphics();
            pptPageXSLFSLiseList.get(0).draw(oneGraphics2D);
            try(OutputStream imageOut = new FileOutputStream(fileName)) {
                ImageIO.write(oneBufferedImage, "png", imageOut);
            } finally {
            }
            return fileName;
        }
    }
}
