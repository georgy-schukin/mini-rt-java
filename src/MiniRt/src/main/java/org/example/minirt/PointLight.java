package org.example.minirt;

public class PointLight {
    private Point3D position = new Point3D();
    private Color color = new Color();
    public PointLight(Point3D position, Color color) {
        this.position = position;
        this.color = color;
    }

    public void setPosition(Point3D pos) {
        this.position = pos;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Point3D getPosition() {
        return position;
    }

    public Color getColor() {
        return color;
    }

}
