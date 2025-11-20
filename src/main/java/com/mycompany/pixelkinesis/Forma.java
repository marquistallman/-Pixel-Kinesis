package com.mycompany.pixelkinesis;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;   // opcional si quieres formas personalizadas
import java.awt.geom.AffineTransform; // opcional para rotaciones/escala
public class Forma {
    public Shape shape;

    public Forma(Shape shape) {
        this.shape = shape;
    }

    public boolean contiene(double x, double y) {
        return shape.contains(x, y);
    }

    public Shape getBounds() {
        return shape.getBounds2D();
    }
}

