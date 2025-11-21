package com.mycompany.pixelkinesis.comandos;
import java.awt.*;
import java.awt.geom.*;
import com.mycompany.pixelkinesis.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class ComandoMover extends Comando {

    private final Point destino;

    // No hacemos repaint desde aquí: que lo haga la UI con panelDibujo.repaint()
    public ComandoMover(Point destino) {
        this.destino = destino;
    }

    @Override
    public void ejecutar(Nodo nodo, Graphics2D g) {
        if (nodo == null) return;

        // POSICIÓN ACTUAL - segura
        Point posActual = new Point(0,0);
        if (nodo.area != null && nodo.area.getPosicion() != null) {
            posActual = nodo.area.getPosicion();
        }

        // calcular desplazamiento
        int dx = destino.x - posActual.x;
        int dy = destino.y - posActual.y;

        // =======================
        // 1) actualizar area.posicion
        // =======================
        if (nodo.area != null) {
            nodo.area.setPosicion(new Point(destino.x, destino.y));
        }

        // =======================
        // 2) actualizar la shape real de la figura (si existe)
        // =======================
        if (nodo instanceof FiguraGeometrica) {
            FiguraGeometrica fig = (FiguraGeometrica) nodo;

            if (fig.forma != null && fig.forma.getShape() != null) {
                AffineTransform at = AffineTransform.getTranslateInstance(dx, dy);
                fig.forma.shape = at.createTransformedShape(fig.forma.getShape());
            }

            // SINCRONIZAR: asegurar que area.forma apunte a la misma instancia
            if (nodo.area != null) {
                nodo.area.forma = fig.forma;
            }
        }

        // =======================
        // 3) mover hijos (si es capa) — hacerlo robusto y no crear shapes nuevas
        // =======================
        if (nodo instanceof Capa) {
            Capa capa = (Capa) nodo;
            if (capa.hijos != null) {
                for (Nodo child : capa.hijos) {
                    // si el hijo tiene posicion, lo actualizamos de forma relativa
                    Point childPos = (child.area != null && child.area.getPosicion() != null)
                                     ? child.area.getPosicion()
                                     : new Point(0,0);
                    Point nuevo = new Point(childPos.x + dx, childPos.y + dy);
                    // reusar el mismo comando para mover al hijo
                    new ComandoMover(nuevo).ejecutar(child, g);
                }
            }
        }

        // NOTA: no llamamos a reload.ejecutar(nodo,g) ni a g.draw aquí.
        // Es más seguro que la UI haga panelDibujo.repaint() después de ejecutar todo el lote.
    }
}



