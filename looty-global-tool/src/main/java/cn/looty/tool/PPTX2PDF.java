package cn.looty.tool;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.fdf.FDFDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.sl.usermodel.Slide;
import org.apache.poi.sl.usermodel.SlideShow;
import org.apache.poi.sl.usermodel.SlideShowFactory;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;

/**
 * @Filename: PPTX2PDF
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-08-06 13:21
 */
public class PPTX2PDF {

    public static void main(String[] args) throws IOException {
        File file = new File("F:\\PVZ\\858585.PPTX");
        try {
            PDDocument doc = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(doc);

            int numberOfPages = doc.getNumberOfPages();

            for(int i = 0; i < numberOfPages; i++){
                BufferedImage image = renderer.renderImageWithDPI(i, 95);
                OutputStream out = Files.newOutputStream(new File("D:\\ruining_chart\\" + (i + 1) + ".png").toPath());
                ImageIO.write(image, "png", out);
                out.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
