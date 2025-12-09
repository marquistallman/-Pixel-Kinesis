package com.mycompany.pixelkinesis.comandos;
import java.awt.*;
import com.mycompany.pixelkinesis.*;
import com.mycompany.pixelkinesis.ComposedFigures.ComposedFigures;

public class ComandoCambiarColor extends Comando {
    private final Color nuevoColor;
    private final ComandoDibujar reload = new ComandoDibujar();
    
    public ComandoCambiarColor(Color nuevoColor) {
        this.nuevoColor = nuevoColor;
    }
    
    @Override
    public void ejecutar(Nodo nodo, Graphics2D g) {
        if (nodo == null) return;
        
        // Asegurar que el nodo tenga area y frente inicializados
        if (nodo.area == null) {
            reload.ejecutar(nodo, g); // Inicializar area
        }
        
        if (nodo.area != null && nodo.area.frente == null) {
            reload.ejecutar(nodo, g); // Inicializar frente
        }
        
        // Si es un Composed, aplicar al area compuesta y a cada figura interna
        if (nodo instanceof ComposedFigures composed) {
            if (composed.area != null && composed.area.frente != null) {
                float opacidadActual = composed.area.frente.getOpacidad();
                composed.area.frente.setColor(nuevoColor);
                composed.area.frente.setOpacidad(opacidadActual); // Preservar opacidad
            }
            
            for (FiguraGeometrica f : composed.getFiguras()) {
                if (f.area != null && f.area.frente != null) {
                    float opacidadActual = f.area.frente.getOpacidad();
                    f.area.frente.setColor(nuevoColor);
                    f.area.frente.setOpacidad(opacidadActual); // Preservar opacidad
                }
            }
            
            // Forzar repaint completo
            if (ComandoMover.refrescarUI != null) {
                ComandoMover.refrescarUI.run();
            }
            
            reload.ejecutar(nodo, g);
            return;
        }
        
        // Cambiar color del nodo preservando la opacidad actual
        if (nodo.area != null && nodo.area.frente != null) {
            float opacidadActual = nodo.area.frente.getOpacidad();
            nodo.area.frente.setColor(nuevoColor);
            nodo.area.frente.setOpacidad(opacidadActual); // Preservar opacidad
        }
        
        // Forzar repaint completo
        if (ComandoMover.refrescarUI != null) {
            ComandoMover.refrescarUI.run();
        }
        
        reload.ejecutar(nodo, g);
    }
}
