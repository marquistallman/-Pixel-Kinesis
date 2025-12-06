package com.mycompany.pixelkinesis.Animate;
import java.awt.Point;
import java.awt.Graphics2D;
import com.mycompany.pixelkinesis.Nodo;
import com.mycompany.pixelkinesis.comandos.ComandoMover;
import javax.swing.JPanel;
import javax.swing.Timer;
public class AnimateMover extends Animate {

    private final Point destino;
    private final JPanel panel;
    private Timer timer;  // Guardar referencia al timer para evitar múltiples ejecuciones
    private boolean ejecutado = false;  // Flag para ejecutar solo una vez

    public AnimateMover(Point inicio, Point destino, int timeMs, int speed, JPanel panel) {
        // inicio se calcula dinámicamente en ejecutar(), no se guarda
        this.destino = destino;
        this.timeStart = 0;
        this.timeEnd = timeMs;
        this.speed = speed;
        this.panel = panel;
    }

    @Override
    public void ejecutar(Nodo nodo, Graphics2D g) {
        // Si ya se ejecutó, no hacer nada (evitar ejecuciones múltiples durante repaint)
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
        
        System.out.println("🎬 Iniciando animación: destino=(" + destino.x + "," + destino.y + "), tiempo=" + timeEnd + "ms, speed=" + speed);
        
        ejecutado = true;  // Marcar como ejecutado INMEDIATAMENTE para evitar ejecuciones múltiples

        // Obtener posición inicial de forma segura (SIEMPRE recalcular, no usar la variable de instancia)
        Point posActual = nodo.area.getPosicion();
        Point posicionInicial;
        
        if (posActual != null && (posActual.x != 0 || posActual.y != 0)) {
            // Si hay una posición establecida y no es (0,0), usarla
            posicionInicial = new Point(posActual);
            System.out.println("📍 Posición inicial desde getPosicion(): (" + posicionInicial.x + "," + posicionInicial.y + ")");
        } else if (nodo.area.forma != null && nodo.area.forma.shape != null) {
            // Usar los bounds de la forma (la posición real donde se dibuja)
            java.awt.Rectangle bounds = nodo.area.forma.shape.getBounds();
            posicionInicial = new Point(bounds.x, bounds.y);
            System.out.println("📍 Posición inicial desde bounds de forma: (" + posicionInicial.x + "," + posicionInicial.y + ")");
        } else {
            // Si no hay forma, usar (0, 0)
            posicionInicial = new Point(0, 0);
            System.out.println("📍 Posición inicial por defecto: (0, 0)");
        }
        
        // Usar la posición inicial calculada (no la variable de instancia)
        final Point inicioFinal = new Point(posicionInicial);

        int framesCalculados = (timeEnd / speed);
        final int frames = (framesCalculados <= 0) ? 1 : framesCalculados;

        final int[] i = {0};

        // Crear el timer solo una vez
        timer = new Timer(speed, e -> {
            // Verificar que el nodo y su área sigan siendo válidos
            if (nodo == null || nodo.area == null) {
                System.err.println("❌ Error: nodo o área se volvieron null durante la animación");
                ((Timer)e.getSource()).stop();
                return;
            }

            // Evitar división por cero cuando frames es 1
            double t = (frames > 1) ? (double) i[0] / (frames - 1) : 1.0;

            int x = (int)(inicioFinal.x + (destino.x - inicioFinal.x) * t);
            int y = (int)(inicioFinal.y + (destino.y - inicioFinal.y) * t);

            // mover nodo
            try {
                nodo.area.setPosicion(new Point(x, y));
                if (i[0] % 10 == 0) {  // Log cada 10 frames para no saturar
                    System.out.println("📍 Frame " + i[0] + "/" + frames + ": posición=(" + x + "," + y + ")");
                }
            } catch (Exception ex) {
                System.err.println("❌ Error al establecer posición: " + ex.getMessage());
                ((Timer)e.getSource()).stop();
                return;
            }

            panel.repaint();

            i[0]++;

            if (i[0] >= frames) {
                ((Timer)e.getSource()).stop();
                System.out.println("✅ Animación completada");

                // aplicar movimiento final
                try {
                    // ComandoMover no usa Graphics2D directamente, así que null es seguro
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
}



