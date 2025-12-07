package com.mycompany.pixelkinesis.comandos;
import java.awt.*;
import com.mycompany.pixelkinesis.*;
import com.mycompany.pixelkinesis.ComposedFigures.ComposedFigures;
public class ComandoCambiarColor extends Comando {
    private final Color nuevoColor;
    private ComandoDibujar reload = new ComandoDibujar();
    public ComandoCambiarColor(Color nuevoColor) {
        this.nuevoColor = nuevoColor;
    }

    @Override
    public void ejecutar(Nodo nodo, Graphics2D g) {
        // Si es un Composed, aplicar al area compuesta y a cada figura interna
        if (nodo instanceof ComposedFigures composed) {
            if (composed.area != null && composed.area.frente != null) {
                composed.area.frente.setColor(nuevoColor);
            }
            for (FiguraGeometrica f : composed.getFiguras()) {
                if (f.area != null && f.area.frente != null) f.area.frente.setColor(nuevoColor);
            }
            reload.ejecutar(nodo, g);
            return;
        }

        // Cambiar color del nodo (si tu nodo tiene esta función)
        if (nodo.area != null && nodo.area.frente != null) nodo.area.frente.setColor(nuevoColor);
        reload.ejecutar(nodo, g);
    }
}
