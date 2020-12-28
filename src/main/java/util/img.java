package util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class img {

    public static boolean bufferedImagesEqual(BufferedImage img1, BufferedImage img2) {
        BufferedImage i1 = argb_img_resize(img1, img1.getWidth(), img1.getHeight());
        BufferedImage i2 = argb_img_resize(img2, img2.getWidth(), img2.getHeight());
        if (i1.getColorModel().getPixelSize() != i1.getColorModel().getPixelSize()) {
            return false;
        }
        if (i1.getWidth() != i2.getWidth() || i1.getHeight() != i2.getHeight()) {
            long s1 = img1.getWidth() * img1.getHeight();
            long s2 = img2.getWidth() * img2.getHeight();
            if (s1 < s2) {
                i2 = argb_img_resize(i2, i1.getWidth(), i1.getHeight());
            } else {
                i1 = argb_img_resize(i1, i2.getWidth(), i2.getHeight());
            }
        }
        double cnt = 0;
        for (int x = 0; x < i1.getWidth(); x++) {
            for (int y = 0; y < i1.getHeight(); y++) {
                try {
                    cnt += diff24bitRGB(i1.getRGB(x, y), i2.getRGB(x, y));
                } catch (Fail ex) {
                    return false;
                }
            }
        }
        double s = i1.getWidth() * i1.getHeight() * 3;
        return cnt <= (s * 0.25);
    }

    private static int diffColor(int c1, int c2) throws Fail {
        if (c1 < 0 || c2 < 0) {
            throw new Fail("diffColor(" + c1 + "," + c2 + "): negative color");
        }
        int max = Math.max(c1, c2);
        int min = Math.min(c1, c2);
        return (max - min - 1) <= (max / 2) ? 0 : 1;
    }

    private static int diff24bitRGB(int rgb1, int rgb2) throws Fail {
        int red1 = (rgb1 >> 16) & 0xFF;
        int red2 = (rgb2 >> 16) & 0xFF;
        int green1 = (rgb1 >> 8) & 0xFF;
        int green2 = (rgb2 >> 8) & 0xFF;
        int blue1 = rgb1 & 0xFF;
        int blue2 = rgb2 & 0xFF;
        return diffColor(red1, red2) + diffColor(blue1, blue2) + diffColor(green1, green2);
    }

    public static BufferedImage loadBufferedImage(File image) {
        try {
            return ImageIO.read(image);
        } catch (Exception | Error e) {
            return null;
        }
    }

    public static BufferedImage argb_img_resize(Image img, int new_width, int new_height) {
        BufferedImage new_img = new BufferedImage(new_width, new_height, BufferedImage.TYPE_INT_ARGB);
        new_img.createGraphics().drawImage(img, 0, 0, new_width, new_height, null);
        return new_img;
    }
}
