package cn.looty.example.生成PPTX.utils;

import org.apache.poi.xslf.usermodel.*;

public enum PowerPointShapeEnum {
    // 建隆量化中性产品图表
    JL_DOTTED_LINE_UP("JL_DOTTED_LINE_UP", "CHAT_JL", "直接连接符 11", XSLFConnectorShape.class, null), // 文字：上方虚线
    JL_CHART_TITLE("JL_CHART_TITLE", "CHAT_JL", "object 14#JL", XSLFTextBox.class, "建隆量化中性产品"),   // 文字：建隆量化中性产品
//    JL_RADAR_TITLE("JL_RADAR_TITLE", "CHAT_JL", "文本框 37", XSLFTextBox.class, null), // 文字：本周子策略及收益
    JL_IMG_TRIANGLE("JL_IMG_TRIANGLE", "CHAT_JL", "组合 17", XSLFGroupShape.class, null), // 图标：前三角形
    JL_CHART_CPJZ("JL_CHART_CPJZ", "CHAT_JL", "文本框 6", XSLFTextBox.class, null),   // 文字：产品净值
    JL_CHART_ZDSYL("JL_CHART_ZDSYL", "CHAT_JL", "文本框 8", XSLFTextBox.class, null),   // 文字：周度收益率
    JL_CHART_DESP("JL_CHART_DESP", "CHAT_JL", "组合 103", XSLFGroupShape.class, null), // 描述：产品净值周度收益

    // 乾德500指增产品图表
    QD500_DOTTED_LINE_UP("QD500_DOTTED_LINE_UP", "CHAT_QD500", "直接连接符 41", XSLFConnectorShape.class, null), // 文字：上方虚线
    QD500_IMG_TRIANGLE("QD500_IMG_TRIANGLE", "CHAT_QD500", "组合 42", XSLFGroupShape.class, null), // 图标：前三角形
    QD500_CHART_TITLE("QD500_CHART_TITLE", "CHAT_QD500", "object 14#QD500", XSLFTextBox.class, "乾德500指增产品"), // 文字：乾德500指增产品
//    QD500_CHART_REMARK("QD500_CHART_REMARK", "CHAT_QD500", "文本框 60", XSLFTextBox.class, null), // 文字：注： 均为实盘业绩，实线部分为新策略运行阶段
    QD500_CHART_CESYL("QD500_CHART_CESYL", "CHAT_QD500", "文本框 12", XSLFTextBox.class, null), // 文字：超额收益率
    QD500_CHART_CPJZ("QD500_CHART_CPJZ", "CHAT_QD500", "文本框 35", XSLFTextBox.class, null), // 文字：产品净值
    QD500_CHART_DESP("QD500_CHART_DESP", "CHAT_QD500", "组合 66", XSLFGroupShape.class, null), // 描述：中证500产品净值累计超额收益

    // 乾德1000指增产品图表
    QD1000_DOTTED_LINE_UP("QD1000_DOTTED_LINE_UP", "CHAT_QD1000", "直接连接符 49", XSLFConnectorShape.class, null), // 文字：上方虚线
    QD1000_IMG_TRIANGLE("QD1000_IMG_TRIANGLE", "CHAT_QD1000", "组合 73", XSLFGroupShape.class, null), // 图标：前三角形
    QD1000_CHART_TITLE("QD1000_CHART_TITLE", "CHAT_QD1000", "object 14#QD1000", XSLFTextBox.class, "乾德1000指增产品"),   // 文字：乾德1000指增产品
//    QD1000_CHART_REMARK("QD1000_CHART_REMARK", "CHAT_QD1000", "文本框 51", XSLFTextBox.class, null), // 文字：注： 均为实盘业绩，实线部分为新策略运行阶段
    QD1000_CHART_CESYL("QD1000_CHART_CESYL", "CHAT_QD1000", "文本框 85#QD1000", XSLFTextBox.class, "超额收益率"), // 文字：超额收益率
    QD1000_CHART_CPJZ("QD1000_CHART_CPJZ", "CHAT_QD1000", "文本框 86", XSLFTextBox.class, null), // 文字：产品净值
    QD1000_CHART_DESP("QD1000_CHART_DESP", "CHAT_QD1000", "组合 16", XSLFGroupShape.class, null), // 描述：中证1000产品净值累计超额收益

    // 量化中性DMA产品图表
    DMA_DOTTED_LINE_UP("DMA_DOTTED_LINE_UP", "CHAT_DMA", "直接连接符 50", XSLFConnectorShape.class, null), // 文字：上方虚线
    DMA_IMG_TRIANGLE("DMA_IMG_TRIANGLE", "CHAT_DMA", "组合 23", XSLFGroupShape.class, null), // 图标：前三角形
    DMA_CHART_TITLE("DMA_CHART_TITLE", "CHAT_DMA", "object 14#DMA", XSLFTextBox.class, "量化中性DMA产品"), // 文字：量化中性DMA产品
    DMA_CHART_DESP("DMA_CHART_DESP", "CHAT_DMA", "组合 82", XSLFGroupShape.class, null), // 描述：DMA1号DMA2号
//    DMA_CHART_DESP("DMA_CHART_DESP", "CHAT_DMA", "组合 57", XSLFGroupShape.class, null), // 描述：DMA1号DMA2号
    DMA_CHART_CPLJSYL("DMA_CHART_CPLJSYL", "CHAT_DMA", "文本框 85#DMA", XSLFTextBox.class, "产品累计收益率"), // 文字：超额收益率

    // 周围观察
    OBS_DOTTED_LINE_UP("OBS_DOTTED_LINE_UP", "CON_OBS", "直接连接符 78", XSLFConnectorShape.class, null), // 文字：上方虚线
    OBS_OBSERVE("OBS_OBSERVE", "CON_OBS", "object 2#OBS", XSLFTextBox.class, "周度观察"), // 文字：周度观察
    OBS_MARKET_STYLE("OBS_MARKET_STYLE", "CON_OBS", "文本框 53", XSLFTextBox.class, null), // 内容：市场风格${marketStyle}
    OBS_STYLE_FACTOR("OBS_STYLE_FACTOR", "CON_OBS", "文本框 39", XSLFTextBox.class, null), // 内容：风格因子${styleFactor}
    OBS_FUTURES_BASIS("OBS_FUTURES_BASIS", "CON_OBS", "文本框 55", XSLFTextBox.class, null), // 内容：股指期货基差${futuresBasis}
    OBS_MARKET_STYLE_ROUND_FRAME("OBS_MARKET_STYLE_ROUND_FRAME", "CON_OBS", "矩形: 圆角 180#MS", XSLFAutoShape.class, "市场风格"), // 圆角背景框：风格因子
    OBS_STYLE_FACTOR_ROUND_FRAME("OBS_STYLE_FACTOR_ROUND_FRAME", "CON_OBS", "矩形: 圆角 180#SF", XSLFAutoShape.class, "风格因子"), // 圆角背景框：风格因子
    OBS_FUTURES_BASIS_ROUND_FRAME("OBS_FUTURES_BASIS_ROUND_FRAME", "CON_OBS", "矩形: 圆角 180#FB", XSLFAutoShape.class, "股指期货基差"), // 圆角背景框：股指期货基差
    OBS_GROUP_FRAME_LEFT("OBS_GROUP_FRAME_LEFT", "CON_OBS", "组合 10", XSLFGroupShape.class, null),    // 左侧组合框
    OBS_GROUP_FRAME_CENTER("OBS_GROUP_FRAME_CENTER", "CON_OBS", "组合 90", XSLFGroupShape.class, null),    // 中间组合框
    OBS_GROUP_FRAME_RIGHT("OBS_GROUP_FRAME_RIGHT", "CON_OBS", "组合 40", XSLFGroupShape.class, null),    // 右侧组合框

    // 睿凝自评
    EVA_DOTTED_LINE_UP("EVA_DOTTED_LINE_UP", "CON_EVA", "直接连接符 79", XSLFConnectorShape.class, null), // 文字：上方虚线
    EVA_RUINING_EVALUATE("EVA_RUINING_EVALUATE", "CON_EVA", "object 2#EVA", XSLFTextBox.class, "睿凝自评"), // 文字：睿凝自评
    EVA_EVALUATE("EVA_EVALUATE", "CON_EVA", "文本框 24", XSLFTextBox.class, null), // 内容：${evaluate}

    // 联系我们
    REL_DOTTED_LINE_UP("REL_DOTTED_LINE_UP", "CON_REL", "直接连接符 87", XSLFConnectorShape.class, null), // 文字：上方虚线
    REL_CONTENT("REL_CONTENT", "CON_REL", "组合 5", XSLFGroupShape.class, null),    // 联系我们内容

    // 表格
    GRID_PRODUCT("GRID_PRODUCT", "CHAT_PRODUCT", "object 5#PRODUCT", XSLFTable.class, "${proName1}"), // 表格：睿凝产品业绩概览
    GRID_QD500("GRID_QD500", "CHAT_QD500", "object 5#QD500", XSLFTable.class, "${exp5.item}"), // 表格：乾德500指增产品
    GRID_QD1000("GRID_QD1000", "CHAT_QD1000", "object 5#QD1000", XSLFTable.class, "${exp10.item}"), // 表格：乾德1000指增产品
    GRID_DMA("GRID_DMA", "CHAT_DMA", "object 5#DMA", XSLFTable.class, "${dma.item}");  // 表格：量化中性DMA产品


    private final String code;
    private final String group;
    private final String shapeName;
    private final Class clazz;
    private final String text;

    PowerPointShapeEnum(String code, String group, String shapeName, Class clazz, String text) {
        this.code = code;
        this.group = group;
        this.shapeName = shapeName;
        this.clazz = clazz;
        this.text = text;
    }

    public String code() {
        return this.code;
    }

    public String toText() {
        return this.text;
    }

    public String toGroup() {
        return this.group;
    }

    public String toShapeName() {
        return this.shapeName;
    }

    public Class toClass() {
        return this.clazz;
    }
}
