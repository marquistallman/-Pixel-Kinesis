package com.mycompany.pixelkinesis.comandos;
import java.awt.*;
import java.awt.geom.*;
import com.mycompany.pixelkinesis.*;
import com.mycompany.pixelkinesis.ComposedFigures.ComposedFigures;

public class ComandoEscalar extends Comando {
    private final double factor;
    private final ComandoDibujar reload = new ComandoDibujar();
    
    // Flag para evitar aplicar la escala múltiples veces POR INSTANCIA
    private boolean yaAplicado = false;
    
    public ComandoEscalar(double factor) {
        this.factor = factor;
    }
    
    @Override
    public void ejecutar(Nodo nodo, Graphics2D g) {
        System.out.println("=== ComandoEscalar.ejecutar ===");
        System.out.println("Factor: " + factor);
        System.out.println("yaAplicado: " + yaAplicado);
        
        // Si ya se aplicó esta transformación, solo redibujar
        if (yaAplicado) {
            System.out.println("⚠️ Escala ya aplicada, solo redibujando");
            reload.ejecutar(nodo, g);
            return;
        }
        
        if (nodo instanceof FiguraGeometrica fig) {
            System.out.println("Escalando FiguraGeometrica...");
            escalarFigura(fig);
            yaAplicado = true;
            System.out.println("✅ Escala aplicada exitosamente");
            
            // Forzar repaint completo
            if (ComandoMover.refrescarUI != null) {
                ComandoMover.refrescarUI.run();
            }
            
            reload.ejecutar(nodo, g);
            return;
        }
        
        if (nodo instanceof ComposedFigures composed) {
            System.out.println("Escalando ComposedFigures...");
            
            for (FiguraGeometrica f : composed.getFiguras()) {
                escalarFigura(f);
            }
            
            // Recalculate composed area if needed
            composed.recalcularArea();
            yaAplicado = true;
            System.out.println("✅ ComposedFigures escalado exitosamente");
            
            // Forzar repaint completo
            if (ComandoMover.refrescarUI != null) {
                ComandoMover.refrescarUI.run();
            }
            
            reload.ejecutar(nodo, g);
            return;
        }
        
        yaAplicado = true;
        reload.ejecutar(nodo, g);
    }
    
    private void escalarFigura(FiguraGeometrica fig) {
        if (fig == null || fig.forma == null || fig.forma.shape == null) return;
        
        // ====================================================================
        // PASO 1: Guardar shapeOriginal si no existe
        // ====================================================================
        if (fig.forma.shapeOriginal == null) {
            // Guardar el shape original tal cual está (con su posición inicial)
            fig.forma.shapeOriginal = fig.forma.shape;
            System.out.println("✅ shapeOriginal guardado");
            
            // Inicializar area.posicion si no existe
            if (fig.area != null && fig.area.getPosicion() == null) {
                Rectangle2D bounds = fig.forma.shape.getBounds2D();
                fig.area.setPosicion(new Point((int)bounds.getX(), (int)bounds.getY()));
            }
        }
        
        // ====================================================================
        // PASO 2: Obtener posición actual y shape base SIN posición
        // ====================================================================
        // FUENTE DE VERDAD: area.getPosicion() siempre tiene la posición correcta
        Point posActual = new Point(0, 0);
        if (fig.area != null && fig.area.getPosicion() != null) {
            posActual = new Point(fig.area.getPosicion().x, fig.area.getPosicion().y);
        }
        
        System.out.println("Posición desde area.getPosicion(): (" + posActual.x + ", " + posActual.y + ")");
        
        // El shape puede estar en (0,0) o en su posición real
        Rectangle2D boundsActual = fig.forma.shape.getBounds2D();
        double offsetX = boundsActual.getX();
        double offsetY = boundsActual.getY();
        
        System.out.println("Offset del shape: (" + offsetX + ", " + offsetY + ")");
        
        // Remover cualquier offset que tenga el shape
        Shape shapeBase;
        if (Math.abs(offsetX) > 0.01 || Math.abs(offsetY) > 0.01) {
            AffineTransform atRemoverPos = AffineTransform.getTranslateInstance(-offsetX, -offsetY);
            shapeBase = atRemoverPos.createTransformedShape(fig.forma.shape);
        } else {
            shapeBase = fig.forma.shape;
        }
        
        // ====================================================================
        // PASO 3: Aplicar la escala sobre el centro del shape base
        // ====================================================================
        Rectangle2D boundsBase = shapeBase.getBounds2D();
        double centerX = boundsBase.getCenterX();
        double centerY = boundsBase.getCenterY();
        
        AffineTransform atEscalar = new AffineTransform();
        atEscalar.translate(centerX, centerY);
        atEscalar.scale(factor, factor);
        atEscalar.translate(-centerX, -centerY);
        
        Shape shapeEscalado = atEscalar.createTransformedShape(shapeBase);
        
        // ====================================================================
        // PASO 4: Aplicar la posición desde area.posicion (fuente de verdad)
        // ====================================================================
        AffineTransform atAplicarPos = AffineTransform.getTranslateInstance(
            posActual.x, posActual.y
        );
        fig.forma.shape = atAplicarPos.createTransformedShape(shapeEscalado);
        
        // NO es necesario actualizar area.posicion porque ya tiene el valor correcto
        System.out.println("✅ Shape transformado y reposicionado a: " + posActual);
    }
}
