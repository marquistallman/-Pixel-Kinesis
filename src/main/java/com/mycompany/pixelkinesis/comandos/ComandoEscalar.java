package com.mycompany.pixelkinesis.comandos;
import java.awt.*;
import java.awt.geom.*;
import com.mycompany.pixelkinesis.*;
import java.util.ArrayList;
public class ComandoEscalar extends Comando {
    private final double factor;
    private final ComandoDibujar reload = new ComandoDibujar();

    public ComandoEscalar(double factor) {
        this.factor = factor;
    }

    @Override
    public void ejecutar(Nodo nodo, Graphics2D g) {
        if (nodo instanceof FiguraGeometrica) {
            FiguraGeometrica fig = (FiguraGeometrica) nodo;
            Shape sh = fig.forma.shape;

            AffineTransform at = new AffineTransform();
            at.scale(factor, factor);
            fig.forma.shape = at.createTransformedShape(sh);
        }
        reload.ejecutar(nodo, g);
    }
}

