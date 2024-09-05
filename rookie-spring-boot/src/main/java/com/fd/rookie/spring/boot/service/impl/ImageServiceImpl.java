package com.fd.rookie.spring.boot.service.impl;

import com.fd.rookie.spring.boot.service.ImageService;
import com.freeway.image.combiner.ImageCombiner;
import com.freeway.image.combiner.element.TextElement;
import com.freeway.image.combiner.enums.OutputFormat;
import com.freeway.image.combiner.enums.ZoomMode;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;

/**
 * @author fd
 * @Description
 * @createTime 2024-05-23 18:44
 **/
@Service
public class ImageServiceImpl implements ImageService {
    @Override
    public InputStream generateSimpleImage(String text, String bgImageUrl, String todoImageUrl, String localPath, Boolean saveLocal, Boolean saveOss) {
        InputStream is = null;
        try{
            // 合成器（指定背景图和输出格式，整个图片的宽高和相关计算依赖于背景图，所以背景图的大小是个基准）
            ImageCombiner combiner = new ImageCombiner(bgImageUrl, OutputFormat.JPG);
            // 加图片元素，第二个参数是左边界距，第三个参数是上边距
            BufferedImage bgImage = ImageIO.read(new URL(bgImageUrl));
            BufferedImage todoImage = ImageIO.read(new URL(todoImageUrl));
            combiner.addImageElement(todoImageUrl,
                    (bgImage.getWidth() - todoImage.getHeight()) / 2, (bgImage.getHeight() - todoImage.getHeight()) / 2);
            // 加文本元素，第二个参数是字体大小，第三个参数是左边界距，第四个参数是上边距
//            combiner.addTextElement(text, 60, 100, 960);
            // 执行图片合并
            combiner.combine();
            // 可以获取流（并上传oss等）
            is = combiner.getCombinedImageStream();
            // 保存到本地
            if(saveLocal){
                combiner.save(localPath);
            }
            // 保存到oss
            if(saveOss){
                // TODO: 2021/12/17 保存到oss
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return is;
    }

    @Override
    public InputStream generateComplexImage(String title, String content, String bgImageUrl, String qrCodeUrl, String productImageUrl, String waterMarkImageUrl, String avatarImageUrl, String localPath, Boolean saveLocal, Boolean saveOss) {
        InputStream is = null;
        try{
            BufferedImage waterMark = ImageIO.read(new URL(waterMarkImageUrl)); //水印图
            BufferedImage avatar = ImageIO.read(new URL(avatarImageUrl));       //头像
            //创建合成器（指定背景图和输出格式，整个图片的宽高和相关计算依赖于背景图，所以背景图的大小是个基准）
            ImageCombiner combiner = new ImageCombiner(bgImageUrl, 1500, 0, ZoomMode.Height, OutputFormat.JPG);  //v1.1.4之后可以指定背景图新宽高了（不指定则默认用图片原宽高）

            //针对背景和整图的设置
            combiner.setBackgroundBlur(30);     //设置背景高斯模糊（毛玻璃效果）
            combiner.setCanvasRoundCorner(100); //设置整图圆角（输出格式必须为PNG）

            //标题（默认字体为阿里普惠、黑色，也可以自己指定Font对象）
            combiner.addTextElement(title, 0, 150, 1400)
                    .setCenter(true)        //居中绘制（会忽略x坐标，改为自动计算）
                    .setAlpha(.8f)         //透明度（0.0~1.0）
                    .setRotate(45)         //旋转（0~360）
                    .setColor(Color.red);    //颜色

            //内容（设置文本自动换行，需要指定最大宽度（超出则换行）、最大行数（超出则丢弃）、行高）
            combiner.addTextElement(content, "微软雅黑", 40, 150, 1480)
                    .setStrikeThrough(true)             //删除线
                    .setAutoBreakLine(837, 2, 60);      //自动换行

            //商品图（设置坐标、宽高和缩放模式，若按宽度缩放，则高度按比例自动计算）
            combiner.addImageElement(productImageUrl, 0, 160, 837, 0, ZoomMode.Width)
                    .setCenter(true)       //居中绘制（会忽略x坐标，改为自动计算）
                    .setRoundCorner(46);     //设置圆角

            //头像（圆角设置一定的大小，可以把头像变成圆的）
            combiner.addImageElement(avatar, 200, 1200)
                    .setRoundCorner(200);   //圆角

            //水印（设置透明度，0.0~1.0）
            combiner.addImageElement(waterMark, 630, 1200)
                    .setAlpha(.8f)         //透明度（0.0~1.0）
                    .setRotate(45)         //旋转（0~360）
                    .setBlur(20);           //高斯模糊(1~100)

            //加入圆角矩形元素（版本>=1.2.0），作为二维码的底衬
            combiner.addRectangleElement(138, 1707, 300, 300)
                    .setColor(Color.WHITE)
                    .setRoundCorner(50)     //该值大于等于宽高时，就是圆形，如设为300
                    .setAlpha(.8f);

            //二维码（强制按指定宽度、高度缩放）
            combiner.addImageElement(qrCodeUrl, 138, 1707, 186, 186, ZoomMode.WidthHeight);

            //价格（元素对象也可以直接new，然后手动加入待绘制列表）
            TextElement textPrice = new TextElement("￥1290", 60, 230, 1300);
            textPrice.setColor(Color.red);          //红色
            textPrice.setStrikeThrough(true);       //删除线
            combiner.addElement(textPrice);         //加入待绘制集合

            //执行图片合并
            combiner.combine();

            //可以获取流（并上传oss等）
            is = combiner.getCombinedImageStream();

            //保存到本地
            if(saveLocal){
                combiner.save(localPath);
            }
            //保存到oss
            if(saveOss){
                // TODO: 2021/12/17 保存到oss
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return is;
    }
}
