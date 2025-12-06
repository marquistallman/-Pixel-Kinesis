package com.mycompany.pixelkinesis.ComposedFigures;
import java.awt.geom.AffineTransform;
import java.awt.Graphics2D;

public class Transformacion {

    private AffineTransform transform = new AffineTransform();

    public void trasladar(double dx, double dy) {
        transform.translate(dx, dy);
    }

    public void escalar(double s) {
        transform.scale(s, s);
    }

    public void rotar(double grados) {
        transform.rotate(Math.toRadians(grados));
    }

    public void aplicar(Graphics2D g) {
        g.transform(transform);
    }
}
