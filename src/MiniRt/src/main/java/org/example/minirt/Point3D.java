package org.example.minirt;

public class Point3D {
    public double x, y, z;

    public Point3D() {
        x = 0;
        y = 0;
        z = 0;
    }

    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3D(double c) {
        this.x = c;
        this.y = c;
        this.z = c;
    }

    public Point3D add(Vector3D v) {
        return new Point3D(x + v.x, y + v.y, z + v.z);
    }

    public Point3D sub(Vector3D v) {
        return new Point3D(x - v.x, y - v.y, z - v.z);
    }

    public Vector3D sub(Point3D p) {
        return new Vector3D(x - p.x, y - p.y, z - p.z);
    }

    public static Point3D plus(Point3D p1, Point3D p2) {
        return new Point3D(p1.x + p2.x, p1.y + p2.y, p1.z + p2.z);
    }

    public static Point3D minus(Point3D p1, Point3D p2) {
        return new Point3D(p1.x - p2.x, p1.y - p2.y, p1.z - p2.z);
    }
}
