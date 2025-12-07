package com.mycompany.pixelkinesis.comandos;
import java.awt.*;
import java.awt.geom.*;
import com.mycompany.pixelkinesis.*;
import com.mycompany.pixelkinesis.ComposedFigures.ComposedFigures;
public class ComandoRotar extends Comando {
    private final double grados;
    private final ComandoDibujar reload = new ComandoDibujar();

    public ComandoRotar(double grados) {
        this.grados = grados;
    }

    @Override
    public void ejecutar(Nodo nodo, Graphics2D g) {
        if (nodo instanceof FiguraGeometrica fig) {
            Rectangle bounds = fig.forma.shape.getBounds();
            double cx = bounds.getCenterX();
            double cy = bounds.getCenterY();

            AffineTransform at = new AffineTransform();
            at.rotate(Math.toRadians(grados), cx, cy);

            fig.forma.shape = at.createTransformedShape(fig.forma.shape);
            reload.ejecutar(nodo, g);
            return;
        }

        if (nodo instanceof ComposedFigures composed) {
            for (FiguraGeometrica f : composed.getFiguras()) {
                Rectangle bounds = f.forma.shape.getBounds();
                double cx = bounds.getCenterX();
                double cy = bounds.getCenterY();
                AffineTransform at = new AffineTransform();
                at.rotate(Math.toRadians(grados), cx, cy);
                f.forma.shape = at.createTransformedShape(f.forma.shape);
            }
            composed.recalcularArea();
            reload.ejecutar(nodo, g);
            return;
        }
        reload.ejecutar(nodo, g);
    }
}

