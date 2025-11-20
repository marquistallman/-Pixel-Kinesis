package com.mycompany.pixelkinesis.comandos;
import java.awt.*;
import java.awt.geom.*;
import com.mycompany.pixelkinesis.*;
import java.util.ArrayList;
public class ComandoCambiarOpacidad extends Comando {
    private final float nuevaOpacidad;
    private final ComandoDibujar reload = new ComandoDibujar();

    public ComandoCambiarOpacidad(float opacidad) {
        this.nuevaOpacidad = Math.max(0f, Math.min(1f, opacidad));
    }

    @Override
    public void ejecutar(Nodo nodo, Graphics2D g) {
        nodo.area.frente.setOpacidad(nuevaOpacidad);
        reload.ejecutar(nodo, g);
    }
}

