package cn.looty.common.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Filename: ExportExcelUtil
 * @Description: Excel导出工具
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: looty_loumt@hotmail.com
 * @Date: 2024-08-20 15:55
 */
public class ExportExcelUtil {
    private static final Logger logger = LoggerFactory.getLogger(ExportExcelUtil.class);

    /**
     * 导出模版内容
     * @param exportType
     * @param response
     */
    public static void exportTemplate(ExportTypeEnum exportType, HttpServletResponse response) {
        export(exportType, toTitles(exportType), null, response);
    }

    /**
     * 导出数据
     * @param exportType
     * @param response
     */
    public static void export(ExportTypeEnum exportType, List records, HttpServletResponse response) {
        List<ExportColumnEnum> titles = toTitles(exportType);
        export(exportType, titles, toData(titles, records), response);
    }

    private static List<ExportColumnEnum> toTitles(ExportTypeEnum exportType) {
        return Arrays.stream(ExportColumnEnum.values())
                .filter(etc -> Objects.equals(etc.getExportClass(), exportType)).sorted(
                        Comparator.comparing(ExportColumnEnum::getColumnIndex)
                ).collect(Collectors.toList());
    }


    private static List<List<String>> toData(List<ExportColumnEnum> exportColumns, List<Object> records) {
        if (CollectionUtils.isEmpty(records)) {
            return null;
        }

        List<List<String>> data = new ArrayList<>();
        records.forEach(record -> {
            data.add(toRowData(exportColumns, record));
        });
        return data;
    }


    private static void export(ExportTypeEnum exportType, List<ExportColumnEnum> titles, List<List<String>> data, HttpServletResponse response) {
        response.reset();
        response.setContentType("application/vnd.ms-excel");
        try {
            String fileName = exportType.toFileName() + ".xls";
            String fileNameURL = URLEncoder.encode(fileName, "utf-8");

            response.setHeader("Content-disposition", "attachment; filename=" + fileNameURL + ";filename*=utf-8''" + fileNameURL);

            //创建一个WorkBook,对应一个Excel文件
            HSSFWorkbook wb = new HSSFWorkbook();
            //在Workbook中，创建一个sheet，对应Excel中的工作薄（sheet）
            HSSFSheet sheet = wb.createSheet(exportType.getSheetName());
            HSSFFont boldFont = wb.createFont();
            boldFont.setBold(true);

            HSSFCellStyle leftTitle = wb.createCellStyle();
            leftTitle.setAlignment(HorizontalAlignment.LEFT);
            leftTitle.setVerticalAlignment(VerticalAlignment.CENTER);
            leftTitle.setFont(boldFont);

            HSSFCellStyle rightTitle = wb.createCellStyle();
            rightTitle.setAlignment(HorizontalAlignment.RIGHT);
            rightTitle.setVerticalAlignment(VerticalAlignment.CENTER);
            rightTitle.setFont(boldFont);

            HSSFCellStyle centerTitle = wb.createCellStyle();
            centerTitle.setAlignment(HorizontalAlignment.CENTER);
            centerTitle.setVerticalAlignment(VerticalAlignment.CENTER);
            centerTitle.setFont(boldFont);

            HSSFCellStyle leftStyle = wb.createCellStyle();
            leftStyle.setAlignment(HorizontalAlignment.LEFT);
            leftStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            HSSFCellStyle rightStyle = wb.createCellStyle();
            rightStyle.setAlignment(HorizontalAlignment.RIGHT);
            rightStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            HSSFCellStyle centerStyle = wb.createCellStyle();
            centerStyle.setAlignment(HorizontalAlignment.CENTER);
            centerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            int beginRowIndex = 0;
            //在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
            HSSFRow row = sheet.createRow(beginRowIndex);
            row.setHeight((short) 400);
            // 填充表头
            Map<Integer, HSSFCellStyle> styleMap = new HashMap<>();
            for (int i = 0; i < titles.size(); i++) {
                ExportColumnEnum title = titles.get(i);
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(title.getColumnName());

                if (Objects.equals(title.toAlignment(), HorizontalAlignment.LEFT)) {
                    cell.setCellStyle(leftTitle);
                    styleMap.put(i, leftStyle);
                } else if (Objects.equals(title.toAlignment(), HorizontalAlignment.RIGHT)) {
                    cell.setCellStyle(rightTitle);
                    styleMap.put(i, rightStyle);
                } else {
                    cell.setCellStyle(centerTitle);
                    styleMap.put(i, centerStyle);
                }
                sheet.setColumnWidth(i, title.toWidth());
            }

            // 填充内容
            if (!CollectionUtils.isEmpty(data)) {
                for (int index = 0; index < data.size(); index++) {
                    beginRowIndex++;
                    row = sheet.createRow(beginRowIndex);
                    row.setHeight((short) 400);
                    // 获取单个对象
                    List<String> item = data.get(index);
                    for (int i = 0; i < item.size(); i++) {
                        HSSFCell cell = row.createCell(i);
                        cell.setCellValue(item.get(i));
                        cell.setCellStyle(styleMap.get(i));
                    }
                }
            }

            //将文件输出
            OutputStream outPutStream = response.getOutputStream();
            wb.write(outPutStream);
            outPutStream.flush();
            outPutStream.close();
        } catch (Exception e) {
            logger.info("导出Excel失败！");
            logger.info(e.getMessage());
        }
    }


    private static List<String> toRowData(List<ExportColumnEnum> columns, Object record) {
        List<String> data = new ArrayList<>();
        columns.forEach(column -> {
            data.add(getValue(column, record));
        });
        return data;
    }

    private static String toColumnMethodName(String column) {
        String first = column.substring(0, 1);
        return "get" + first.toUpperCase() + column.substring(1);
    }

    private static String getValue(ExportColumnEnum column, Object reportData) {
        try {
            Method method = reportData.getClass().getDeclaredMethod(toColumnMethodName(column.getColumn()), null);
            Object obj = method.invoke(reportData, null);
            if (obj == null) {
                return "";
            }

            if (Objects.equals(method.getReturnType(), String.class)) {
                return obj.toString();
            } else if (Objects.equals(method.getReturnType(), Integer.class) ||
                    Objects.equals(method.getReturnType(), Long.class) ||
                    Objects.equals(method.getReturnType(), Double.class) ||
                    Objects.equals(method.getReturnType(), Float.class) ||
                    Objects.equals(method.getReturnType(), Integer.class) ||
                    Objects.equals(method.getReturnType(), Boolean.class)) {
                return obj.toString();
            } else if (Objects.equals(method.getReturnType(), BigDecimal.class)) {
                BigDecimal val = (BigDecimal) obj;
                if(val == null) return "";
                return val.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
            } else if (Objects.equals(method.getReturnType(), Date.class)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return sdf.format((Date) obj);
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

}
