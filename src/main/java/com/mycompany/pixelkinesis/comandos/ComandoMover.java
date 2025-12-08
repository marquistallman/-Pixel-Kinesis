package com.mycompany.pixelkinesis.comandos;
import java.awt.*;
import java.awt.geom.*;
import com.mycompany.pixelkinesis.*;
import com.mycompany.pixelkinesis.ComposedFigures.ComposedFigures;
import java.util.HashMap;

public class ComandoMover extends Comando {
    private final Point destino;
    private final ComandoDibujar reload = new ComandoDibujar();
    
    // Callback global para pedir repaint
    public static Runnable refrescarUI = null;
    
    // Guardar la última posición a la que movimos cada nodo
    // Esto evita que se aplique el mismo movimiento múltiples veces
    private final HashMap<Nodo, Point> ultimaPosicionAplicada = new HashMap<>();
    
    public ComandoMover(Point destino) {
        this.destino = destino;
    }
    
    @Override
    public void ejecutar(Nodo nodo, Graphics2D g) {
        if (nodo == null) return;
        
        // ====================================
        // VERIFICAR SI YA MOVIMOS A ESTE DESTINO
        // ====================================
        Point ultimaPos = ultimaPosicionAplicada.get(nodo);
        if (ultimaPos != null && ultimaPos.equals(destino)) {
            // Ya movimos este nodo a este destino con esta instancia del comando
            reload.ejecutar(nodo, g);
            return;
        }
        
        // ====================================
        // POSICIÓN ACTUAL
        // ====================================
        Point posActual = new Point(0, 0);
        if (nodo.area != null && nodo.area.getPosicion() != null) {
            Point ref = nodo.area.getPosicion();
            posActual = new Point(ref.x, ref.y); // Crear copia
        }
        
        // ====================================
        // CALCULAR DX, DY
        // ====================================
        int dx = destino.x - posActual.x;
        int dy = destino.y - posActual.y;
        
        // Si no hay movimiento, no hacer nada
        if (dx == 0 && dy == 0) {
            return;
        }
        
        // ====================================
        // MOVER SEGÚN EL TIPO DE NODO
        // ====================================
        
        // 1) Si es FiguraGeometrica, mover el shape
        if (nodo instanceof FiguraGeometrica fig) {
            moverFigura(fig, dx, dy);
            
            // Actualizar la posición en el área - CREAR NUEVA INSTANCIA
            if (fig.area != null) {
                fig.area.setPosicion(new Point(destino.x, destino.y));
            }
            
            // Guardar que ya movimos este nodo a este destino
            ultimaPosicionAplicada.put(nodo, new Point(destino.x, destino.y));
            
            // Forzar repaint
            if (refrescarUI != null) {
                refrescarUI.run();
            }
            
            reload.ejecutar(nodo, g);
            return;
        }
        
        // 2) Si es ComposedFigures, mover todas las figuras internas
        if (nodo instanceof ComposedFigures composed) {
            if (composed.getFiguras() != null) {
                for (FiguraGeometrica child : composed.getFiguras()) {
                    Point posChild = new Point(0, 0);
                    if (child.area != null && child.area.getPosicion() != null) {
                        Point ref = child.area.getPosicion();
                        posChild = new Point(ref.x, ref.y);
                    }
                    
                    // Aplicar desplazamiento relativo
                    Point nuevaPos = new Point(posChild.x + dx, posChild.y + dy);
                    new ComandoMover(nuevaPos).ejecutar(child, g);
                }
            }
            
            // Actualizar la posición del composed
            if (composed.area != null) {
                composed.area.setPosicion(new Point(destino.x, destino.y));
            }
            
            // Recalcular el área compuesta
            composed.recalcularArea();
            
            // Guardar que ya movimos este nodo
            ultimaPosicionAplicada.put(nodo, new Point(destino.x, destino.y));
            
            // Forzar repaint
            if (refrescarUI != null) {
                refrescarUI.run();
            }
            
            reload.ejecutar(nodo, g);
            return;
        }
        
        // 3) Si es Capa, mover todos los hijos
        if (nodo instanceof Capa capa) {
            // Actualizar la posición de la capa
            if (capa.area != null) {
                capa.area.setPosicion(new Point(destino.x, destino.y));
            }
            
            // Mover los hijos
            if (capa.hijos != null) {
                for (Nodo child : capa.hijos) {
                    Point posChild = new Point(0, 0);
                    if (child.area != null && child.area.getPosicion() != null) {
                        Point ref = child.area.getPosicion();
                        posChild = new Point(ref.x, ref.y);
                    }
                    
                    // Aplicar desplazamiento relativo
                    Point nuevaPos = new Point(posChild.x + dx, posChild.y + dy);
                    new ComandoMover(nuevaPos).ejecutar(child, g);
                }
            }
            
            // Guardar que ya movimos este nodo
            ultimaPosicionAplicada.put(nodo, new Point(destino.x, destino.y));
            
            // Forzar repaint
            if (refrescarUI != null) {
                refrescarUI.run();
            }
            
            reload.ejecutar(nodo, g);
            return;
        }
        
        // 4) Para otros tipos de nodos, solo actualizar la posición
        if (nodo.area != null) {
            nodo.area.setPosicion(new Point(destino.x, destino.y));
        }
        
        // Guardar que ya movimos este nodo
        ultimaPosicionAplicada.put(nodo, new Point(destino.x, destino.y));
        
        // Forzar repaint
        if (refrescarUI != null) {
            refrescarUI.run();
        }
        
        reload.ejecutar(nodo, g);
    }
    
    /**
     * Mueve una FiguraGeometrica aplicando el desplazamiento al shape
     */
    private void moverFigura(FiguraGeometrica fig, int dx, int dy) {
        if (fig == null || fig.forma == null || fig.forma.shape == null) return;
        
        // Si la figura tiene transformaciones aplicadas (shapeOriginal existe)
        if (fig.forma.shapeOriginal != null) {
            // La figura ya fue transformada (rotada, escalada)
            // Obtener la posición actual del área (fuente de verdad)
            Point posActual = new Point(0, 0);
            if (fig.area != null && fig.area.getPosicion() != null) {
                Point ref = fig.area.getPosicion();
                posActual = new Point(ref.x, ref.y);
            }
            
            // Calcular la nueva posición
            Point nuevaPos = new Point(posActual.x + dx, posActual.y + dy);
            
            // Remover la posición actual del shape usando area.getPosicion()
            AffineTransform atInversa = AffineTransform.getTranslateInstance(
                -posActual.x, -posActual.y
            );
            Shape shapeBase = atInversa.createTransformedShape(fig.forma.shape);
            
            // Aplicar la nueva posición
            AffineTransform atNueva = AffineTransform.getTranslateInstance(
                nuevaPos.x, nuevaPos.y
            );
            fig.forma.shape = atNueva.createTransformedShape(shapeBase);
            
        } else {
            // Figura sin transformaciones previas, mover directamente
            AffineTransform at = AffineTransform.getTranslateInstance(dx, dy);
            fig.forma.shape = at.createTransformedShape(fig.forma.shape);
        }
    }
}






