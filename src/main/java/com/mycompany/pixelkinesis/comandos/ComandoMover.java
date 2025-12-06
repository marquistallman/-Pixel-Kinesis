package com.mycompany.pixelkinesis.comandos;
import java.awt.*;
import java.awt.geom.*;
import com.mycompany.pixelkinesis.*;
import java.util.ArrayList;
import java.awt.*;
import com.mycompany.pixelkinesis.UI.*;
import java.awt.geom.AffineTransform;

public class ComandoMover extends Comando {

    private final Point destino;

    // Callback global para pedir repaint
    public static Runnable refrescarUI = null;

    public ComandoMover(Point destino) {
        this.destino = destino;
    }

    @Override
    public void ejecutar(Nodo nodo, Graphics2D g) {
        if (nodo == null) return;

        // ====================================
        // POSICIÓN ACTUAL
        // ====================================
        Point posActual = new Point(0, 0);
        if (nodo.area != null && nodo.area.getPosicion() != null) {
            posActual = nodo.area.getPosicion();
        }

        // ====================================
        // CALCULAR DX, DY  (corregido)
        // ====================================
        int dx = destino.x - posActual.x;
        int dy = destino.y - posActual.y;

        // ====================================
        // 1) ACTUALIZAR SOLO LA POSICIÓN
        // ====================================
        if (nodo.area != null) {
            nodo.area.setPosicion(new Point(destino.x, destino.y));
        }

        // ====================================
        // 2) MOVER LOS HIJOS SI ES CAPA
        // ====================================
        if (nodo instanceof Capa capa && capa.hijos != null) {

            for (Nodo child : capa.hijos) {

                Point posChild = new Point(0, 0);
                if (child.area != null && child.area.getPosicion() != null) {
                    posChild = child.area.getPosicion();
                }

                // aplicar desplazamiento relativo
                Point nuevo = new Point(posChild.x + dx, posChild.y + dy);

                new ComandoMover(nuevo).ejecutar(child, g);
            }
        }

        // ====================================
        // 3) FORZAR REPAINT
        // ====================================
        if (refrescarUI != null) {
            refrescarUI.run();
        }
    }
}






