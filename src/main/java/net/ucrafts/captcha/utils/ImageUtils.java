package net.ucrafts.captcha.utils;

import com.github.johnnyjayjay.spigotmaps.util.ImageTools;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Base64;

public class ImageUtils
{

    public static BufferedImage base64ToBuffer(String base64)
    {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                    Base64.getDecoder().decode(base64)
            );

            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
            bufferedImage = ImageTools.resizeToMapSize(bufferedImage);

            byteArrayInputStream.close();

            return bufferedImage;
        } catch (Throwable e) {
            return null;
        }
    }
}
