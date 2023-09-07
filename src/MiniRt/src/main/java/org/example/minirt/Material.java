package org.example.minirt;

public class Material {
    private Color diffuseColor = new Color(1, 1, 1);
    private Color specularColor = new Color(1, 1, 1);
    private double shininess = 25;
    // Amount of refraction in [0, 1].
    // 0 - no refraction, 1 - full refraction (no reflection).
    private double refractionCoeff = 0;
    // Index of refraction (1 for air).
    private double refractionIndex = 1;

    public Material(Color color) {
        this.diffuseColor= color;
    }

    public Material(Color diffuse, Color specular, double shininess) {
        this.diffuseColor = diffuse;
        this.specularColor = specular;
        this.shininess = shininess;
    }

    public Material(Color color, double diffuseCoeff, double specularCoeff, double shininess) {
        this.diffuseColor = color.multiplied(diffuseCoeff);
        this.specularColor = color.multiplied(specularCoeff);
        this.shininess = shininess;
    }

    public Material(Color color, double diffuseCoeff, double specularCoeff) {
        this.diffuseColor = color.multiplied(diffuseCoeff);
        this.specularColor = color.multiplied(specularCoeff);
        this.shininess = 25;
    }

    public Material clone() {
        Material clone = new Material(diffuseColor, specularColor, shininess);
        clone.makeTransparent(refractionCoeff, refractionIndex);
        return clone;
    }

    public void setDiffuse(Color diffuse) {
        this.diffuseColor = diffuse;
    }

    public void setSpecular(Color specular) {
        this.specularColor = specular;
    }

    public void setDiffuseAndSpecular(Color color) {
        this.diffuseColor = color;
        this.specularColor = color;
    }

    public void setShininess(double shininess) {
        this.shininess = shininess;
    }

    public Color getDiffuse() {
        return diffuseColor;
    }

    public Color getSpecular() {
        return specularColor;
    }

    public double getShininess() {
        return shininess;
    }

    public double getRefractionCoeff() {
        return refractionCoeff;
    }

    public double getRefractionIndex() {
        return refractionIndex;
    }

    public void makeTransparent(double refractionCoeff, double refractionIndex) {
        this.refractionCoeff = refractionCoeff;
        this.refractionIndex = refractionIndex;
    }

    public Color shade(Color lightColor, Vector3D normal, Vector3D reflected, Vector3D toLight, Vector3D toViewer) {
        double diffuse = Math.max(toLight.dot(normal), 0.0);
        double specular = 0.0;
        if (diffuse > 0.0 && shininess > 0.0) {
            specular = Math.pow(Math.max(reflected.dot(toViewer), 0.0), shininess);
        }
        return (diffuseColor.multiplied(diffuse).add(specularColor.multiplied(specular))).multiplied(lightColor);
    }
}
