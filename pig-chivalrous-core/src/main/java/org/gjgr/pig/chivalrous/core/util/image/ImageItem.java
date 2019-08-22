package org.gjgr.pig.chivalrous.core.util.image;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

/**
 * @Author gwd
 * @Time 03-04-2019 Monday
 * @Description: developer.tools:
 * @Target:
 * @More:
 */
public class ImageItem extends Image {

    @Override
    public int getWidth(ImageObserver observer) {

        return 0;
    }

    @Override
    public int getHeight(ImageObserver observer) {
        return 0;
    }

    @Override
    public ImageProducer getSource() {

        return null;
    }

    @Override
    public Graphics getGraphics() {
        return null;
    }

    @Override
    public Object getProperty(String name, ImageObserver observer) {
        return null;
    }

}
