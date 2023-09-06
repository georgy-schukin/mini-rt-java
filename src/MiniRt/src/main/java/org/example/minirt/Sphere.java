package org.example.minirt;

public class Sphere {
    private Point3D position = null;
    private double radius = 1.0;
    private Material material = null;

    public Sphere(Point3D position, double radius, Material material) {
        this.position = position;
        this.radius = radius;
        this.material = material;
    }

    public Sphere(Point3D position, double radius, Color color) {
        this.position = position;
        this.radius = radius;
        this.material = new Material(color);
    }

    public Point3D getPosition() {
        return position;
    }

    public double getRadius() {
        return radius;
    }

    public Material getMaterial() {
        return material;
    }

    public static class Intersection {
        public boolean isIntersected = false;
        public double distance = 0;

        public Intersection(boolean isIntersected) {
            this.isIntersected = isIntersected;
        }

        public Intersection(boolean isIntersected, double distance) {
            this.isIntersected = isIntersected;
            this.distance = distance;
        }
    }

    // Check intersection with the sphere.
    public Intersection intersect(Ray ray) {
        Vector3D v = ray.getOrigin().sub(position);
        double d = v.dot(ray.getDirection());
        double discriminant = d * d - (v.dot(v) - radius * radius);
        if (discriminant < 0) {
            return new Intersection(false);
        }

        double sq = Math.sqrt(discriminant);
        double t1 = -d + sq;
        double t2 = -d - sq;

        double distance = 0;
        double epsilon = 1e-6;
        if (t1 < epsilon) {
            if (t2 < epsilon) {
                return new Intersection(false);
            } else {
                distance = t2;
            }
        } else if (t2 < epsilon) {
            distance = t1;
        } else {
            distance = Math.min(t1, t2);
        }
        return new Intersection(true, distance);
    }

    public Vector3D normalTo(Point3D surfacePoint) {
        return surfacePoint.sub(position).normalized();
    }
}
