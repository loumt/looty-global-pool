package cn.looty.common.utils;

import cn.hutool.core.util.RandomUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author xiaokong
 * @version 1.0.0
 * @DESCRIPTION:JAVA验证码工具类
 * @TODO:未实现的事情
 * @date 2018年3月29日
 * @file com.frame.util.CodeUtil.java
 */
public class CodeUtil {
    private static char[] codeSequence = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private static Random random = new Random();

    /**
     * @param width:图片长
     * @param height:图片高
     * @param fontHeight:字高
     * @param codeCount:字符总数
     * @return code:字符型验证码/codePic:验证码图片
     * @DESCRIPTION:生成一个map集合,fontHeight*codeCount>width 和 fontHeight>height
     * @TODO:未实现的事情
     */
    public static Map<String, Object> generateCodeAndPic(int width, int height, int fontHeight, int codeCount) {
        // 定义图像buffer
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // Graphics2D gd = buffImg.createGraphics();
        // Graphics2D gd = (Graphics2D) buffImg.getGraphics();
        Graphics gd = buffImg.getGraphics();
        // 创建一个随机数生成器类
        // 将图像填充为白色
        gd.setColor(Color.WHITE);
        gd.fillRect(0, 0, width, height);

        // 创建字体，字体的大小应该根据图片的高度来定。
        Font font = new Font("Fixedsys", Font.BOLD, fontHeight);
        // 设置字体。
        gd.setFont(font);

        // 画边框。
        gd.setColor(Color.BLACK);
        gd.drawRect(0, 0, width - 1, height - 1);

        // 随机产生40条干扰线，使图象中的认证码不易被其它程序探测到。
        gd.setColor(Color.BLACK);
        int bound = 12;
        for (int i = 0; i < 30; i++) {
            int x = toRandomNumber(width);
            int y = toRandomNumber(height);
            gd.drawLine(x, y, x + toRandomNumber(bound), y + toRandomNumber(bound));
        }
        // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
        StringBuffer randomCode = new StringBuffer();
        // 随机产生codeCount数字的验证码。
        int codeSequenceLen = codeSequence.length;
        for (int i = 0; i < codeCount; i++) {
            // 得到随机产生的验证码数字。
            String code = String.valueOf(codeSequence[toRandomNumber(codeSequenceLen)]);
            // 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
            // 用随机产生的颜色将验证码绘制到图像中。
            gd.setColor(new Color(toColor(), toColor(), toColor()));
            gd.drawString(code, (width - fontHeight * codeCount) / 2 + (i) * fontHeight, (height - fontHeight) / 2 + fontHeight);
            // 将产生的四个随机数组合在一起。
            randomCode.append(code);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        //存放验证码
        map.put("code", randomCode);
        //存放生成的验证码BufferedImage对象
        map.put("codePic", buffImg);
        return map;
    }

    private static int toRandomNumber(int val) {
        return RandomUtil.randomInt(val);
    }

    private static int toColor() {
        return RandomUtil.randomInt(255);
    }
}