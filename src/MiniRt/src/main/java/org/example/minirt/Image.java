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
        java.awt.Color rgbColor = new java.awt.Color((float)color.red, (float)color.green, (float)color.blue);
        image.setRGB(x, y, rgbColor.getRGB());
    }

    public void setRGB(int x, int y, int rgb) {
        image.setRGB(x, y, rgb);
    }

    public Color get(int x, int y) {
        int rgb = image.getRGB(x, y);
        java.awt.Color rgbColor = new java.awt.Color(rgb);
        float[] comps = rgbColor.getRGBColorComponents(null);
        return new Color(comps[0], comps[1], comps[2]);
    }

    public int getRGB(int x, int y) {
        return image.getRGB(x, y);
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
