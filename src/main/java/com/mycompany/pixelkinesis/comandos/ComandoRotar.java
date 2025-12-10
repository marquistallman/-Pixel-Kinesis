package com.mycompany.pixelkinesis.comandos;
import java.awt.*;
import java.awt.geom.*;
import com.mycompany.pixelkinesis.*;
import com.mycompany.pixelkinesis.ComposedFigures.ComposedFigures;
import java.util.HashSet;

public class ComandoRotar extends Comando {
    private final double grados;
    private final ComandoDibujar reload = new ComandoDibujar();
    
    // Guardar qué nodos ya fueron transformados por ESTA INSTANCIA
    private final HashSet<Nodo> nodosTransformados = new HashSet<>();
    
    public ComandoRotar(double grados) {
        this.grados = grados;
    }
    
    @Override
    public void ejecutar(Nodo nodo, Graphics2D g) {
        // Si YA transformamos ESTE nodo con ESTA instancia del comando, solo redibujar
        if (nodosTransformados.contains(nodo)) {
            reload.ejecutar(nodo, g);
            return;
        }
        
        if (nodo instanceof FiguraGeometrica fig) {
            rotarFigura(fig);
            nodosTransformados.add(nodo);
            
            // Forzar repaint completo
            if (ComandoMover.refrescarUI != null) {
                ComandoMover.refrescarUI.run();
            }
            
            reload.ejecutar(nodo, g);
            return;
        }
        
        if (nodo instanceof ComposedFigures composed) {
            for (FiguraGeometrica f : composed.getFiguras()) {
                rotarFigura(f);
            }
            
            // Recalcular el área compuesta
            composed.recalcularArea();
            nodosTransformados.add(nodo);
            
            // Forzar repaint completo
            if (ComandoMover.refrescarUI != null) {
                ComandoMover.refrescarUI.run();
            }
            
            reload.ejecutar(nodo, g);
            return;
        }
        
        // Marcar como procesado
        nodosTransformados.add(nodo);
        reload.ejecutar(nodo, g);
    }
    
    private void rotarFigura(FiguraGeometrica fig) {
        if (fig == null || fig.forma == null || fig.forma.shape == null) return;
        
        // ====================================================================
        // PASO 1: Guardar shapeOriginal si no existe
        // ====================================================================
        if (fig.forma.shapeOriginal == null) {
            // Guardar el shape original tal cual está (con su posición inicial)
            fig.forma.shapeOriginal = fig.forma.shape;
            
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
        
        // El shape puede estar:
        // 1. En (0,0) con posición en area.posicion (primera vez)
        // 2. En su posición real después de transformaciones previas
        Rectangle2D boundsActual = fig.forma.shape.getBounds2D();
        double offsetX = boundsActual.getX();
        double offsetY = boundsActual.getY();
        
        // Remover cualquier offset que tenga el shape
        Shape shapeBase;
        if (Math.abs(offsetX) > 0.01 || Math.abs(offsetY) > 0.01) {
            AffineTransform atRemoverPos = AffineTransform.getTranslateInstance(-offsetX, -offsetY);
            shapeBase = atRemoverPos.createTransformedShape(fig.forma.shape);
        } else {
            shapeBase = fig.forma.shape;
        }
        
        // ====================================================================
        // PASO 3: Aplicar la rotación sobre el centro del shape base
        // ====================================================================
        Rectangle2D boundsBase = shapeBase.getBounds2D();
        double centerX = boundsBase.getCenterX();
        double centerY = boundsBase.getCenterY();
        
        // Trasladar al origen, rotar, y volver al centro
        AffineTransform atRotar = new AffineTransform();
        atRotar.translate(centerX, centerY);
        atRotar.rotate(Math.toRadians(grados));
        atRotar.translate(-centerX, -centerY);
        
        Shape shapeRotado = atRotar.createTransformedShape(shapeBase);
        
        // ====================================================================
        // PASO 4: Aplicar la posición desde area.posicion (fuente de verdad)
        // ====================================================================
        AffineTransform atAplicarPos = AffineTransform.getTranslateInstance(
            posActual.x, posActual.y
        );
        fig.forma.shape = atAplicarPos.createTransformedShape(shapeRotado);
        
        // NO es necesario actualizar area.posicion porque ya tiene el valor correcto
    }
}
