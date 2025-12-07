package com.mycompany.pixelkinesis.comandos;
import java.awt.*;
import java.awt.geom.*;
import com.mycompany.pixelkinesis.*;
import com.mycompany.pixelkinesis.ComposedFigures.ComposedFigures;
public class ComandoEscalar extends Comando {
    private final double factor;
    private final ComandoDibujar reload = new ComandoDibujar();

    public ComandoEscalar(double factor) {
        this.factor = factor;
    }

    @Override
    public void ejecutar(Nodo nodo, Graphics2D g) {
        if (nodo instanceof FiguraGeometrica fig) {
            Shape sh = fig.forma.shape;
            AffineTransform at = new AffineTransform();
            at.scale(factor, factor);
            fig.forma.shape = at.createTransformedShape(sh);
            reload.ejecutar(nodo, g);
            return;
        }

        if (nodo instanceof ComposedFigures composed) {
            for (FiguraGeometrica f : composed.getFiguras()) {
                Shape sh = f.forma.shape;
                AffineTransform at = new AffineTransform();
                at.scale(factor, factor);
                f.forma.shape = at.createTransformedShape(sh);
            }
            // Recalculate composed area if needed
            composed.recalcularArea();
            reload.ejecutar(nodo, g);
            return;
        }
        reload.ejecutar(nodo, g);
    }
}

