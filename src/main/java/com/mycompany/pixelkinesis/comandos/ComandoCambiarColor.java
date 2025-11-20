package com.mycompany.pixelkinesis.comandos;
import java.awt.*;
import com.mycompany.pixelkinesis.*;
public class ComandoCambiarColor extends Comando {
    private final Color nuevoColor;
    private ComandoDibujar reload = new ComandoDibujar();
    public ComandoCambiarColor(Color nuevoColor) {
        this.nuevoColor = nuevoColor;
    }

    @Override
    public void ejecutar(Nodo nodo, Graphics2D g) {
        // Cambiar color del nodo (si tu nodo tiene esta función)
        nodo.area.frente.setColor(nuevoColor);
        reload.ejecutar(nodo, g);
    }
}
