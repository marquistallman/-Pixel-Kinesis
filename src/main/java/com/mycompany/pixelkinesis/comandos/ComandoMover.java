package com.mycompany.pixelkinesis.comandos;
import java.awt.*;
import java.awt.geom.*;
import com.mycompany.pixelkinesis.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class ComandoMover extends Comando {
    private final int dx, dy;
    private final ComandoDibujar reload = new ComandoDibujar();

    public ComandoMover(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public void ejecutar(Nodo nodo, Graphics2D g) {
        if (nodo == null) return;

        // 1) Actualiza la posicion guardada en el area (si existe)
        if (nodo.area != null) {
            Point pos = nodo.area.getPosicion();
            if (pos == null) pos = new Point(0, 0);
            nodo.area.setPosicion(new Point(pos.x + dx, pos.y + dy));
        }

        // 2) Si es figura y ya tiene una Forma con Shape -> trasladar el Shape
        if (nodo instanceof FiguraGeometrica) {
            FiguraGeometrica fig = (FiguraGeometrica) nodo;
            if (fig.forma != null && fig.forma.shape != null) {
                // Trasladar la shape existente
                AffineTransform at = AffineTransform.getTranslateInstance(dx, dy);
                fig.forma.shape = at.createTransformedShape(fig.forma.shape);
            }
        }

        // 3) (Opcional) mover hijos también si quieres arrastrar grupos
        // if (nodo.hijos != null) {
        //     for (Nodo child : nodo.hijos) {
        //         new ComandoMover(dx, dy).ejecutar(child, g);
        //     }
        // }

        // 4) Forzar re-draw
        reload.ejecutar(nodo, g);
    }
}


