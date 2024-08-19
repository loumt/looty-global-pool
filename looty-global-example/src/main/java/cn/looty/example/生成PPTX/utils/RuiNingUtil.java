package cn.looty.example.生成PPTX.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class RuiNingUtil {

    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    private final static SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy.MM.dd");
    private final static SimpleDateFormat monthFormat = new SimpleDateFormat("yy/MM");

    public final static String AXIS_LEFT_OVERAGE_YIELD = "LEFT_OVERAGE_YIELD";
    public final static String AXIS_RIGHT_OVERAGE_YIELD = "RIGHT_OVERAGE_YIELD";
    public final static String AXIS_LEFT_NEUTRAL = "LEFT_NEUTRAL";
    public final static String AXIS_RIGHT_NEUTRAL = "RIGHT_NEUTRAL";
    public final static String AXIS_LEFT_DMA = "LEFT_DMA";
    public final static String AXIS_WEEKLY_STRATEGY = "LEFT_WEEKLY_STRATEGY";
    public final static double DEFAULT_GRID_ROW_HEIGHT = 31D;

    public static boolean isPosterior(Date date, long deadlineTime) {
        if (date.getTime() > deadlineTime) {
            return true;
        }
        return false;
    }

    public static String dateToMonth(Date date) {
        if (date == null) {
            return "";
        }
        try {
            return monthFormat.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    public static Date stringToDate(String day) {
        if (StringUtil.isEmpty(day)) {
            return null;
        }
        try {
            return dateFormat.parse(day);
        } catch (Exception e) {
            return null;
        }
    }


    public static String stringToString2(String day) {
        if (StringUtil.isEmpty(day)) {
            return "";
        }
        try {
            Date date = dateFormat.parse(day);
            if (date != null) {
                return dateFormat2.format(date);
            }
        } catch (Exception e) {
        }
        return "";
    }

    public static List<String> toDespCode() {
        return Arrays.asList(
                PowerPointShapeEnum.QD500_CHART_DESP.code(),
                PowerPointShapeEnum.QD500_CHART_CPJZ.code(),
                PowerPointShapeEnum.QD500_CHART_CESYL.code(),
                PowerPointShapeEnum.QD1000_CHART_DESP.code(),
                PowerPointShapeEnum.QD1000_CHART_CPJZ.code(),
                PowerPointShapeEnum.QD1000_CHART_CESYL.code(),
                PowerPointShapeEnum.DMA_CHART_DESP.code(),
                PowerPointShapeEnum.DMA_CHART_CPLJSYL.code());
    }

    public static Long toDeadlineTime() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
                    .parse("2023-03-31 23:59:59.999").getTime();
        } catch (Exception e) {
            return new Date().getTime();
        }
    }

    public static BigDecimal doubleToSixDecimal(Double val) {
        if (val == null) {
            return null;
        }
        return new BigDecimal(val).setScale(6, BigDecimal.ROUND_HALF_UP);
    }

    public static String decimalToString(BigDecimal val) {
        if (val == null) {
            return "null";
        }
        return val.setScale(8, BigDecimal.ROUND_DOWN).toPlainString();
    }

    public static BigDecimal twoDecimalPlaces(BigDecimal val) {
        if (val == null) {
            return null;
        }
        return val.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static String twoStringDecimalPlaces(BigDecimal val) {
        if (val == null) {
            return "";
        }
        return twoDecimalPlaces(val).toPlainString();
    }
}
