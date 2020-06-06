package ru.nstu.javaprog.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class ImageReader {
    private ImageReader() {
    }

    public static BufferedImage readImage(String pathname) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(pathname));
        } catch (IOException exception) {
            System.err.println("Couldn't to read an image " + pathname);
        }
        return image;
    }
}
