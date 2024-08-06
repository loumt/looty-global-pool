package cn.looty.tool;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.poi.sl.usermodel.SlideShow;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Filename: PptxToImageConverter
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: LouMT@orz.com
 * @Date: 2024-08-06 17:32
 */
public class PptxToImageConverter {
    public static void main(String[] args) {
        try {
            String inputFile = "D:\\ruining_chart\\1722933062548.pptx";
            String outputFilePrefix = "slide";
            String outputFormat = "png";
            convertPptxToImage(inputFile, outputFilePrefix, outputFormat);
        } catch (IOException | TranscoderException e) {
            e.printStackTrace();
        }
    }
    private static void convertPptxToImage(String inputFile, String outputFilePrefix, String outputFormat)
            throws IOException, TranscoderException {
        XMLSlideShow xmlSlideShow = new XMLSlideShow(new FileInputStream(inputFile));
        Dimension pgsize = xmlSlideShow.getPageSize();
        int slideNumber = 0;
        for (XSLFSlide slide : xmlSlideShow.getSlides()) {
            slideNumber++;
            String outputFile = outputFilePrefix + slideNumber + "." + outputFormat;
            FileOutputStream out = new FileOutputStream(new File(outputFile));
            DOMImplementation domImpl = SVGDOMImplementation.getDOMImplementation();
            Document document = domImpl.createDocument(SVGDOMImplementation.SVG_NAMESPACE_URI, "svg", null);
            SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
            slide.draw(svgGenerator);
            svgGenerator.setSVGCanvasSize(pgsize);

            TranscoderInput input = new TranscoderInput(svgGenerator.getDOMFactory());
            TranscoderOutput output = new TranscoderOutput(out);
            PNGTranscoder transcoder = new PNGTranscoder();
            transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, (float) pgsize.getWidth());
            transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, (float) pgsize.getHeight());
            transcoder.transcode(input, output);
            out.flush();
            out.close();
        }
    }
}
