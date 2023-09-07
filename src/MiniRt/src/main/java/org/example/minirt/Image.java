package org.example.minirt;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Image {
    private BufferedImage image = null;

    public Image(int width, int height) {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public void set(int x, int y, Color color) {
        setRGB(x, y, color.toRGB());
    }

    public void setRGB(int x, int y, int rgb) {
        image.setRGB(x, getHeight() - y - 1, rgb);
    }

    public Color get(int x, int y) {
        return Color.fromRGB(getRGB(x, y));
    }

    public int getRGB(int x, int y) {
        return image.getRGB(x, getHeight() - y - 1);
    }

    // Insert src image starting from given position.
    public void copy(int x, int y, Image src) {
        int sx = Math.min(src.getWidth(), getWidth() - x);
        int sy = Math.min(src.getHeight(), getHeight() - y);
        for (int i = 0; i < sx; i++) {
            for (int j = 0; j < sy; j++) {
                setRGB(x + i, y + j, src.getRGB(i, j));
            }
        }
    }

    public void saveJPEG(String filename) {
        try {
            File file = new File(filename);
            ImageIO.write(image, "jpg", file);
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }
    }
}
