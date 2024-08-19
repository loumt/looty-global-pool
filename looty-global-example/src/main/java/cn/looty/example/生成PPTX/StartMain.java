package cn.looty.example.生成PPTX;

import cn.looty.example.生成PPTX.model.ChartDataDto;
import cn.looty.example.生成PPTX.model.DmaExponentDto;
import cn.looty.example.生成PPTX.model.WeeklyReportDto;
import cn.looty.example.生成PPTX.model.WeeklyStrategyDto;
import cn.looty.example.生成PPTX.utils.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.poi.common.usermodel.fonts.FontGroup;
import org.apache.poi.sl.usermodel.TableCell;
import org.apache.poi.sl.usermodel.TextParagraph;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.*;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xddf.usermodel.text.XDDFFont;
import org.apache.poi.xddf.usermodel.text.XDDFRunProperties;
import org.apache.poi.xslf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openxmlformats.schemas.drawingml.x2006.chart.*;
import org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor;
import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties;
import org.springframework.util.CollectionUtils;

import java.awt.*;
import java.io.*;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Classname StartMain
 * @Created by loumt
 * @email looty_loumt@hotmail.com
 * @Description TODO
 * @Date 2024/8/14 23:38
 */
public class StartMain {


    private static final String DEFAULT_PPTX_TEMPLATE_FILE_PATH = "D:/ruining_report.pptx";
    private static final String DEFAULT_ATTACHMENT_LOCAL_PATH = "D:/ruining_chart";
    private static final boolean CONTAINS_DATA_AFTER_MARCH = false;
    public static void main(String[] args) throws URISyntaxException {
//        toWeeklyReportForPowerPoint("test.PPTX");
        String currentDir = System.getProperty("user.dir");

        // 打印当前工作目录
        System.out.println("Current Working Directory: " + currentDir);

        //查询模板文件
        List<String> tempaltes = FileUtils.findByName(currentDir, "ruining_report.pptx");
        for (String s : tempaltes) {
            System.out.println(s);
        }


    }


    public static void toWeeklyReportForPowerPoint(String fileName) {
        WeeklyReportDto body;
        Gson gson = new GsonBuilder().create();

        StringBuffer sb = new StringBuffer();
        try (BufferedReader br = new BufferedReader(new FileReader("D:\\test.json"))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

//        System.out.println(sb.toString());
            body = gson.fromJson(sb.toString(), WeeklyReportDto.class);

            generalPPTX(fileName, body);

            //TO PNG
            convertPPTtoPNG(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void generalPPTX(String fileName, WeeklyReportDto body) {
        OutputStream outPutStream = null;
        XMLSlideShow pptx = null;
        try {
            RuiningVo ruiningVo = getRuiningVo(body);
            FileInputStream fileInput = new FileInputStream(DEFAULT_PPTX_TEMPLATE_FILE_PATH);
            pptx = new XMLSlideShow(fileInput);
            pptCommentHandle(pptx, body, ruiningVo);
            outPutStream = new FileOutputStream(DEFAULT_ATTACHMENT_LOCAL_PATH + File.separator + fileName);
            pptx.write(outPutStream);
            outPutStream.flush();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (pptx != null) {
                    pptx.close();
                }
                if (outPutStream != null) {
                    outPutStream.close();
                }
            } catch (IOException e) {
                System.out.println("WEEKLY REPORT CLOSE STREAM ERROR >>> " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void convertPPTtoPNG(String fileName){
//        String cmd = "D:/LibreOffice/program/soffice --headless --convert-to pdf:writer_pdf_Export D:/ruining_chart/"+ fileName +" --outdir D:/ruining_chart/temp";
        String cmd = "D:/LibreOffice/program/soffice --headless --convert-to pdf:writer_pdf_Export:TextAutoSpace=false D:/ruining_chart/"+ fileName +" --outdir D:/ruining_chart/temp";
//        String cmd = "D:/LibreOffice/program/soffice --headless --convert-to pdf --filter=writer_pdf_Export --options='--export-filter-name=writer_pdf_Export;-calc-opts=' D:/ruining_chart/"+ fileName +" --outdir D:/ruining_chart/temp --calc-opts='--calc-opts=NoBreakSpaces'";
        try {
            long start = System.currentTimeMillis();
            String result = execCmd(cmd, null);
            long end = System.currentTimeMillis();
            System.out.println(result + " use time => " +  (end - start));
        } catch (Exception e) {
            e.printStackTrace();
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
                        if (!CONTAINS_DATA_AFTER_MARCH &&
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
            System.out.println("WEEKLY REPORT GENERATE ERROR >>> " + e.getMessage());
            e.printStackTrace();
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
//            categoryAxis.setCrosses(AxisCrosses.MIN);
            categoryAxis.setCrosses(AxisCrosses.AUTO_ZERO);
//            categoryAxis.setCrosses(AxisCrosses.MAX);
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
            System.out.println("生成本周子策略及收益雷达图异常 >>> " + e.getMessage());
            e.printStackTrace();
        }
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
            System.out.println("生成量化中性DMA产品曲线异常 >>> " + e.getMessage());
            e.printStackTrace();
        }
    }

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
            System.out.println("生成乾德" + sheetName + "指增产品曲线异常 >>> " + e.getMessage());
            e.printStackTrace();
        }
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
            System.out.println("生成建隆量化中性产品曲线异常 >>> " + e.getMessage());
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

    /**
     * 执行系统命令, 返回执行结果
     *
     * @param cmd 需要执行的命令
     * @param dir 执行命令的子进程的工作目录, null 表示和当前主进程工作目录相同
     */
    public static String execCmd(String cmd, File dir) throws Exception {
        StringBuilder result = new StringBuilder();

        Process process = null;
        BufferedReader bufrIn = null;
        BufferedReader bufrError = null;

        try {
            // 执行命令, 返回一个子进程对象（命令在子进程中执行）
            process = Runtime.getRuntime().exec(cmd, null, dir);

            // 方法阻塞, 等待命令执行完成（成功会返回0）
            process.waitFor();

            // 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
            bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));

            // 读取输出
            String line = null;
            while ((line = bufrIn.readLine()) != null) {
                result.append(line).append('\n');
            }
            while ((line = bufrError.readLine()) != null) {
                result.append(line).append('\n');
            }

        } finally {
            closeStream(bufrIn);
            closeStream(bufrError);

            // 销毁子进程
            if (process != null) {
                process.destroy();
            }
        }

        // 返回执行结果
        return result.toString();
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                // nothing
            }
        }
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
        vo.paramLoading(true, rangeDateForCe5, rangeDateForCe10, rangeDateForDma);

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
}
