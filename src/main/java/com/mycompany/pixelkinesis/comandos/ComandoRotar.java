package com.mycompany.pixelkinesis.comandos;
import java.awt.*;
import java.awt.geom.*;
import com.mycompany.pixelkinesis.*;
import java.util.ArrayList;
public class ComandoRotar extends Comando {
    private final double grados;
    private final ComandoDibujar reload = new ComandoDibujar();

    public ComandoRotar(double grados) {
        this.grados = grados;
    }

    @Override
    public void ejecutar(Nodo nodo, Graphics2D g) {
        if (nodo instanceof FiguraGeometrica) {
            FiguraGeometrica fig = (FiguraGeometrica) nodo;

            Rectangle bounds = fig.forma.shape.getBounds();
            double cx = bounds.getCenterX();
            double cy = bounds.getCenterY();

            AffineTransform at = new AffineTransform();
            at.rotate(Math.toRadians(grados), cx, cy);

            fig.forma.shape = at.createTransformedShape(fig.forma.shape);
        }
        reload.ejecutar(nodo, g);
    }
}

