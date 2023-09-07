package org.example.minirt;

import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;

public class SceneLoader {
    private final Map<String, Material> materials = new HashMap<>();

    public void loadSceneFromFile(String filename, Scene scene) {
        try {
            File input = new File(filename);
            Scanner scanner = new Scanner(input);
            while (scanner.hasNext()) {
                String tag = scanner.next();
                processNextTag(tag, scanner, scene);
            }
        } catch (IOException e) {
            System.err.println("Error reading file " + filename + ": " + e.getMessage());
        }
    }

    private void processNextTag(String tag, Scanner scanner, Scene scene) throws IOException {
        if (tag.isEmpty()) {
            return;
        }
        switch (tag) {
            case "sphere" -> {
                Sphere sphere = readSphere(scanner);
                scene.addSphere(sphere);
            }
            case "light" -> {
                PointLight light = readLight(scanner);
                scene.addLight(light);
            }
            case "camera" -> {
                Camera camera = readCamera(scanner);
                scene.setCamera(camera);
            }
            case "mat", "material" -> {
                String materialName = scanner.next();
                materials.put(materialName, readMaterial(scanner));
            }
            case "ambient" -> {
                Color ambientColor = readColor(scanner);
                scene.setAmbient(ambientColor);
            }
            case "background" -> {
                Color bgColor = readColor(scanner);
                scene.setBackground(bgColor);
            }
            case "recursion" -> {
                int limit = readInt(scanner);
                scene.setRecursionLimit(limit);
            }
            default -> throw new IOException("Unknown tag: " + tag);
        }
    }

    private Sphere readSphere(Scanner scanner) throws IOException {
        Point3D pos = new Point3D(0);
        double radius = 1.0;
        Material material = new Material(new Color(1.0));
        boolean stop = false;
        while (scanner.hasNext() && !stop) {
            String tag = scanner.next();
            if (tag.isEmpty()) {
                continue;
            }
            switch (tag) {
                case "pos", "position" -> {
                    pos = readPoint(scanner);
                }
                case "rad", "radius" -> {
                    radius = readDouble(scanner);
                }
                case "mat", "material" -> {
                    String materialName = scanner.next();
                    Material mat = materials.get(materialName);
                    if (mat == null) {
                        throw new IOException("Unknown material name: " + materialName);
                    }
                    material = mat.clone();
                }
                case "color" -> {
                    Color color = readColor(scanner);
                    material.setDiffuseAndSpecular(color);
                }
                case "diffuse" -> {
                    Color diffuseColor = readColor(scanner);
                    material.setDiffuse(diffuseColor);
                }
                case "specular" -> {
                    Color specularColor = readColor(scanner);
                    material.setSpecular(specularColor);
                }
                case "shininess" -> {
                    double shininess = readDouble(scanner);
                    material.setShininess(shininess);
                }
                case "transparent" -> {
                    double refrCoeff = readDouble(scanner);
                    double refrIndex = readDouble(scanner);
                    material.makeTransparent(refrCoeff, refrIndex);
                }
                case "end" -> stop = true;
                default -> throw new IOException("Unknown sphere parameter: " + tag);
            }
        }
        return new Sphere(pos, radius, material);
    }

    private PointLight readLight(Scanner scanner) throws IOException {
        Point3D pos = new Point3D(0);
        Color color = new Color(1);
        boolean stop = false;
        while (scanner.hasNext() && !stop) {
            String tag = scanner.next();
            if (tag.isEmpty()) {
                continue;
            }
            switch (tag) {
                case "pos", "position" -> {
                    pos = readPoint(scanner);
                }
                case "color" -> {
                    color = readColor(scanner);
                }
                case "end" -> stop = true;
                default -> throw new IOException("Unknown light parameter: " + tag);
            }
        }
        return new PointLight(pos, color);
    }

    private Material readMaterial(Scanner scanner) throws IOException  {
        Material material = new Material(new Color (1.0));
        boolean stop = false;
        while (scanner.hasNext() && !stop) {
            String tag = scanner.next();
            if (tag.isEmpty()) {
                continue;
            }
            switch (tag) {
                case "color" -> {
                    Color color = readColor(scanner);
                    material.setDiffuseAndSpecular(color);
                }
                case "diffuse" -> {
                    Color diffuseColor = readColor(scanner);
                    material.setDiffuse(diffuseColor);
                }
                case "specular" -> {
                    Color specularColor = readColor(scanner);
                    material.setSpecular(specularColor);
                }
                case "shininess" -> {
                    double shininess = readDouble(scanner);
                    material.setShininess(shininess);
                }
                case "transparent" -> {
                    double refrCoeff = readDouble(scanner);
                    double refrIndex = readDouble(scanner);
                    material.makeTransparent(refrCoeff, refrIndex);
                }
                case "end" -> stop = true;
                default -> throw new IOException("Unknown material parameter: " + tag);
            }
        }
        return material;
    }

    private Camera readCamera(Scanner scanner) throws IOException  {
        Point3D viewPoint = new Point3D(0);
        Point3D target = new Point3D(0);
        Vector3D up = new Vector3D(0, 1, 0);
        boolean stop = false;
        while (scanner.hasNext() && !stop) {
            String tag = scanner.next();
            if (tag.isEmpty()) {
                continue;
            }
            switch (tag) {
                case "pos", "position" -> viewPoint = readPoint(scanner);
                case "target" -> target = readPoint(scanner);
                case "up" -> up = readVector(scanner);
                case "end" -> stop = true;
                default -> throw new IOException("Unknown camera parameter: " + tag);
            }
        }
        return new Camera(viewPoint, target, up);
    }

    private Color readColor(Scanner scanner) {
        double red = readDouble(scanner);
        double green = readDouble(scanner);
        double blue = readDouble(scanner);
        return new Color(red, green, blue);
    }

    private Point3D readPoint(Scanner scanner) {
        double x = readDouble(scanner);
        double y = readDouble(scanner);
        double z = readDouble(scanner);
        return new Point3D(x, y, z);
    }

    private Vector3D readVector(Scanner scanner) {
        double x = readDouble(scanner);
        double y = readDouble(scanner);
        double z = readDouble(scanner);
        return new Vector3D(x, y, z);
    }

    private double readDouble(Scanner scanner) {
        return Double.parseDouble(scanner.next());
    }

    private int readInt(Scanner scanner) {
        return Integer.parseInt(scanner.next());
    }
}
