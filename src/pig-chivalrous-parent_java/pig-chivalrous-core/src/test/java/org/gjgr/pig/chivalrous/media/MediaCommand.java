package org.gjgr.pig.chivalrous.media;

import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @Author gwd
 * @Time 01-16-2019 Wednesday
 * @Description: developer.tools:
 * @Target:
 * @More:
 */
public class MediaCommand {

    @Test
    public void testImageMetadataReader() throws IOException, URISyntaxException {
        String imagePath = "https://www.gstatic.com/webp/gallery/1.webp";
        // imagePath="file://./1.webp";
        BufferedImage bufferedImage = ImageIO.read(new URL(imagePath));
        File file = new File(new URI(imagePath));
        FileInputStream fileInputStream = new FileInputStream(file);

    }
}
