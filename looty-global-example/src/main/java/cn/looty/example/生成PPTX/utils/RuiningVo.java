package cn.looty.example.生成PPTX.utils;

import org.springframework.util.CollectionUtils;

import java.awt.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;

public class RuiningVo {
    // 周报日期范围
    private String rangeDate;
    // 睿凝产品集
    private List<List<String>> products;
    // 乾德500
    private ExponentRatioVo exp500;
    // 乾德1000
    private ExponentRatioVo exp1000;
    // 量化中性DMA
    private ExponentRatioVo dma;
    private Map<String, Integer> rowCountMap;
    private List<Integer> maxTextLengths;

    // 市场风格
    private String marketStyle;
    // 风格因子
    private String styleFactor;
    // 股指期货基差
    private String futuresBasis;
    // 自评
    private String evaluate;
    // 产品表格列数据
    private boolean existWeeklyYield = false;
    private int removeCellCount = 0;
    private int cellCount = 16;

    private int observeParagraphCount;
    private int evaluateParagraphCount;

    private Map<String, String> paramMap;
    private Set<String> lineSpacings;
    private Map<String, List<ExponentVo>> tableMap;

    public void paramLoading(boolean lineChartAdaptiveHeight, String rangeDateForCe5, String rangeDateForCe10, String rangeDateForDma) {
        this.rowCountMap = new HashMap<>();
        this.paramMap = new HashMap<>();
        this.tableMap = new HashMap<>();
        this.lineSpacings = new HashSet<>();
        this.maxTextLengths = new ArrayList<>();
        this.paramMap.put("${rangeTime}", StringUtil.isEmpty(this.rangeDate) ? "" : this.rangeDate);

        int maxLen = 0;
        if (StringUtil.isNotEmpty(this.marketStyle)) {
            this.paramMap.put("${marketStyle}", this.marketStyle);
            maxLen = this.marketStyle.length();
        } else {
            String mms = "本周股票指数普遍下跌，指数换手率分化。沪深300指数较上周上浮，中证500、中证1000指数换手率相较于上周有一定下滑，两市平均成交额7300亿，北向资金净流出约59亿元，从个股涨跌数来看，指数外获利能力一般。";
            this.paramMap.put("${marketStyle}", mms);
            maxLen = mms.length();
        }
        if (StringUtil.isNotEmpty(this.styleFactor)) {
            this.paramMap.put("${styleFactor}", this.styleFactor);
            if (this.styleFactor.length() > maxLen) {
                maxLen = this.styleFactor.length();
            }
        } else {
            String msf = "本周风格因子大多下跌且幅度较大。司量因子反转上涨；Beta因子、波动率因子反转大幅下跌；市值、流动性、非线性市值因子下跌。预期本周Alpha收益环境一般。";
            this.paramMap.put("${styleFactor}", msf);
            if (msf.length() > maxLen) {
                maxLen = msf.length();
            }
        }
        if (StringUtil.isNotEmpty(this.futuresBasis)) {
            this.paramMap.put("${futuresBasis}", this.futuresBasis);
            if (this.futuresBasis.length() > maxLen) {
                maxLen = this.futuresBasis.length();
            }
        } else {
            String mfb = "本周IF近月合约依旧维持小幅升水状态，IC、IM近月合约由升水转为贴水。为中性产品增厚了一些收益。";
            this.paramMap.put("${futuresBasis}", mfb);
            if (mfb.length() > maxLen) {
                maxLen = mfb.length();
            }
        }
        int paragraphCount = 0;
        if (maxLen > 0) {
            paragraphCount = maxLen / 18;
            if (maxLen % 18 > 0) {
                paragraphCount++;
            }
        }
        this.observeParagraphCount = paragraphCount;

        int evaLen = 0;
        if (StringUtil.isNotEmpty(this.evaluate)) {
            this.paramMap.put("${evaluate}", this.evaluate);
            evaLen = this.evaluate.length();
        } else {
            String eva = "市场普遍下跌的一周。双创和中小板指在新年的开局格外低迷，与微盘超脱于大市的表现相比非常突兀。在这个估值水平和大环境下，感觉红利概念从投资的会计优势和进出规则上看，有成为可以炒作对象的潜质。回到我们的产品上，我们继续保持了各线产品（在超额角度上）的新高，其中多空策略组由于alpha自由度更高，从而表现偏好；300策略组由于我们对对标指数各项偏离度的要求严格，并没有斩获。我们本周整体上从Size和SizeNL上各获得了+10bps的收益，为各风格贡献之最。总体上，本周我们在Sector上有轻微损失，而Style对超额的贡献甚至多过Idio。";
            this.paramMap.put("${evaluate}", eva);
            evaLen = eva.length();
        }
        int evaParagraphCount = 0;
        if (evaLen > 0) {
            evaParagraphCount = evaLen / 60;
            if (evaLen % 60 > 0) {
                evaParagraphCount++;
            }
        }
        this.evaluateParagraphCount = evaParagraphCount;

        if (!CollectionUtils.isEmpty(this.products) && this.products.size() > 0) {
            int maxTextLengthHead = 0;
            int maxTextLengthRow1 = 0;
            int maxTextLengthRow2 = 0;
            int maxTextLengthRow3 = 0;
            int maxTextLengthRow4 = 0;
            int len = this.products.size();
            for (int i = 1; i <= 4; i++) {
                String val = "/";
                if (i < len) {
                    val = this.products.get(i).get(0);
                }
                if (i == 4 && Objects.equals(val, "DMA2号")) {
                    val = "开宝1号（DMA）";
                }
                setParam("${proName" + i + "}", val);
            }
            int weeklyYieldIndex = 0;
            List<String> productTitles = this.products.get(0);
            int maxColumnCount = productTitles.size();
            this.cellCount = productTitles.size();
            for (int ci = 1; ci < maxColumnCount; ci++) {
                String ti = toVal(productTitles.get(ci));
                if (Objects.equals(ti, "本周收益率") ||
                        Objects.equals(ti, "超额本周收益率") ||
                        Objects.equals(ti, "本周收益率\n（超额）")) {
                    weeklyYieldIndex = ci;
                    this.existWeeklyYield = true;
                    break;
                }
            }
            if (maxColumnCount < 16) {
                int rcc = 16 - maxColumnCount;
                if (!this.existWeeklyYield) {
                    rcc--;
                }
                this.removeCellCount = rcc;
            }
            int valueIndex = 1;
            if (maxColumnCount > 1) {
                for (int ci = 1; ci < maxColumnCount; ci++) {
                    String head = len > 0 ? toVal(this.products.get(0).get(ci)) : "/";
                    String val1 = len > 1 ? toVal(this.products.get(1).get(ci)) : "/";
                    String val2 = len > 2 ? toVal(this.products.get(2).get(ci)) : "/";
                    String val3 = len > 3 ? toVal(this.products.get(3).get(ci)) : "/";
                    String val4 = len > 4 ? toVal(this.products.get(4).get(ci)) : "/";
                    if (head.length() > maxTextLengthHead) {
                        maxTextLengthHead = head.length();
                    }
                    if (val1.length() > maxTextLengthRow1) {
                        maxTextLengthRow1 = val1.length();
                    }
                    if (val2.length() > maxTextLengthRow2) {
                        maxTextLengthRow2 = val2.length();
                    }
                    if (val3.length() > maxTextLengthRow3) {
                        maxTextLengthRow3 = val3.length();
                    }
                    if (val4.length() > maxTextLengthRow4) {
                        maxTextLengthRow4 = val4.length();
                    }

                    if (weeklyYieldIndex == ci) {
                        // 本周收益率
                        setParam("${t_3}", head);
                        setParam("${v1_3}", val1);
                        setParam("${v2_3}", val2);
                        setParam("${v3_3}", val3);
                        setParam("${v4_3}", val4);
                    } else {
                        setParam("${t_" + valueIndex + "}", head);
                        setParam("${v1_" + valueIndex + "}", val1);
                        setParam("${v2_" + valueIndex + "}", val2);
                        setParam("${v3_" + valueIndex + "}", val3);
                        setParam("${v4_" + valueIndex + "}", val4);
                        if (valueIndex == 2) {
                            valueIndex = 4;
                        } else {
                            valueIndex++;
                        }
                    }
                }
            }
            this.maxTextLengths.add(maxTextLengthHead);
            this.maxTextLengths.add(maxTextLengthRow1);
            this.maxTextLengths.add(maxTextLengthRow2);
            this.maxTextLengths.add(maxTextLengthRow3);
            this.maxTextLengths.add(maxTextLengthRow4);
        }

        if (this.exp500 != null) {
            setParam("${exp5.item}", StringUtil.isEmpty(this.exp500.getItemName()) ? "累计超额" : this.exp500.getItemName());
            setParam("${exp5.up}", this.exp500.getItemUp());
            setParam("${exp5.down}", this.exp500.getItemDown());
            setParam("${exp5.rangeDate}", rangeDateForCe5);
            this.tableMap.put("${exp5.item}", this.exp500.getExponents());
            rowCountMap.put("EXP_500_ROW", lineChartAdaptiveHeight ? this.exp500.getExponents().size() : 0);
        }

        if (this.exp1000 != null) {
            setParam("${exp10.item}", StringUtil.isEmpty(this.exp1000.getItemName()) ? "累计超额" : this.exp1000.getItemName());
            setParam("${exp10.up}", this.exp1000.getItemUp());
            setParam("${exp10.down}", this.exp1000.getItemDown());
            setParam("${exp10.rangeDate}", rangeDateForCe10);
            this.tableMap.put("${exp10.item}", this.exp1000.getExponents());
            rowCountMap.put("EXP_1000_ROW", lineChartAdaptiveHeight ? this.exp1000.getExponents().size() : 0);
        }

        if (this.dma != null) {
            setParam("${dma.item}", StringUtil.isEmpty(this.dma.getItemName()) ? "累计收益率" : this.dma.getItemName());
//            setParam("${dma.up}", this.dma.getItemUp());
//            setParam("${dma.down}", this.dma.getItemDown());
//            this.tableMap.put("${dma.item}", this.dma.getExponents());
            setParam("${dma.up}", this.dma.getItemDown());
            setParam("${dma.down}", this.dma.getItemUp());
            List<ExponentVo> dmaList = new ArrayList<>();
            this.dma.getExponents().forEach(exp -> {
                ExponentVo vo = new ExponentVo();
                vo.setName(exp.getName());
                vo.setUp(exp.getDown());
                vo.setDown(exp.getUp());
                dmaList.add(vo);
            });
            setParam("${dma.rangeDate}", rangeDateForDma);
            this.tableMap.put("${dma.item}", dmaList);
            rowCountMap.put("DMA_ROW", lineChartAdaptiveHeight ? this.dma.getExponents().size() : 0);
        }
    }

    public List<Integer> toMaxTextLengths() {
        return this.maxTextLengths;
    }

    private String toVal(String val) {
        if (StringUtil.isEmpty(val) || Objects.equals(val.trim(), "")) {
            return "/";
        }
        return val.trim();
    }

    public Map<String, Integer> getRowCountMap() {
        return rowCountMap;
    }

    public int getObserveParagraphCount() {
        return observeParagraphCount;
    }

    public int getEvaluateParagraphCount() {
        return evaluateParagraphCount;
    }

    public int getCellCount() {
        return this.cellCount;
    }

    public boolean isExistWeeklyYield() {
        return existWeeklyYield;
    }

    public int getRemoveCellCount() {
        return removeCellCount;
    }

    public Map<String, String> toParamMap() {
        return this.paramMap;
    }

    public boolean isLineSpacing(String key) {
        return this.lineSpacings.contains(key);
    }

    public List<ExponentVo> toExponents(String tableName) {
        return this.tableMap.get(tableName);
    }

    public boolean isAddTableRow(String key) {
        return this.tableMap.containsKey(key);
    }

    private void setParam(String key, String val) {
        this.paramMap.put(key, StringUtil.isEmpty(val) ? "" : val);
        if (StringUtil.isNotEmpty(val) && StringUtil.containsIgnoreCase(val, "\n")) {
            this.lineSpacings.add(key);
        }
    }

    public void setRangeDate(String rangeDate) {
        this.rangeDate = rangeDate;
    }

    public void setProducts(List<List<String>> products) {
        this.products = products;
    }

    public void setExp500(ExponentRatioVo exp500) {
        this.exp500 = exp500;
    }

    public void setExp1000(ExponentRatioVo exp1000) {
        this.exp1000 = exp1000;
    }

    public void setDma(ExponentRatioVo dma) {
        this.dma = dma;
    }

    public void setMarketStyle(String marketStyle) {
        this.marketStyle = marketStyle;
    }

    public void setStyleFactor(String styleFactor) {
        this.styleFactor = styleFactor;
    }

    public void setFuturesBasis(String futuresBasis) {
        this.futuresBasis = futuresBasis;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }
}
