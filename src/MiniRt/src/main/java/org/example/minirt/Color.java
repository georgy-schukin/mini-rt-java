package org.example.minirt;

public class Color {
    public double red, green, blue;

    public Color() {
        red = 0;
        green = 0;
        blue = 0;
    }

    public Color(double red, double green, double blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Color(double c) {
        this.red = c;
        this.green = c;
        this.blue = c;
    }

    public Color (Color color) {
        this.red = color.red;
        this.green = color.green;
        this.blue = color.blue;
    }

    public Color multiplied(double coeff) {
        return new Color(red * coeff, green * coeff, blue * coeff);
    }

    public Color multiplied(Color c) {
        return new Color(red * c.red, green * c.green, blue * c.blue);
    }

    public Color divided(double coeff) {
        return new Color(red / coeff, green / coeff, blue / coeff);
    }

    public Color add(Color c) {
        return new Color(red + c.red, green + c.green, blue + c.blue);
    }

    public Color clamped(double min, double max) {
        double r = Math.max(min, Math.min(max, red));
        double g = Math.max(min, Math.min(max, green));
        double b = Math.max(min, Math.min(max, blue));
        return new Color(r, g, b);
    }

    public static Color fromRGB(int rgb) {
        java.awt.Color rgbColor = new java.awt.Color(rgb);
        float[] comps = rgbColor.getRGBColorComponents(null);
        return new Color(comps[0], comps[1], comps[2]);
    }

    public int toRGB() {
        java.awt.Color rgbColor = new java.awt.Color((float)red, (float)green, (float)blue);
        return rgbColor.getRGB();
    }
}
