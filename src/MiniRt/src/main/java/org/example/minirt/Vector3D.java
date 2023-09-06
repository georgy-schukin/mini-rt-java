package org.example.minirt;

public class Vector3D {
    public double x, y, z;

    public Vector3D() {
        x = 0;
        y = 0;
        z = 0;
    }

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D(double c) {
        this.x = c;
        this.y = c;
        this.z = c;
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public double lengthSquared() {
        return x * x + y * y + z * z;
    }

    public Vector3D normalized() {
        return divided(length());
    }

    public double dot(Vector3D v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public Vector3D cross(Vector3D v) {
        return new Vector3D(
                y * v.z - z * v.y,
                z * v.x - x * v.z,
                x * v.y - y * v.x);
    }

    public Vector3D reversed() {
        return new Vector3D(-x, -y, -z);
    }

    public Vector3D multiplied(double c) {
        return new Vector3D(x * c, y * c, z * c);
    }

    public Vector3D divided(double c) {
        return multiplied(1.0 / c);
    }

    public Vector3D add(Vector3D v) {
        return new Vector3D(x + v.x, y + v.y, z + v.z);
    }

    public Vector3D sub(Vector3D v) {
        return new Vector3D(x - v.x, y - v.y, z - v.z);
    }

    public static Vector3D plus(Vector3D p1, Vector3D p2) {
        return new Vector3D(p1.x + p2.x, p1.y + p2.y, p1.z + p2.z);
    }

    public static Vector3D minus(Vector3D v1, Vector3D v2) {
        return new Vector3D(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }

    public static Vector3D fromPoints(Point3D start, Point3D end) {
        return new Vector3D(end.x - start.x, end.y - start.y, end.z - start.z);
    }
}
