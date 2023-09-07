package org.example.minirt;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Scene {
    private List<Sphere> spheres = new ArrayList<>();
    private List<PointLight> lights = new ArrayList<>();

    private Color ambientLight = new Color(0.1, 0.1, 0.1);
    private Color backgroundColor = new Color(0);

    private int limitOfRecursion = 1;

    private Camera camera = null;

    public void addSphere(Sphere sphere) {
        spheres.add(sphere);
    }

    public void addLight(PointLight light) {
        lights.add(light);
    }

    public void setAmbient(Color ambient) {
        this.ambientLight = ambient;
    }

    public void setBackground(Color background){
        this.backgroundColor = background;
    }

    public void setRecursionLimit(int limit) {
        this.limitOfRecursion = limit;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Color getAmbient() {
        return ambientLight;
    }

    public Color getBackground() {
        return backgroundColor;
    }

    public int getRecursionLimit() {
        return limitOfRecursion;
    }

    public Camera getCamera() {
        return camera;
    }

    public void loadFromFile(String filename) {
        SceneLoader loader = new SceneLoader();
        loader.loadSceneFromFile(filename, this);
    }

    static public class Intersection {
        public Sphere object = null;
        public Point3D intersectionPoint = null;

        public Intersection(Sphere object, Point3D intersectionPoint) {
            this.object = object;
            this.intersectionPoint = intersectionPoint;
        }
    }

    // Returns closest object if there is an intersection or nullptr otherwise.
    public Intersection intersect(Ray ray) {
        Sphere closestSphere = null;
        double minDistance = Double.MAX_VALUE;
        for (Sphere sphere: spheres) {
            Sphere.Intersection inter = sphere.intersect(ray);
            if (inter.isIntersected && inter.distance < minDistance) {
                closestSphere = sphere;
                minDistance = inter.distance;
            }
        }
        if (closestSphere == null) {
            return new Intersection(null, null);
        }
        Point3D closestIntersectionPoint = ray.fromOrigin(minDistance);
        return new Intersection(closestSphere, closestIntersectionPoint);
    }

    // Compute color for this ray in the scene.
    public Color illumination(Ray ray, int recursionStep) {
        if (recursionStep >= limitOfRecursion) {
            return new Color();
        }

        final Intersection inter = intersect(ray);
        if (inter.object == null) {
            return backgroundColor;
        }

        Sphere sphere = inter.object;
        Point3D closestIntersectionPoint = inter.intersectionPoint;
        Material material = sphere.getMaterial();

        // Normal for the sphere and reflected ray.
        Vector3D normal = sphere.normalTo(closestIntersectionPoint);
        Vector3D toViewer = ray.getDirection().reversed();
        double cosThetaI = normal.dot(toViewer);
        Vector3D reflected = normal.multiplied(2 * cosThetaI).sub(toViewer);

        // Add ambient light.
        Color color = new Color(ambientLight);

        // Add illumination from each light.
        for (PointLight light: lights) {
            // Check if the point on the object is illuminated by this light (not obscured by an obstacle).
            Vector3D toLight = light.getPosition().sub(closestIntersectionPoint);
            double distanceToLight = toLight.length();
            toLight = toLight.normalized();
            boolean lightIsVisible = true;
            final Intersection obstacle = intersect(new Ray(closestIntersectionPoint, toLight));
            if (obstacle.object != null) {
                // Check if the light is closer than the intersected object.
                double distanceToObstacle = obstacle.intersectionPoint.sub(closestIntersectionPoint).length();
                if (distanceToObstacle <= distanceToLight) {
                    lightIsVisible = false;
                }
            }
            if (lightIsVisible) {
                // Apply coefficients of the body color to the intensity of the light source.
                color = color.add(material.shade(light.getColor(), normal, reflected, toLight, toViewer));
            }
        }

        double refractionCoeff = material.getRefractionCoeff();
        Vector3D refracted = new Vector3D();
        // Check for refraction.
        if (refractionCoeff > 0) {
            double nu = 1.0 / material.getRefractionIndex(); // assume refraction index 1.0 for air
            // Check if we hit object from inside.
            if (cosThetaI < 0) {
                nu = 1.0 / nu;
                normal = normal.reversed();
                cosThetaI = -cosThetaI;
            }
            double cosThetaT = 1.0 - (1.0 - cosThetaI * cosThetaI) * (nu * nu);
            // Check for total internal reflection (no refraction).
            if (cosThetaT < 0) {
                refractionCoeff = 0.0;
            } else {
                cosThetaT = Math.sqrt(cosThetaT);
                refracted = normal.multiplied(cosThetaI * nu - cosThetaT).sub(toViewer.multiplied(nu));
            }
        }

        // Add refraction.
        if (refractionCoeff > 0) {
            Ray refractedRay = new Ray(closestIntersectionPoint, refracted.normalized());
            Color refractionColor = illumination(refractedRay, recursionStep + 1);
            color = color.add(refractionColor.multiplied(refractionCoeff));
        }

        // Add reflection.
        double reflectionCoeff = 1.0 - refractionCoeff;
        if (reflectionCoeff > 0) {
            Ray reflectedRay = new Ray(closestIntersectionPoint, reflected.normalized());
            Color reflectionColor = illumination(reflectedRay, recursionStep + 1);
            color = color.add(reflectionColor.multiplied(material.getSpecular()).multiplied(reflectionCoeff));
        }

        return color;
    }
}
