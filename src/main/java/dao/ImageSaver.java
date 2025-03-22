package dao;

import java.io.FileOutputStream;
import java.io.IOException;

public class ImageSaver {
    public static void saveImage(byte[] imageBytes, String filePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(imageBytes);
        }
    }
}