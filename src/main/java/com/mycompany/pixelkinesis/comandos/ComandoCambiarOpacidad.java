package com.mycompany.pixelkinesis.comandos;
import java.awt.*;
import java.awt.geom.*;
import com.mycompany.pixelkinesis.*;
import com.mycompany.pixelkinesis.ComposedFigures.ComposedFigures;
public class ComandoCambiarOpacidad extends Comando {
    private final float nuevaOpacidad;
    private final ComandoDibujar reload = new ComandoDibujar();

    public ComandoCambiarOpacidad(float opacidad) {
        this.nuevaOpacidad = Math.max(0f, Math.min(1f, opacidad));
    }

    @Override
    public void ejecutar(Nodo nodo, Graphics2D g) {
        if (nodo instanceof ComposedFigures composed) {
            if (composed.area != null && composed.area.frente != null) {
                composed.area.frente.setOpacidad(nuevaOpacidad);
            }
            for (FiguraGeometrica f : composed.getFiguras()) {
                if (f.area != null && f.area.frente != null) f.area.frente.setOpacidad(nuevaOpacidad);
            }
            reload.ejecutar(nodo, g);
            return;
        }

        if (nodo.area != null && nodo.area.frente != null) nodo.area.frente.setOpacidad(nuevaOpacidad);
        reload.ejecutar(nodo, g);
    }
}

