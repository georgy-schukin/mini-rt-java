package org.example.minirt;

public class Camera {
    // Camera's position.
    private Point3D viewPoint = new Point3D(0, 0, -20);
    // Look a target.
    private Point3D target = new Point3D(0, 0, 0);
    // Unit basis vectors for camera (left-handed system).
    private Vector3D unitX = new Vector3D(1, 0, 0);
    private Vector3D unitY = new Vector3D(0, 1, 0);
    private Vector3D unitZ = new Vector3D(0, 0, 1);

    public Camera(Point3D viewPoint) {
        setViewPoint(viewPoint);
    }

    public Camera(Point3D viewPoint, Point3D target) {
        set(viewPoint, target, new Vector3D(0, 1, 0));
    }

    public Camera(Point3D viewPoint, Point3D target, Vector3D up) {
        set(viewPoint, target, up);
    }

    public void setViewPoint(Point3D point) {
        Vector3D shift = point.sub(viewPoint);
        viewPoint = point;
        target = target.add(shift);
    }

    public void setTarget(Point3D tgt) {
        target = tgt;
    }

    public void setUnitX(Vector3D unit) {
        unitX = unit;
    }

    public void setUnitY(Vector3D unit) {
        unitY = unit;
    }

    public void setUnitZ(Vector3D unit) {
        unitZ = unit;
    }

    public Point3D getViewPoint() {
        return viewPoint;
    }

    public Point3D getTarget() {
        return target;
    }

    public Vector3D getUnitX() {
        return unitX;
    }

    public Vector3D getUnitY() {
        return unitY;
    }

    public Vector3D getUnitZ() {
        return unitZ;
    }

    public void set(Point3D viewPoint, Point3D target, Vector3D up) {
        this.viewPoint = viewPoint;
        this.target = target;
        // Set up left-handed coordinate system for camera.
        unitZ = target.sub(viewPoint).normalized();
        unitX = up.cross(unitZ).normalized();
        unitY = unitZ.cross(unitX);
    }

    public Ray rayFrom(double dx, double dy, double dz) {
        Vector3D ux = unitX.multiplied(dx);
        Vector3D uy = unitY.multiplied(dy);
        Vector3D uz = unitZ.multiplied(dz);
        Vector3D rayDirection = ux.add(uy).add(uz);
        return new Ray(viewPoint, rayDirection.normalized());
    }

    public void rotateAroundTarget(double degrees) {
        final double pi = 3.14159265359;
        final Vector3D dir = viewPoint.sub(target);
        final double radius = Math.sqrt(dir.x * dir.x + dir.z * dir.z);
        final double angle = Math.atan2(dir.z, dir.x);
        final double newAngle = angle + pi * degrees / 180.0;
        Point3D newViewPoint = new Point3D(
                target.x + radius * Math.cos(newAngle),
                viewPoint.y,
                target.z + radius * Math.sin(newAngle));
        set(newViewPoint, target, unitY);
    }
}
