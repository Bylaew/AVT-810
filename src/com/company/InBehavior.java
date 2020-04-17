package com.company;
import java.awt.image.BufferedImage;
import java.io.IOException;
public interface InBehavior {
    BufferedImage getImage() throws IOException;
    int getX();
    int getY();
}
