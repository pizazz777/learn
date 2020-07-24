package com.example.demo.util.qrcode;

import com.google.common.collect.Maps;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author Administrator
 * @date 2020-07-24 17:17
 * @description: 二维码工具类
 */
public class QrCodeUtil {

    private QrCodeUtil() {
    }

    /**
     * 生成二维码图片 可通过{@link javax.imageio.ImageIO#write(RenderedImage, String, ImageOutputStream)}写到文件/输出流/输入流中
     *
     * @param qrCodeText      二维码文字内容
     * @param size            图片大小
     * @param errorCorrection 容错率 {@link ErrorCorrectionLevel#M}
     * @param colorModel      颜色模型,例如RGB {@link BufferedImage#TYPE_INT_RGB}
     * @param onColor         图案颜色
     * @param offColor        图案底色
     * @return 二维码图片对象
     * @throws WriterException e
     */
    public static BufferedImage generateQrCodeImage(String qrCodeText, int size, ErrorCorrectionLevel errorCorrection, int colorModel, Color onColor, Color offColor) throws WriterException {
        // 校验二维码内容
        verifyText(qrCodeText);
        // 二维码属性EncodeHintType
        Map<EncodeHintType, Serializable> hints = Maps.newHashMap();
        // 设置字符编码
        hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
        // 设置二维码容错率
        hints.put(EncodeHintType.ERROR_CORRECTION, errorCorrection);
        // 设置二维码边的空度,非负数
        hints.put(EncodeHintType.MARGIN, 0);
        // 生成二维码矩阵
        BitMatrix bitMatrix = new MultiFormatWriter().encode(qrCodeText, BarcodeFormat.QR_CODE, size, size, hints);
        // 将二维码转换成 BufferedImage
        return toBufferedImage(bitMatrix, colorModel, onColor, offColor);
    }

    /**
     * 生成二维码图片,包括Logo
     *
     * @param logoFile        logo文件
     * @param text            二维码文字内容
     * @param size            图片大小
     * @param errorCorrection 容错率 {@link ErrorCorrectionLevel#M}
     * @param colorModel      颜色模型,例如RGB {@link BufferedImage#TYPE_INT_RGB}
     * @param onColor         图案颜色
     * @param offColor        图案底色
     * @return 二维码图片对象
     * @throws IOException     e
     * @throws WriterException e
     */
    public static BufferedImage generateQrCodeImage(File logoFile, String text, int size, ErrorCorrectionLevel errorCorrection,
                                                    int colorModel, Color onColor, Color offColor) throws IOException, WriterException {
        // 不含图片的二维码
        BufferedImage encodeImage = generateQrCodeImage(text, size, errorCorrection, colorModel, onColor, offColor);
        try (FileInputStream inputStream = new FileInputStream(logoFile)) {
            // 获取画笔
            Graphics2D g2 = encodeImage.createGraphics();
            int matrixWidth = encodeImage.getWidth();
            int matrixHeight = encodeImage.getHeight();
            BufferedImage logo = ImageIO.read(inputStream);
            // 开始绘制图片
            g2.drawImage(logo, matrixWidth / 5 * 2, matrixHeight / 5 * 2, matrixWidth / 5, matrixHeight / 5, null);
            BasicStroke stroke = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            // 设置笔画对象
            g2.setStroke(stroke);
            // 指定弧度的圆角矩形
            RoundRectangle2D.Float round = new RoundRectangle2D.Float(matrixWidth / 5 * 2, matrixHeight / 5 * 2, matrixWidth / 5, matrixHeight / 5, 20, 20);
            g2.setColor(Color.white);
            // 绘制圆弧矩形
            g2.draw(round);
            // 设置logo 有一道灰色边框
            BasicStroke stroke2 = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            // 设置笔画对象
            g2.setStroke(stroke2);
            RoundRectangle2D.Float round2 = new RoundRectangle2D.Float(matrixWidth / 5 * 2 + 2, matrixHeight / 5 * 2 + 2, matrixWidth / 5 - 4, matrixHeight / 5 - 4, 20, 20);
            g2.setColor(new Color(128, 128, 128));
            // 绘制圆弧矩形*/
            g2.draw(round2);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            encodeImage.flush();
            g2.dispose();
        }
        return encodeImage;
    }


    /**
     * 生成二维码图片
     *
     * @param matrix     二维码矩阵
     * @param colorModel 颜色模型,例如RGB {@link BufferedImage#TYPE_INT_RGB}
     * @param onColor    图案颜色
     * @param offColor   图案底色
     * @return 二维码图片对象
     */
    private static BufferedImage toBufferedImage(BitMatrix matrix, int colorModel, Color onColor, Color offColor) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, colorModel);
        int[] rowPixels = new int[width];
        BitArray row = new BitArray(width);
        for (int y = 0; y < height; y++) {
            row = matrix.getRow(y, row);
            for (int x = 0; x < width; x++) {
                rowPixels[x] = row.get(x) ? onColor.getRGB() : offColor.getRGB();
            }
            image.setRGB(0, y, width, 1, rowPixels, 0, width);
        }
        return image;
    }

    private static void verifyText(String qrCodeText) throws WriterException {
        if (StringUtils.isBlank(qrCodeText)) {
            throw new WriterException("二维码内容不能为空");
        }
    }
}
