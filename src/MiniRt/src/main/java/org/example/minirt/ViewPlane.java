package org.example.minirt;

import java.util.ArrayList;
import java.util.List;

public class ViewPlane {
    private int resolutionX = 0;
    private int resolutionY = 0;
    private double sizeX = 0;
    private double sizeY = 0;
    private double distance = 0; // distance to view plane from view point

    public ViewPlane(int resolutionX, int resolutionY, double sizeX, double sizeY, double distance) {
        this.resolutionX = resolutionX;
        this.resolutionY = resolutionY;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.distance = distance;
    }

    public Color computePixel(Scene scene, int x, int y, int numOfSamples) {
        return computePixel(scene, scene.getCamera(), x, y, numOfSamples);
    }

    // Compute color for the pixel with the given index.
    public Color computePixel(Scene scene, Camera camera, int x, int y, int numOfSamples) {
        List<Sampler.Sample2D> samples = new ArrayList<>();
        if (numOfSamples == 1) {
            // One sample - use pixel's center.
            samples.add(new Sampler.Sample2D(0.5, 0.5));
        } else {
            // Use random samples.
            Sampler sampler = new Sampler();
            samples = sampler.sampleRandom(numOfSamples);
        }
        final double aspect = (double)resolutionX / (double)resolutionY;
        Color color = new Color();
        for (Sampler.Sample2D sample: samples) {
            double sx = (double)x + sample.x;
            double sy = (double)y + sample.y;
            // A ray from the eye.
            double dx = sx * sizeX * aspect / resolutionX - sizeX * aspect / 2;
            double dy = sy * sizeY / resolutionY - sizeY / 2;
            double dz = distance;
            color = color.add(scene.illumination(camera.rayFrom(dx, dy, dz), 0));
        }
        return color.divided(numOfSamples).clamped(0, 1);
    }
}
