package cn.looty.example.生成PPTX.utils;

import cn.looty.example.生成PPTX.model.ProductBenefitLineDto;
import cn.looty.example.生成PPTX.model.WeeklyReportDto;
import cn.looty.example.生成PPTX.utils.encrypt.Md5Util;
import com.alibaba.fastjson.JSON;

import java.util.Locale;

public class ChartKeyUtil {
    public static String newProductBenefitKey(ProductBenefitLineDto body) {
        return Md5Util.encryption("RUINING_CHART_NET_WORTH#" + JSON.toJSONString(body)).toUpperCase(Locale.ENGLISH);
    }

    public static String newWeeklyReportKey(WeeklyReportDto body) {
        return Md5Util.encryption("RUINING_CHART_WEEKLY_REPORT#" + JSON.toJSONString(body)).toUpperCase(Locale.ENGLISH);
    }
}
