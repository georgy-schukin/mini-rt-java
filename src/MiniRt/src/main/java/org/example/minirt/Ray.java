package org.example.minirt;

public class Ray {
    private Point3D origin = new Point3D();
    private Vector3D direction = new Vector3D();

    public Ray(Point3D origin, Vector3D direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public void setOrigin(Point3D origin) {
        this.origin = origin;
    }

    public void setDirection(Vector3D dir) {
        this.direction = dir;
    }

    public Point3D getOrigin() {
        return origin;
    }

    public Vector3D getDirection() {
        return direction;
    }

    public Point3D fromOrigin(double distance) {
        Vector3D d = direction.multiplied(distance);
        return origin.add(d);
    }
}
