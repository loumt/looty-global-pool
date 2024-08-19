package cn.looty.example.生成PPTX.utils;


import org.apache.poi.sl.usermodel.TextBox;
import org.apache.poi.sl.usermodel.TextParagraph;
import org.apache.poi.xslf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PowerPointUtil {

    /**
     * PPT文件
     */
    private XMLSlideShow pptx;

    public PowerPointUtil(XMLSlideShow pptx) {
        this.pptx = pptx;
    }

    /**
     * 从幻灯片中获取表格列表
     *
     * @param slide 幻灯片
     * @return 表格列表
     * @Author Nile (QQEmail:576109623)
     * @Date 16:55 2022/11/5
     */
    public List<XSLFTable> getAllTableFromSlide(XSLFSlide slide) {
        List<XSLFTable> tables = new ArrayList<>();
        for (XSLFShape shape : slide.getShapes()) {
            if (shape instanceof XSLFTable) {
                tables.add((XSLFTable) shape);
            }
        }
        return tables;
    }

    /**
     * 替换段落内的标签文本
     *
     * @param paragraph 段落
     * @param paramMap  参数Map
     * @param start     替换位置索引
     * @return void
     * @Author Nile (QQEmail:576109623)
     * @Date 16:55 2022/11/5
     */
    public void replaceTagInParagraph(XSLFTextParagraph paragraph, Map<String, String> paramMap, int start) {
        paragraph.setTextAlign(TextParagraph.TextAlign.CENTER);
        String paraText = paragraph.getText();
        // 正则匹配，循环匹配替换
        String regEx = "\\$\\{.+?\\}";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(paraText);
        String key = null;
        while (matcher.find()) {
            StringBuilder keyWord = new StringBuilder();
            // 获取占位符起始位置所在run的索引
            int s = getRunIndex(paragraph, "${", start);
            if (s < start) {
                // 重复递归，直接返回
                return;
            }
            // 获取占位符结束位置所在run的索引
            int e = getRunIndex(paragraph, "}", start);
            // 存放标签
            String rs = matcher.group(0);
            // 存放 key
            keyWord.append(rs);
            // 获取标签所在 run 的全部文字
            String text = getRunsT(paragraph, s, e + 1);
            // 如果没在 paramMap，则不做替换
            String v = nullToDefault(paramMap.get(keyWord.toString()), keyWord.toString());
            // 没有找到这个标签所对应的值，那么就直接替换成标签的值（业务需求来着，找不到不替换）
            setText(paragraph.getTextRuns().get(s), text.replace(rs, v));
            // 存在 ${ 和 } 不在同一个CTRegularTextRun内的情况，将其他替换为空字符
            for (int i = s + 1; i < e + 1; i++) {
                setText(paragraph.getTextRuns().get(i), "");
            }
            start = e + 1;
        }
    }

    /**
     * 解析一个shape内的所有段落
     *
     * @param shape shape
     * @return 文本段落列表
     * @Author Nile (QQEmail:576109623)
     * @Date 16:56 2022/11/5
     */
    public List<XSLFTextParagraph> parseParagraph(XSLFShape shape) {
        if (shape instanceof XSLFAutoShape) {
            XSLFAutoShape autoShape = (XSLFAutoShape) shape;
            return autoShape.getTextParagraphs();
        } else if (shape instanceof XSLFTextShape) {
            XSLFTextShape textShape = (XSLFTextShape) shape;
            return textShape.getTextParagraphs();
        } else if (shape instanceof XSLFFreeformShape) {
            XSLFFreeformShape freeformShape = (XSLFFreeformShape) shape;
            return freeformShape.getTextParagraphs();
        } else if (shape instanceof TextBox) {
            TextBox textBox = (TextBox) shape;
            return textBox.getTextParagraphs();
        }
        return new ArrayList<>();
    }

    /**
     * 获取段落下特定索引的textRun的值
     *
     * @param paragraph 段落
     * @param start     起始位置
     * @param end       终止位置
     * @return run值
     * @Author Nile (QQEmail:576109623)
     * @Date 17:17 2022/11/5
     */
    private String getRunsT(XSLFTextParagraph paragraph, int start, int end) {
        List<XSLFTextRun> textRuns = paragraph.getTextRuns();
        StringBuilder t = new StringBuilder();
        for (int i = start; i < end; i++) {
            t.append(textRuns.get(i).getRawText());
        }
        return t.toString();
    }

    /**
     * 设置run的值
     *
     * @param run run
     * @param t   run值
     * @return void
     * @Author Nile (QQEmail:576109623)
     * @Date 17:18 2022/11/5
     */
    private void setText(XSLFTextRun run, String t) {
        run.setText(t);
    }

    /**
     * 获取word在段落中出现第一次的run的索引
     *
     * @param paragraph 段落
     * @param word      目标值
     * @param start     索引
     * @return void
     * @Author Nile (QQEmail:576109623)
     * @Date 17:19 2022/11/5
     */
    private int getRunIndex(XSLFTextParagraph paragraph, String word, int start) {
        List<CTRegularTextRun> rList = paragraph.getXmlObject().getRList();
        for (int i = (Math.max(start, 0)); i < rList.size(); i++) {
            String text = rList.get(i).getT();
            if (text.contains(word)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * toString方法，空则返回默认值
     *
     * @param o          对象
     * @param defaultStr 默认值
     * @return toString
     * @Author Nile (QQEmail:576109623)
     * @Date 17:20 2022/11/5
     */
    private String nullToDefault(Object o, String defaultStr) {
        if (o == null) {
            return defaultStr;
        }
        return o.toString();
    }


}
