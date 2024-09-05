package com.fd.rookie.spring.boot.service.impl;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * @author fd
 * @Description
 * @createTime 2024-05-23 19:41
 **/
public class ImageMerger {
    public static void mergeImages(String imagePath1, String imagePath2, String outputPath) throws IOException {
        // 读取两张图片
        BufferedImage bgImage = ImageIO.read(new URL(imagePath1));
        BufferedImage todoImage = ImageIO.read(new URL(imagePath2));

        // 合并图片
        // 假设两张图片大小相同，直接将第二张图片画到第一张图片上
        bgImage.getGraphics().drawImage(todoImage,
                (bgImage.getWidth() - todoImage.getHeight()) / 2, (bgImage.getHeight() - todoImage.getHeight()) / 2, null);

        // 保存合并后的图片
        ImageIO.write(bgImage, "PNG", new File(outputPath));
    }

    public static void main(String[] args) {
        try {
            String bgImageUrl = "file:///C:/天溯/工作文档/综合安全/3.4.0/开发/POI/设备&空间点位标签-选中.png";
            String todoImage = "file:///C:/天溯/工作文档/综合安全/3.4.0/开发/CategoryIcon/ACY.png";
            mergeImages(bgImageUrl, todoImage, "C:/Users/du/Desktop/ACY-选中.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
