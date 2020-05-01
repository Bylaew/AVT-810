package LabObjects;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DownloadImage {
    protected BufferedImage wood_image;
    protected BufferedImage kap_image;
    private DownloadImage(){
        try{
            File file_wooden= new File("./resources/Wooden.png");
            wood_image = ImageIO.read(file_wooden);
            File file_kap= new File("./resources/Kap.png");
           kap_image = ImageIO.read(file_kap);
        } catch (IOException e) {

            System.out.println("Файл не найден");}
    }
    public static class DownloadHolder{
        public static final DownloadImage HOLDER_INSTANCE = new DownloadImage();
    }

    public static DownloadImage getInstance(){
        return DownloadHolder.HOLDER_INSTANCE;
    }
}
