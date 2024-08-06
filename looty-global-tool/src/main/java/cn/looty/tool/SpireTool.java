package cn.looty.tool;

import cn.hutool.core.collection.CollectionUtil;
import com.spire.presentation.FileFormat;
import com.spire.presentation.ISlide;
import com.spire.presentation.Presentation;
import com.spire.presentation.collections.SlideCollection;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 * @Filename: SpireTool
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-08-06 14:09
 */
public class SpireTool {

    //Spire解决图表无法生成问题
    public static void main(String[] args) throws Exception {
        String pptxFilePath = "F:\\PVZ\\1722933062548.PPTX";

        //初始化Presentation类的实例
        Presentation presentation = new Presentation();
        //加载PowerPoint文档
        presentation.loadFromFile(pptxFilePath);
        System.out.println("success");
        SlideCollection slides = presentation.getSlides();
        System.out.println("slides size:" + slides.getCount());
        if(slides.getCount() > 0){
            //遍历PowerPoint文档中的所有幻灯片
            for(int i = 0; i < slides.getCount(); i++)
            {
                ISlide slide = presentation.getSlides().get(i);
                //将每张幻灯片另存为PNG图像
                BufferedImage image = slide.saveAsImage();
                String fileName = String.format("F:\\PVZ\\1722933062548\\" + "ToImage-%1$s.png", i);
                ImageIO.write(image, "PNG",new File(fileName));
            }
        }
    }

}
