package com.fintech.insurance.commons.utils;

import com.fintech.insurance.commons.exceptions.FInsuranceIOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/22 20:02
 */
public class ImageUtils {

    private static final Logger logger = LoggerFactory.getLogger(ImageUtils.class);

    // 水印透明度
    private static float PDF_IMAGE_ALPHA = 0.5f;
    // 水印横向位置
    private static int PDF_IMAGE_POSITION_WIDTH = 450;
    // 水印纵向位置
    private static int PDF_IMAGE_POSITION_HEIGHT = 1000;
    // 水印文字字体
    private static Font PDF_IMAGE_FONT = new Font("宋体", Font.BOLD, 72);
    // 水印文字颜色
    private static Color PDF_IMAGE_COLOR = Color.gray;
    // 临时图片文件路径
    private static String TEMP_PIC_DIR = "insurance/pic";

    public static byte[] image2byte(String path) {
        byte[] data = null;
        FileImageInputStream input = null;
        try {
            input = new FileImageInputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        } catch (FileNotFoundException ex1) {
            throw new FInsuranceIOException("Not found file:" + path);
        } catch (IOException ex1) {
            throw new FInsuranceIOException("IOException:" + path);
        }
        return data;
    }

    /**
     * 从InputStream读取字节数组
     * 结束时会关闭InputStream
     *
     * @param in
     * @return 字节数组
     */
    public static byte[] readBytes(InputStream in) {
        if (null == in)
            throw new NullPointerException("the argument 'in' must not be null");
        try {
            int buffSize = Math.max(in.available(), 1024 * 8);
            byte[] temp = new byte[buffSize];
            ByteArrayOutputStream out = new ByteArrayOutputStream(buffSize);
            int size = 0;
            while ((size = in.read(temp)) != -1) {
                out.write(temp, 0, size);
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new FInsuranceIOException("IOException: " + in);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                logger.error("Fail to close input stream due to exception found", e);
            }
        }
    }

    //将图片转换成BASE64字符串
    public static String getImageContentString(String path) {
        return org.springframework.util.Base64Utils.encodeToString(ImageUtils.image2byte(path));
    }

    //将图片转换成BASE64字符串
    public static String getImageContentString(File imageFile) {
        return org.springframework.util.Base64Utils.encodeToString(ImageUtils.image2byte(imageFile.getPath()));
    }

    //将图片转换成BASE64字符串
    public static String getImageContentString(InputStream in) {
        return org.springframework.util.Base64Utils.encodeToString(ImageUtils.readBytes(in));
    }

    /**
     * 生成pdf的缩略图
     *
     * @param zoom       缩略图显示倍数，1表示不缩放，0.5表示缩小到50%
     * @param inputFile  需要生成缩略图的PDF文件的完整路径
     * @param outputDir 生成缩略图的放置路径
     */
    public static List<String> pdfPageToIamge(float zoom, String inputFile, String outputDir) {
        List<String> list = null;
        Document document = null;
        try {
            list = new ArrayList<>();
            document = new Document();
            document.setFile(inputFile);
            float rotation = 0;
            int maxPages = document.getPageTree().getNumberOfPages();
            for (int i = 0; i < maxPages; i++) {
                BufferedImage bfimage = (BufferedImage) document.getPageImage(i, GraphicsRenderingHints.PRINT, Page.BOUNDARY_ARTBOX, rotation, zoom);

                //bfimage = setGraphics(bfimage);

                RenderedImage rendImage = bfimage;
                ImageIO.write(rendImage, "jpg", new File(outputDir + i + ".jpg"));
                bfimage.flush();
                list.add(outputDir + i + ".jpg");
            }
        } catch (Exception e) {
            logger.error("Fail to convert pdf page to images due to exception found", e);
        }

        if (document != null) {
            document.dispose();
        }
        return list;
    }

    /**
     *  生成pdf文件的缩略图， 全部页在一张图上
     *
     * @param zoom 缩略图显示倍数，1表示不缩放，0.5表示缩小到50%, 值可以大于1，越大生成的图片越清晰，也占内存更大
     * @param isMergePageHorizontal 缩略图是否从水平方向合并PDF页图， false 表示从垂直方向合并
     * @param pdfFile PDF 文件路径
     * @param outputImageFile 缩略图的输出路径
     * @return 缩略图的二进制数组
     */
    public static void pdfFileToIamge(float zoom, boolean isMergePageHorizontal, String pdfFile, String outputImageFile) {
        BufferedImage fileImage = ImageUtils.convertPDFFileToImage(zoom, isMergePageHorizontal, pdfFile);
        RenderedImage rendImage = fileImage;
        try {
            ImageIO.write(rendImage, "jpg", new File(outputImageFile));
        } catch (IOException e) {
            throw new FInsuranceIOException(e.getMessage());
        }
        fileImage.flush();
    }

    /**
     *  生成pdf文件的缩略图， 全部页在一张图上
     *
     * @param zoom 缩略图显示倍数，1表示不缩放，0.5表示缩小到50%, 值可以大于1，越大生成的图片越清晰，也占内存更大
     * @param isMergePageHorizontal 缩略图是否从水平方向合并PDF页图， false 表示从垂直方向合并
     * @param pdfFile PDF 文件路径
     * @return 缩略图的二进制数组
     */
    public static byte[] pdfFileToIamge(float zoom, boolean isMergePageHorizontal, String pdfFile) {
        BufferedImage fileImage = ImageUtils.convertPDFFileToImage(zoom, isMergePageHorizontal, pdfFile);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            ImageIO.write(fileImage, "jpg", output);
            fileImage.flush();
            return output.toByteArray();
        } catch (IOException e) {
            throw new FInsuranceIOException(e.getMessage());
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                logger.error("Fail to close output stream due to exception found", e);
            }
        }
    }

    /**
     *  使用工具Pdfbox工具 生成pdf文件的缩略图， 全部页在一张图上
     * @param isMergePageHorizontal 缩略图是否从水平方向合并PDF页图， false 表示从垂直方向合并
     * @param pdfFile PDF 文件路径
     * @param outputImageFile 缩略图的输出路径
     * @return 缩略图的二进制数组
     */
    public static void pdfFileToIamgePdfbox(boolean isMergePageHorizontal, String pdfFile, String outputImageFile, String waterMarkText) throws IOException {
        BufferedImage fileImage = ImageUtils.convertPDFFileToImageByPdfbox(isMergePageHorizontal, pdfFile, waterMarkText);
        RenderedImage rendImage = fileImage;
        try {
            ImageIO.write(rendImage, "jpg", new File(outputImageFile));
        } catch (IOException e) {
            throw new FInsuranceIOException(e.getMessage());
        }
        fileImage.flush();
    }

    public static String pdfFileToIamgePdfbox(boolean isMergePageHorizontal, String pdfFile, String waterMarkText) {
        String systemTempDir = System.getProperty("java.io.tmpdir");
        logger.info("Current temporally dir is: {}", systemTempDir);
        File tempImage = FileUtils.getFile(systemTempDir, TEMP_PIC_DIR, String.valueOf(Calendar.getInstance().getTime().getTime() + ".jpg"));
        BufferedOutputStream output = null;
        try {
            output = new BufferedOutputStream(FileUtils.openOutputStream(tempImage));
            BufferedImage fileImage = ImageUtils.convertPDFFileToImageByPdfbox(isMergePageHorizontal, pdfFile, waterMarkText);
            ImageIO.write(fileImage, "jpg", output);
            fileImage.flush();

            return tempImage.getAbsolutePath();
        } catch (IOException e) {
            throw new FInsuranceIOException(e.getMessage());
        } finally {
            IOUtils.closeQuietly(output);
        }
    }

    private static BufferedImage convertPDFFileToImage(float zoom, boolean isMergePageHorizontal, String pdfFile) {
        Document document = null;
        try {
            document = new Document();

            try {
                URL url = new URL(pdfFile);
                // is network file
                document.setUrl(url);
            } catch (MalformedURLException e) {
                // is file
                document.setFile(pdfFile);
            }

            float rotation = 0;
            int maxPages = document.getPageTree().getNumberOfPages();

            BufferedImage[] pageImages = new BufferedImage[maxPages];
            for (int i = 0; i < maxPages; i++) {
                BufferedImage pageImage = (BufferedImage) document.getPageImage(i, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, rotation, zoom);
                //pageImage = setGraphics(pageImage); //加水印
                pageImage = markImageByText(pageImage, "诺米金服", 20);
                pageImage.flush();
                pageImages[i] = pageImage;
            }
            return ImageUtils.mergeImage(isMergePageHorizontal, pageImages);
        } catch (Exception e) {
            throw new FInsuranceIOException(e.getMessage());
        } finally {
            if (document != null) {
                document.dispose();
            }
        }
    }

    private static BufferedImage convertPDFFileToImageByPdfbox2(boolean isMergePageHorizontal, String pdfFileUrl, String waterMarkText) throws IOException {
        PDDocument document = new PDDocument();


        try {
            URL url = new URL(pdfFileUrl);
            // is network file
            byte[] fileData = ToolUtils.downloadFile(pdfFileUrl);
            document = PDDocument.load(fileData);
        } catch (MalformedURLException e) {
            // is file
            document.load(new File(pdfFileUrl));
        }

        try {
            int size = document.getNumberOfPages();

            BufferedImage[] pageImages = new BufferedImage[size];
            for(int i=0 ; i < size; i++){
                BufferedImage image = new PDFRenderer(document).renderImageWithDPI(i,130,ImageType.RGB);
                image = markImageByText(image, waterMarkText, -20);
                image.flush();
                pageImages[i] = image;
            }
            return ImageUtils.mergeImage(isMergePageHorizontal, pageImages);
        } finally {
             if (null != document) {
                 try {
                     document.close();
                 } catch (IOException e) {
                     logger.error("Fail to close pdf document due to exception found", e);
                 }
             }
        }
    }

    private static BufferedImage convertPDFFileToImageByPdfbox(boolean isMergePageHorizontal, String pdfFileUrl, String waterMarkText) throws IOException {
        PDDocument document = new PDDocument();
        File tempFile = null;
        try {
            URL url = new URL(pdfFileUrl);
            // is network file
            tempFile = ToolUtils.downloadFileToTempUrl(pdfFileUrl);
            document = PDDocument.load(tempFile);
        } catch (MalformedURLException e) {
            // is file
            document.load(new File(pdfFileUrl));
        }

        try {
            int size = document.getNumberOfPages();

            BufferedImage[] pageImages = new BufferedImage[size];
            for(int i=0 ; i < size; i++){
                BufferedImage image = new PDFRenderer(document).renderImageWithDPI(i,130,ImageType.RGB);
                image = markImageByText(image, waterMarkText, -20);
                image.flush();
                pageImages[i] = image;
            }
            return ImageUtils.mergeImage(isMergePageHorizontal, pageImages);
        } finally {
            IOUtils.closeQuietly(document);
            FileUtils.deleteQuietly(tempFile);
        }
    }

    /*private static BufferedImage setGraphics(BufferedImage bfimage) {
        Graphics2D g = bfimage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        // 5、设置水印文字颜色
        g.setColor(PDF_IMAGE_COLOR);
        // 6、设置水印文字Font
        g.setFont(PDF_IMAGE_FONT);
        // 7、设置水印文字透明度
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, PDF_IMAGE_ALPHA));
        //设置旋转
        g.rotate(-Math.PI / 6);
        g.drawString("薪乐宝金融科技", 0, (bfimage.getHeight() / 2));
        // 9、释放资源
        g.dispose();
        return bfimage;
    }*/

    /**
     * 给图片添加水印文字、可设置水印文字的旋转角度
     * @param bfimage 指定图片
     * @param logoText 水印文字
     * @param degree
     * @return
     */
    public static BufferedImage markImageByText(BufferedImage bfimage, String logoText, Integer degree) {
        // 2、得到画笔对象
        Graphics2D g = bfimage.createGraphics();
        // 3、设置对线段的锯齿状边缘处理
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(
                bfimage.getScaledInstance(bfimage.getWidth(null),
                        bfimage.getHeight(null), Image.SCALE_SMOOTH), 0, 0,
                null);
        // 4、设置水印旋转
        if (null != degree) {
            g.rotate(Math.toRadians(degree),
                    (double) bfimage.getWidth() / 2,
                    (double) bfimage.getHeight() / 2);
        }
        // 5、设置水印文字颜色
        g.setColor(PDF_IMAGE_COLOR);
        // 6、设置水印文字Font
        g.setFont(PDF_IMAGE_FONT);
        // 7、设置水印文字透明度
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                PDF_IMAGE_ALPHA));
        // 8、第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y)
        g.drawString(logoText, PDF_IMAGE_POSITION_WIDTH, PDF_IMAGE_POSITION_HEIGHT);
        // 9、释放资源
        g.dispose();
        return bfimage;
    }

    /**
     * @param fileUrl 文件绝对路径或相对路径
     * @return 读取到的缓存图像
     * @throws IOException 路径错误或者不存在该文件时抛出IO异常
     */
    private static BufferedImage getBufferedImage(String fileUrl) throws IOException {
        File f = new File(fileUrl);
        return ImageIO.read(f);
    }

    /**
     * @param savedImg 待保存的图像
     * @param saveDir  保存的目录
     * @param fileName 保存的文件名，必须带后缀，比如 "beauty.jpg"
     * @param format   文件格式：jpg、png或者bmp
     * @return
     */
    public static boolean saveImage(BufferedImage savedImg, String saveDir, String fileName, String format) {
        boolean flag = false;

        // 先检查保存的图片格式是否正确
        String[] legalFormats = {"jpg", "JPG", "png", "PNG", "bmp", "BMP"};
        int i = 0;
        for (i = 0; i < legalFormats.length; i++) {
            if (format.equals(legalFormats[i])) {
                break;
            }
        }
        if (i == legalFormats.length) { // 图片格式不支持
            logger.info("不是保存所支持的图片格式!");
            return false;
        }

        // 再检查文件后缀和保存的格式是否一致
        String postfix = fileName.substring(fileName.lastIndexOf('.') + 1);
        if (!postfix.equalsIgnoreCase(format)) {
            logger.info("待保存文件后缀和保存的格式不一致!");
            return false;
        }

        String fileUrl = saveDir + fileName;
        File file = new File(fileUrl);
        try {
            flag = ImageIO.write(savedImg, format, file);
        } catch (IOException e) {
            logger.error("Fail to write image to file due to exception found", e);
        }

        return flag;
    }

    /**
     * 待合并的两张图必须满足这样的前提，如果水平方向合并，则高度必须相等；如果是垂直方向合并，宽度必须相等。
     * mergeImage方法不做判断，自己判断。
     *
     * @param img1         待合并的第一张图
     * @param img2         带合并的第二张图
     * @param isHorizontal 为true时表示水平方向合并，为false时表示垂直方向合并
     * @return 返回合并后的BufferedImage对象
     */
    public static BufferedImage mergeImage(boolean isHorizontal, BufferedImage img1, BufferedImage img2) {
        int w1 = img1.getWidth();
        int h1 = img1.getHeight();
        int w2 = img2.getWidth();
        int h2 = img2.getHeight();

        // 从图片中读取RGB
        int[] ImageArrayOne = new int[w1 * h1];
        ImageArrayOne = img1.getRGB(0, 0, w1, h1, ImageArrayOne, 0, w1); // 逐行扫描图像中各个像素的RGB到数组中
        int[] ImageArrayTwo = new int[w2 * h2];
        ImageArrayTwo = img2.getRGB(0, 0, w2, h2, ImageArrayTwo, 0, w2);

        // 生成新图片
        BufferedImage DestImage = null;
        if (isHorizontal) { // 水平方向合并
            DestImage = new BufferedImage(w1 + w2, h1, BufferedImage.TYPE_INT_RGB);
            DestImage.setRGB(0, 0, w1, h1, ImageArrayOne, 0, w1); // 设置上半部分或左半部分的RGB
            DestImage.setRGB(w1, 0, w2, h2, ImageArrayTwo, 0, w2);
        } else { // 垂直方向合并
            DestImage = new BufferedImage(w1, h1 + h2, BufferedImage.TYPE_INT_RGB);
            DestImage.setRGB(0, 0, w1, h1, ImageArrayOne, 0, w1); // 设置上半部分或左半部分的RGB
            DestImage.setRGB(0, h1, w2, h2, ImageArrayTwo, 0, w2); // 设置下半部分的RGB
        }

        return DestImage;

    }

    /**
     * 合并任数量的图片成一张图片
     *
     * @param isHorizontal true代表水平合并，fasle代表垂直合并
     * @param imgs         欲合并的图片数组
     * @return
     */
    public static BufferedImage mergeImage(boolean isHorizontal, BufferedImage... imgs) {
        // 生成新图片
        BufferedImage DestImage = null;

        // 计算新图片的长和高
        int allw = 0, allh = 0, allwMax = 0, allhMax = 0;
        for (BufferedImage img : imgs) {
            allw += img.getWidth();
            allh += img.getHeight();
            if (img.getWidth() > allwMax) {
                allwMax = img.getWidth();
            }
            if (img.getHeight() > allhMax) {
                allhMax = img.getHeight();
            }
        }
        // 创建新图片
        if (isHorizontal) {
            DestImage = new BufferedImage(allw, allhMax, BufferedImage.TYPE_INT_RGB);
        } else {
            DestImage = new BufferedImage(allwMax, allh, BufferedImage.TYPE_INT_RGB);
        }

        // 合并所有子图片到新图片
        int wx = 0, wy = 0;
        for (int i = 0; i < imgs.length; i++) {
            BufferedImage img = imgs[i];
            int w1 = img.getWidth();
            int h1 = img.getHeight();
            // 从图片中读取RGB
            int[] ImageArrayOne = new int[w1 * h1];
            ImageArrayOne = img.getRGB(0, 0, w1, h1, ImageArrayOne, 0, w1); // 逐行扫描图像中各个像素的RGB到数组中
            if (isHorizontal) { // 水平方向合并
                DestImage.setRGB(wx, 0, w1, h1, ImageArrayOne, 0, w1); // 设置上半部分或左半部分的RGB
            } else { // 垂直方向合并
                DestImage.setRGB(0, wy, w1, h1, ImageArrayOne, 0, w1); // 设置上半部分或左半部分的RGB
            }
            wx += w1;
            wy += h1;
        }
        return DestImage;
    }

    public static void main(String[] args) throws IOException {
        String pdfFile = "C:\\Users\\Administrator\\Desktop\\test\\aaa.pdf";
        String path = ImageUtils.pdfFileToIamgePdfbox(false,  "http://ozhoilwen.bkt.clouddn.com/pdf_QN201801192240310014?e=1516584901&token=49-Ruxyz4EdBenYuZ91u6cPjPpHGfvVwN6t2HnPP:wkm557MV2MamiMX2bg6haROjN5w=", "C:\\Users\\Administrator\\Desktop\\test\\aaa.jpg");
        System.out.println(path);
    }

}
