package org.example;

import org.example.minirt.*;

public class MiniRt {

    public static Scene initScene() {
        Scene scene = new Scene();

        Color red = new Color(1, 0.2, 0.2);
        Color blue = new Color(0.2, 0.2, 1);
        Color green = new Color(0.2, 1, 0.2);
        Color white = new Color(0.8, 0.8, 0.8);
        Color yellow = new Color(1, 1, 0.2);

        Material metallicRed = new Material(red, white, 50);
        Material mirrorBlack = new Material(new Color(0.0), new Color(0.9), 1000);
        Material matteWhite = new Material(new Color (0.7), new Color (0.3), 1);
        Material metallicYellow = new Material(yellow, white, 250);
        Material greenishGreen = new Material(green, 0.5, 0.5);

        Material transparentGreen = new Material(green, 0.8, 0.2);
        transparentGreen.makeTransparent(1.0, 1.03);
        Material transparentBlue = new Material(blue, 0.4, 0.6);
        transparentBlue.makeTransparent(0.9, 0.7);

        scene.addSphere(new Sphere(new Point3D(0, -2, 7), 1, transparentBlue));
        scene.addSphere(new Sphere(new Point3D(-3, 2, 11), 2, metallicRed));
        scene.addSphere(new Sphere(new Point3D(0, 2, 8), 1, mirrorBlack));
        scene.addSphere(new Sphere(new Point3D(1.5, -0.5, 7), 1, transparentGreen));
        scene.addSphere(new Sphere(new Point3D(-2, -1, 6), 0.7, metallicYellow));
        scene.addSphere(new Sphere(new Point3D(2.2, 0.5, 9), 1.2, matteWhite));
        scene.addSphere(new Sphere(new Point3D(4, -1, 10), 0.7, metallicRed));

        scene.addLight(new PointLight(new Point3D(-15, 0, -15), white));
        scene.addLight(new PointLight(new Point3D(1, 1, 0), blue));
        scene.addLight(new PointLight(new Point3D(0, -10, 6), red));

        scene.setBackground(new Color(0.05, 0.05, 0.08));
        scene.setAmbient(new Color(0.1, 0.1, 0.1));
        scene.setRecursionLimit(20);

        scene.setCamera(new Camera(new Point3D(0, 0, -20), new Point3D(0, 0, 0)));

        return scene;
    }

    public static void main(String[] args) {
        int viewPlaneResolutionX = args.length > 0 ? Integer.parseInt(args[0]) : 600;
        int viewPlaneResolutionY = args.length > 1 ? Integer.parseInt(args[1]) : 600;
        int numOfSamples = args.length > 2 ? Integer.parseInt(args[2]) : 1;

        Scene scene = initScene();

        double backgroundSizeX = 4;
        double backgroundSizeY = 4;
        double backgroundDistance = 15;

        double viewPlaneDistance = 5;
        double viewPlaneSizeX = backgroundSizeX * viewPlaneDistance / backgroundDistance;
        double viewPlaneSizeY = backgroundSizeY * viewPlaneDistance / backgroundDistance;

        ViewPlane viewPlane = new ViewPlane(viewPlaneResolutionX, viewPlaneResolutionY,
                viewPlaneSizeX, viewPlaneSizeY, viewPlaneDistance);

        Image image = new Image(viewPlaneResolutionX, viewPlaneResolutionY); // computed image
        for(int x = 0; x < viewPlaneResolutionX; x++) {
            for (int y = 0; y < viewPlaneResolutionY; y++) {
                Color color = viewPlane.computePixel(scene, x, y, numOfSamples);
                image.set(x, y, color);
            }
        }
        image.saveJPEG("raytracing.jpg");
    }
}