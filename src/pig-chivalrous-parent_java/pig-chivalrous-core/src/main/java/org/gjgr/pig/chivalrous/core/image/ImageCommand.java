package org.gjgr.pig.chivalrous.core.image;

import org.gjgr.pig.chivalrous.core.io.file.FileCommand;
import org.gjgr.pig.chivalrous.core.io.stream.StreamCommand;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;

/**
 * File Name : pig-chivalrous - org.gjgr.pig.chivalrous.core.image CopyRright (c) 1949-xxxx: File Number： Author：gwd
 * Date：on 2018/12/7 Modify：gwd Time ： Comment： Description： Version：
 */
public final class ImageCommand {

    public static BufferedImage open(String url) {
        if (FileCommand.isExist(url)) {
            try {
                return ImageIO.read(new URL(url));
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static BufferedImage open(InputStream inputStream) {
        if (StreamCommand.isExist(inputStream)) {
            try {
                return ImageIO.read(inputStream);
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }
}
