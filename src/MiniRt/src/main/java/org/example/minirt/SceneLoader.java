package org.example.minirt;

import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;

public class SceneLoader {
    private Map<String, Material> materials = new HashMap<>();

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
        if (tag == "sphere") {
            Sphere sphere = loadSphere(scanner);
            scene.addSphere(sphere);
        } else if (tag == "light") {
            PointLight light = loadLight(scanner);
            scene.addLight(light);
        } else if (tag == "camera") {
            Camera camera = loadCamera(scanner);
            scene.setCamera(camera);
        } else if (tag == "mat" || tag == "material") {
            String materialName = scanner.next();
            materials.put(materialName, loadMaterial(scanner));
        } else if (tag == "ambient") {
            Color color = loadColor(scanner);
            scene.setAmbient(color);
        } else if (tag == "background") {
            Color color = loadColor(scanner);
            scene.setBackground(color);
        } else if (tag == "recursion") {
            int limit = loadInt(scanner);
            scene.setRecursionLimit(limit);
        } else {
            throw new IOException("Unknown tag: " + tag);
        }
    }

    private Sphere loadSphere(Scanner scanner) throws IOException {
        Point3D pos = new Point3D(0);
        double radius = 1.0;
        Material material = new Material(new Color(1.0));
        while (scanner.hasNext()) {
            String tag = scanner.next();
            if (tag.isEmpty()) {
                continue;
            }
            if (tag == "pos" || tag == "position") {
                pos = loadPoint(scanner);
            } else if (tag == "rad" || tag == "radius") {
                radius = loadDouble(scanner);
            } else if (tag == "mat" || tag == "material") {
                String materialName = scanner.next();
                Material mat = materials.get(materialName);
                if (mat == null) {
                    throw new IOException("Unlnown material name: " + materialName);
                }
                material = mat.clone();
            } else if (tag == "color") {
                Color color = loadColor(scanner);
                material.setDiffuseAndSpecular(color);
            } else if (tag == "diffuse") {
                Color color = loadColor(scanner);
                material.setDiffuse(color);
            } else if (tag == "specular") {
                Color color = loadColor(scanner);
                material.setSpecular(color);
            } else if (tag == "shininess") {
                double shininess = loadDouble(scanner);
                material.setShininess(shininess);
            } else if (tag == "transparent") {
                double refrCoeff = loadDouble(scanner);
                double refrIndex = loadDouble(scanner);
                material.makeTransparent(refrCoeff, refrIndex);
            } else if (tag == "end") {
                break;
            } else {
                throw new IOException("Unknown sphere parameter: " + tag);
            }
        }
        return new Sphere(pos, radius, material);
    }

    private PointLight loadLight(Scanner scanner) throws IOException {
        Point3D pos = new Point3D(0);
        Color color = new Color(1);
        while (scanner.hasNext()) {
            String tag = scanner.next();
            if (tag.isEmpty()) {
                continue;
            }
            if (tag == "pos" || tag == "position") {
                pos = loadPoint(scanner);
            } else if (tag == "color") {
                color = loadColor(scanner);
            } else if (tag == "end") {
                break;
            } else {
                throw new IOException("Unknown light parameter: " + tag);
            }
        }
        return new PointLight(pos, color);
    }

    private Material loadMaterial(Scanner scanner) throws IOException  {
        Material material = new Material(new Color (1.0));
        while (scanner.hasNext()) {
            String tag = scanner.next();
            if (tag.isEmpty()) {
                continue;
            }
            if (tag == "color") {
                Color color = loadColor(scanner);
                material.setDiffuseAndSpecular(color);
            } else if (tag == "diffuse") {
                Color color = loadColor(scanner);
                material.setDiffuse(color);
            } else if (tag == "specular") {
                Color color = loadColor(scanner);
                material.setSpecular(color);
            } else if (tag == "shininess") {
                double shininess = loadDouble(scanner);
                material.setShininess(shininess);
            } else if (tag == "transparent") {
                double refrCoeff = loadDouble(scanner);
                double refrIndex = loadDouble(scanner);
                material.makeTransparent(refrCoeff, refrIndex);
            } else if (tag == "end") {
                break;
            } else {
                throw new IOException("Unknown material parameter: " + tag);
            }
        }
        return material;
    }

    private Camera loadCamera(Scanner scanner) throws IOException  {
        Point3D viewPoint = new Point3D(0);
        Point3D target = new Point3D(0);
        Vector3D up = new Vector3D(0, 1, 0);
        while (scanner.hasNext()) {
            String tag = scanner.next();
            if (tag.isEmpty()) {
                continue;
            }
            if (tag == "pos" || tag == "position") {
                viewPoint = loadPoint(scanner);
            } else if (tag == "target") {
                target = loadPoint(scanner);
            } else if (tag == "up") {
                up = loadVector(scanner);
            } else if (tag == "end") {
                break;
            } else {
                throw new IOException("Unknown camera parameter: " + tag);
            }
        }
        return new Camera(viewPoint, target, up);
    }

    private Color loadColor(Scanner scanner) {
        double red = loadDouble(scanner);
        double green = loadDouble(scanner);
        double blue = loadDouble(scanner);
        return new Color(red, green, blue);
    }

    private Point3D loadPoint(Scanner scanner) {
        double x = loadDouble(scanner);
        double y = loadDouble(scanner);
        double z = loadDouble(scanner);
        return new Point3D(x, y, z);
    }

    private Vector3D loadVector(Scanner scanner) {
        double x = loadDouble(scanner);
        double y = loadDouble(scanner);
        double z = loadDouble(scanner);
        return new Vector3D(x, y, z);
    }

    private double loadDouble(Scanner scanner) {
        return Double.parseDouble(scanner.next());
    }

    private int loadInt(Scanner scanner) {
        return Integer.parseInt(scanner.next());
    }
}
