package cn.looty.tool;

import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.sl.draw.DrawFactory;
import org.apache.poi.xslf.usermodel.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static cn.hutool.core.io.FileUtil.mkdir;


/**
 * @Filename: PPTX2PNG
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-08-06 11:24
 */
public class PPTX2PNG {

    //低版本的poi转换ppt时会丢失一些排版，例如图层重叠4.1.1 -》 5.1.0解决

    //默认清晰度会损失，scale调节ppt放大倍数，写入png会较为清晰

    private static void convert(String filePath, String fileName) {
        String format = "PNG";
        float scale = 5;


        String outRoot = filePath + fileName.substring(0, fileName.indexOf(".")) + File.separator;
        System.out.println(" ==> " + outRoot);

        mkdir(outRoot);

        String pptName = filePath + fileName;
        System.out.println(" ==>" + pptName);
        File pptxFile = new File(pptName);

        FileInputStream fis = null;
        XMLSlideShow ppt = null;

        try {
//            GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
//            String[] availableFontFamilyNames = e.getAvailableFontFamilyNames();

            fis = new FileInputStream(pptxFile);
            ppt = new XMLSlideShow(fis);
            List<XSLFSlide> slides = ppt.getSlides();


            Set<Integer> slidesNums = slidesIndexes(slides);
            if (slidesNums.isEmpty()) {
                System.out.println("无转化页");
                return;
            }
            Dimension sheet = ppt.getPageSize();
//            int width = sheet.width;
            int width = (int) (sheet.width * scale);
//            int height = sheet.height;
            int height = (int) (sheet.height * scale);

            for (Integer slidesNum : slidesNums) {
                XSLFSlide slide = slides.get(slidesNum);
                for (XSLFShape shape : ppt.getSlides().get(slidesNum).getShapes()) {
                    if (shape instanceof XSLFTextShape) {
                        XSLFTextShape tsp = (XSLFTextShape) shape;

                        for (XSLFTextParagraph p : tsp) {
                            for (XSLFTextRun t : p) {
                                t.setFontFamily("宋体");
                            }
                        }
                    }
                }

                String title = slide.getTitle();
                if (StringUtils.isNoneBlank(title)) {
                    System.out.println("TITLE:" + title);
                }

                BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = img.createGraphics();
                graphics.setBackground(Color.WHITE);

                //图像放大
//                AffineTransform at = new AffineTransform();
//                at.setToScale(scale, scale);
//                graphics.setTransform(at);

                graphics.setBackground(slide.getBackground().getFillColor());
//                DrawFactory.getInstance(graphics).fixFonts(graphics);
//                instance.getFontManager(graphics)

                Map<Object,Object> renderingMaps = new HashMap<Object,Object>(){
                    {
//                        put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                        put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//                        put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
                        put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
//                        put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
                        put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
//                        put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                        put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
                        put(RenderingHints.KEY_TEXT_LCD_CONTRAST, 150);
                    }
                };
                graphics.addRenderingHints(renderingMaps);

                graphics.scale(scale, scale);

//                slide.setFollowMasterGraphics(true);
//                slide.setFollowMasterColourScheme(true);
//                slide.setFollowMasterBackground(true);
//                slide.setFollowMasterObjects(true);

                slide.draw(graphics);

                File fos = new File(outRoot, slidesNum + "." + format);
                ImageIO.write(img, format, fos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(ppt != null){
                try {
                    ppt.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static Set<Integer> slidesIndexes(List<XSLFSlide> slides) {
        Set<Integer> slideIndex = new TreeSet<>();
        for (int i = 0; i < slides.size(); i++) {
            slideIndex.add(i);
        }
        return slideIndex;
    }

    public static void main(String[] args) {
//        convert("F:\\PVZ\\", "858585.PPTX");
        convert("F:\\PVZ\\", "1722933062548.PPTX");
    }
}
