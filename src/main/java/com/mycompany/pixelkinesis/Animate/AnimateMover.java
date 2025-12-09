package com.mycompany.pixelkinesis.Animate;
import java.awt.Point;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.Shape;
import com.mycompany.pixelkinesis.Nodo;
import com.mycompany.pixelkinesis.FiguraGeometrica;
import com.mycompany.pixelkinesis.comandos.ComandoMover;
import javax.swing.JPanel;
import javax.swing.Timer;

public class AnimateMover extends Animate {

    private final Point destino;
    private final JPanel panel;
    private Timer timer;
    private boolean ejecutado = false;

    public AnimateMover(Point inicio, Point destino, int timeMs, int speed, JPanel panel) {
        this.destino = destino;
        this.timeStart = 0;
        this.timeEnd = timeMs;
        this.speed = speed;
        this.panel = panel;
    }

    @Override
    public void ejecutar(Nodo nodo, Graphics2D g) {
        // Si ya se ejecutó, no hacer nada
        if (ejecutado) {
            return;
        }
        
        // Si ya hay un timer ejecutándose, detenerlo primero
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        
        // Validaciones iniciales
        if (nodo == null || nodo.area == null) {
            System.err.println("❌ Error: nodo o nodo.area es null en AnimateMover");
            return;
        }
        
        System.out.println("🎬 Iniciando animación: destino=(" + destino.x + "," + destino.y + 
            "), tiempo=" + timeEnd + "ms, speed=" + speed);
        
        ejecutado = true;

        // Obtener posición inicial REAL del shape
        Point posicionInicial = obtenerPosicionReal(nodo);
        System.out.println("📍 Posición inicial: (" + posicionInicial.x + "," + posicionInicial.y + ")");

        int framesCalculados = (timeEnd / speed);
        final int frames = (framesCalculados <= 0) ? 1 : framesCalculados;

        final int[] i = {0};

        // Crear el timer
        timer = new Timer(speed, e -> {
            if (nodo == null || nodo.area == null) {
                System.err.println("❌ Error: nodo o área se volvieron null durante la animación");
                ((Timer)e.getSource()).stop();
                return;
            }

            double t = (frames > 1) ? (double) i[0] / (frames - 1) : 1.0;

            int x = (int)(posicionInicial.x + (destino.x - posicionInicial.x) * t);
            int y = (int)(posicionInicial.y + (destino.y - posicionInicial.y) * t);

            try {
                // Actualizar TANTO el área como el shape
                actualizarPosicion(nodo, x, y);
                
                if (i[0] % 10 == 0) {
                    System.out.println("📍 Frame " + i[0] + "/" + frames + ": posición=(" + x + "," + y + ")");
                }
            } catch (Exception ex) {
                System.err.println("❌ Error al establecer posición: " + ex.getMessage());
                ex.printStackTrace();
                ((Timer)e.getSource()).stop();
                return;
            }

            panel.repaint();

            i[0]++;

            if (i[0] >= frames) {
                ((Timer)e.getSource()).stop();
                System.out.println("✅ Animación completada");

                try {
                    // Aplicar movimiento final para asegurar la posición exacta
                    new ComandoMover(destino).ejecutar(nodo, null);
                } catch (Exception ex) {
                    System.err.println("❌ Error al aplicar movimiento final: " + ex.getMessage());
                }

                panel.repaint();
            }
        });

        timer.start();
        System.out.println("⏱️ Timer iniciado: " + frames + " frames, delay=" + speed + "ms");
    }
    
    /**
     * Obtiene la posición REAL de un nodo (donde realmente se dibuja)
     */
    private Point obtenerPosicionReal(Nodo nodo) {
        if (nodo instanceof FiguraGeometrica fig) {
            if (fig.forma != null && fig.forma.shape != null) {
                Rectangle2D bounds = fig.forma.shape.getBounds2D();
                return new Point((int)bounds.getX(), (int)bounds.getY());
            }
        }
        
        // Fallback: usar area.getPosicion()
        if (nodo.area != null && nodo.area.getPosicion() != null) {
            return new Point(nodo.area.getPosicion().x, nodo.area.getPosicion().y);
        }
        
        return new Point(0, 0);
    }
    
    /**
     * Actualiza la posición de un nodo, sincronizando tanto el área como el shape
     */
    private void actualizarPosicion(Nodo nodo, int x, int y) {
        if (!(nodo instanceof FiguraGeometrica fig)) {
            // Para nodos que no son figuras, solo actualizar area
            nodo.area.setPosicion(new Point(x, y));
            return;
        }
        
        if (fig.forma == null || fig.forma.shape == null) {
            nodo.area.setPosicion(new Point(x, y));
            return;
        }
        
        // Obtener la posición actual del shape
        Rectangle2D boundsActual = fig.forma.shape.getBounds2D();
        double offsetXActual = boundsActual.getX();
        double offsetYActual = boundsActual.getY();
        
        // Calcular el desplazamiento necesario
        double dx = x - offsetXActual;
        double dy = y - offsetYActual;
        
        // Aplicar el desplazamiento al shape
        AffineTransform at = AffineTransform.getTranslateInstance(dx, dy);
        fig.forma.shape = at.createTransformedShape(fig.forma.shape);
        
        // Sincronizar area.posicion
        nodo.area.setPosicion(new Point(x, y));
    }
}



