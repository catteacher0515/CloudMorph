package com.pingyu.cloudmorph.util;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * 本地封面图生成器。
 * <p>
 * 第八节教程里是 Selenium + 压缩 + COS，这里先提供可运行的本地占位实现，
 * 之后只需要把上传逻辑替换成 COS 即可。
 */
@Slf4j
public final class LocalCoverGenerator {

    private LocalCoverGenerator() {
    }

    public static String generatePlaceholderCover(File sourceDir, String outputDir, String fileName) {
        FileUtil.mkdir(outputDir);
        File target = new File(outputDir, fileName);

        BufferedImage image = new BufferedImage(1200, 675, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        try {
            graphics.setColor(new Color(18, 24, 38));
            graphics.fillRect(0, 0, 1200, 675);
            graphics.setColor(new Color(57, 91, 155));
            graphics.fillRoundRect(60, 60, 1080, 555, 32, 32);
            graphics.setColor(Color.WHITE);
            graphics.setFont(new Font("SansSerif", Font.BOLD, 44));
            graphics.drawString("CloudMorph Preview", 90, 170);
            graphics.setFont(new Font("SansSerif", Font.PLAIN, 28));
            graphics.drawString("Generated from: " + sourceDir.getName(), 90, 240);
            graphics.drawString("This is a local placeholder cover.", 90, 290);
            graphics.drawString("Replace with COS upload later.", 90, 340);
        } finally {
            graphics.dispose();
        }

        try {
            ImageIO.write(image, "png", target);
        } catch (Exception e) {
            throw new RuntimeException("生成封面失败: " + e.getMessage(), e);
        }
        return target.getAbsolutePath();
    }

    public static String toFileUrl(String filePath) {
        return "file://" + filePath.replace(File.separatorChar, '/');
    }
}
