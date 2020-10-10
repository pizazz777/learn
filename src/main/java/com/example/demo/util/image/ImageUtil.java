package com.example.demo.util.image;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Administrator
 * @date 2020-07-24 16:47
 * @description: 图片工具类
 */
public class ImageUtil {

    private ImageUtil() {
    }

    /**
     * 生成固定大小的图片
     *
     * @param srcPath  目标图片路径
     * @param distPath 生成图片路径
     * @param width    宽
     * @param height   高
     * @throws IOException e
     */
    public static void generateFixedSize(String srcPath, String distPath, int width, int height) throws IOException {
        Thumbnails.of(srcPath)
                .size(width, height)
                .toFile(distPath);
    }

    /**
     * 按照比例缩放
     *
     * @param srcPath  目标图片路径
     * @param distPath 生成图片路径
     * @param scale    比例
     * @throws IOException e
     */
    public static void scale(String srcPath, String distPath, double scale) throws IOException {
        Thumbnails.of(srcPath)
                .scale(scale)
                .toFile(distPath);
    }

    /**
     * 旋转图像
     *
     * @param srcPath  目标图片路径
     * @param distPath 生成图片路径
     * @param angle    旋转角度 正数:顺时针 负数:逆时针
     * @throws IOException e
     */
    public static void rotate(String srcPath, String distPath, double angle) throws IOException {
        Thumbnails.of(srcPath)
                .scale(1)
                .rotate(angle)
                .toFile(distPath);
    }

    /**
     * 添加水印图片
     *
     * @param srcPath       目标地址
     * @param distPath      生成地址
     * @param watermarkPath 水印地址
     * @param width         宽
     * @param height        高
     * @param alpha         透明度
     * @param quality       质量 0.0-1.0
     * @param position      水印的方向
     * @throws IOException e
     */
    public static void watermark(String srcPath, String distPath, String watermarkPath, int width,
                                 int height, float alpha, float quality, Positions position) throws IOException {
        Thumbnails.of(srcPath)
                .size(width, height)
                .watermark(position, ImageIO.read(new File(watermarkPath)), alpha)
                .outputQuality(quality)
                .toFile(distPath);
    }


    /**
     * 转换图片类型
     *
     * @param srcPath  目标地址
     * @param distPath 生成地址
     * @param type     图片类型
     * @throws IOException e
     */
    public static void convert(String srcPath, String distPath, ImageTypeEnum type) throws IOException {
        Thumbnails.of(srcPath)
                .scale(1)
                .outputFormat(type.name().toLowerCase())
                .toFile(distPath);
    }


    /**
     * 裁剪
     *
     * @param srcPath   目标地址
     * @param distPath  生成地址
     * @param positions 位置
     * @param width     在该位置中的宽
     * @param height    在该位置中的宽
     * @throws IOException e
     */
    public static void cutImage(String srcPath, String distPath, Positions positions, int width, int height) throws IOException {
        Thumbnails.of(srcPath)
                .sourceRegion(positions, width, height)
                .scale(1)
                .toFile(distPath);
    }

    /**
     * 指定坐标裁剪
     *
     * @param srcPath  目标地址
     * @param distPath 生成地址
     * @param x        x轴
     * @param y        y轴
     * @param width    宽
     * @param height   高
     * @throws IOException e
     */
    public static void cutImage(String srcPath, String distPath, int x, int y, int width, int height) throws IOException {
        Thumbnails.of(srcPath)
                .sourceRegion(x, y, width, height)
                .scale(1)
                .toFile(distPath);
    }

    /**
     * 把文字转化为一张背景透明的图片
     *
     * @param content       文字的内容
     * @param fontType      字体，例如宋体
     * @param fontSize      字体大小
     * @param colorStr      字体颜色(16进制,不带#号)
     * @param outfile       图片的路径
     * @param imageTypeEnum 图片的类型
     * @throws IOException e
     */
    public static void convertFontToImage(String content, String fontType, int fontSize, String colorStr, String outfile, ImageTypeEnum imageTypeEnum) throws IOException {
        Font font = new Font(fontType, Font.BOLD, fontSize);
        //获取font的样式应用在str上的整个矩形
        Rectangle2D r = font.getStringBounds(content, new FontRenderContext(AffineTransform.getScaleInstance(1, 1), false, false));
        //获取单个字符的高度
        int unitHeight = (int) Math.floor(r.getHeight());
        //获取整个str用了font样式的宽度这里用四舍五入后+1保证宽度绝对能容纳这个字符串作为图片的宽度
        int width = (int) Math.round(r.getWidth()) + 1;
        //把单个字符的高度+3保证高度绝对能容纳字符串作为图片的高度
        int height = unitHeight + 3;
        //创建图片
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        image = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g2d.dispose();
        g2d = image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(1));
        //在换成所需要的字体颜色
        g2d.setColor(new Color(Integer.parseInt(colorStr, 16)));
        g2d.setFont(font);
        g2d.drawString(content, 0, font.getSize());

        File file = new File(outfile);
        //输出图片
        ImageIO.write(image, imageTypeEnum.name().toLowerCase(), file);
    }


}
