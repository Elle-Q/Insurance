package com.fintech.insurance.micro.thirdparty.service.vercode;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Description: (some words)
 * @Author: Yong Li
 * @Date: 2017/11/13 21:05
 */
public class ImageVercodeGenerator {

    /*static char[] chars = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};*/

    /**
     * 生成指定长度的Base64编码的验证码图片
     *
     * @return 产生指定长度的复杂/简单验证码
     * @throws IOException
     */
    public static String gen(String vercode, boolean isSimple) throws IOException {
        BufferedImage image = null;
        if (isSimple) {
            image = genSimpleVercodeImg(vercode);
        } else {
            image = genComplexVercodeImg(vercode);
        }
        // 创建字符流
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        // 写入字符流
        ImageIO.write(image, "jpeg", bs);
        // 转码成字符串
        String imgsrc = org.springframework.util.Base64Utils.encodeToString(bs.toByteArray());
        return imgsrc;
    }

    /**
     * 生成指定长度的Base64编码的验证码图片
     *
     * @return 产生指定长度的复杂/简单验证码
     * @throws IOException
     */
    public static void genFile(String vercode, boolean isSimple) throws IOException {
        BufferedImage image = null;
        if (isSimple) {
            image = genSimpleVercodeImg(vercode);
        } else {
            image = genComplexVercodeImg(vercode);
        }
        // 创建字符流
        FileOutputStream bs = new FileOutputStream("F:\\temp\\test.jpeg");
        // 写入字符流
        ImageIO.write(image, "jpeg", bs);

        bs.close();
    }

    /**
     * @param codeLength 验证码的长度
     * @return 验证码字符串
     */
    public static String generateVercode(int codeLength) {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < codeLength; i++) {
            s.append(getRandomChar());
        }
        return s.toString();
    }

    public static void main(String[] args) throws IOException {
        String vercode = generateVercode(4);
        //System.out.println("vercode = " + vercode);
        //System.out.println(ImageVercodeGenerator.gen(vercode, false));
        //ImageVercodeGenerator.genFile("147M", true);
        //System.out.println(ImageVercodeGenerator.gen(vercode, false));
    }

    /**
     * 为一个验证码生成一个图片
     * <p>
     * 特性：
     * - 颜色随机
     * - 上下位置随机
     * - 左右位置随机，但字符之间不会重叠
     * - 左右随机旋转一个角度
     * - 避免字符出界
     * - 随机颜色的小字符做背景干扰
     * - 根据字符大小自动调整图片大小、自动计算干扰字符的个数
     *
     * @param vercode 验证码字符长度
     * @return 产生指定长度的复杂验证码
     */
    public static BufferedImage genComplexVercodeImg(String vercode) {
        ThreadLocalRandom r = ThreadLocalRandom.current();
        int fontSize = 80; //code的字体大小
        int fontMargin = fontSize / 4; //字符间隔
        int width = (fontSize + fontMargin) * vercode.length() + fontMargin; //图片长度
        int height = (int) (fontSize * 1.2); //图片高度，根据字体大小自动调整；调整这个系数可以调整字体占图片的比例
        int avgWidth = width / vercode.length(); //字符平均占位宽度
        int maxDegree = 26; //最大旋转度数

        //背景颜色
        Color bkColor = Color.WHITE;
        //验证码的颜色
        Color[] catchaColor = {Color.MAGENTA, Color.BLACK, Color.BLUE, Color.CYAN, Color.GREEN, Color.ORANGE, Color.PINK};

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        //填充底色为灰白
        g.setColor(bkColor);
        g.fillRect(0, 0, width, height);

        //画边框
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, width - 1, height - 1);

        //画干扰字母、数字
        int dSize = fontSize / 3; //调整分母大小以调整干扰字符大小
        Font font = new Font("Fixedsys", Font.PLAIN, dSize);
        g.setFont(font);
        int dNumber = width * height / dSize / dSize;//根据面积计算干扰字母的个数
        for (int i = 0; i < dNumber; i++) {
            //char d_code = chars[r.nextInt(chars.length)];
            char d_code = getRandomChar();
            g.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
            g.drawString(String.valueOf(d_code), r.nextInt(width), r.nextInt(height));
        }

        //开始画验证码：

        // 创建字体
        font = new Font(Font.MONOSPACED, Font.ITALIC | Font.BOLD, fontSize);
        // 设置字体
        g.setFont(font);

        for (int i = 0; i < vercode.length(); i++) {
            char c = vercode.charAt(i);
            g.setColor(catchaColor[r.nextInt(catchaColor.length)]);//随机选取一种颜色

            //随机旋转一个角度[-maxDegre, maxDegree]
            int degree = r.nextInt(-maxDegree, maxDegree + 1);

            //偏移系数，和旋转角度成反比，以避免字符在图片中越出边框
            double offsetFactor = 1 - (Math.abs(degree) / (maxDegree + 1.0));//加上1，避免出现结果为0

            g.rotate(degree * Math.PI / 180); //旋转一个角度
            int x = (int) (fontMargin + r.nextInt(avgWidth - fontSize) * offsetFactor); //横向偏移的距离
            int y = (int) (fontSize + r.nextInt(height - fontSize) * offsetFactor); //上下偏移的距离

            g.drawString(String.valueOf(c), x, y); //x,y是字符的左下角，偏离原点的距离！！！

            g.rotate(-degree * Math.PI / 180); //画完一个字符之后，旋转回原来的角度
            g.translate(avgWidth, 0);//移动到下一个画画的原点
            //System.out.println(c+": x="+x+" y="+y+" degree="+degree+" offset="+offsetFactor);

            //X、Y坐标在合适的范围内随机，不旋转：
            //g.drawString(String.valueOf(c), width/count*i+r.nextInt(width/count-fontSize), fontSize+r.nextInt(height-fontSize));
        }

        g.dispose();

        return image;
    }

    /**
     * @param vercode 验证码
     * @return 产生指定长度的简单验证码
     */
    public static BufferedImage genSimpleVercodeImg(String vercode) {
        //在内存中创建图象
        int width = 80, height = 35;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //获取图形上下文
        Graphics g = image.getGraphics();
        //生成随机类
        Random random = new Random();
        //设定背景色
        g.setColor(getRandColor(220, 250));
        g.fillRect(0, 0, width, height);
        //设定字体
        g.setFont(new Font("Helvetica Neue", Font.PLAIN, 30));
        //画边框
        //g.drawRect(0,0,width-1,height-1);
        g.draw3DRect(0, 0, width - 1, height - 1, true);
        //随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }
        // 取随机产生的认证码(4位数字)
        String sRand = "";
        for (int i = 0; i < vercode.length(); i++) {
            char rand = vercode.charAt(i);
            sRand += rand;
            // 将认证码显示到图象中
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            //调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
            g.drawString(String.valueOf(rand), 17 * i + 4, 29);
        }
        g.drawOval(0, 18, 80, 17);
        return image;
    }

    private static char getRandomChar() {
        //String s = "012345678901234567890123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ012345678901234567890123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String s = "123456789123456789123456789ABCDEFGHJKLMNPQRSTUVWXYZ123456789123456789123456789ABCDEFGHJKLMNPQRSTUVWXYZ";
        return s.charAt(new Random().nextInt(s.length()));
    }

    /**
     * 生成随机颜色
     */
    private static Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}
