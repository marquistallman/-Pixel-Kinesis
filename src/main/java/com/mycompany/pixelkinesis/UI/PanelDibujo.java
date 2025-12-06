package com.mycompany.pixelkinesis.UI;

import javax.swing.*;
import java.awt.*;
import com.mycompany.pixelkinesis.*;
import com.mycompany.pixelkinesis.comandos.ComandoMover;
public class PanelDibujo extends JPanel {
    private Capa capaPrincipal;
    public PanelDibujo() {
        setBackground(Color.WHITE);
        setDoubleBuffered(true);
    
        // Registrar callback para que ComandoMover pida repaint en el EDT
        ComandoMover.refrescarUI = () -> {
            // forzamos repaint en EDT
            SwingUtilities.invokeLater(() -> {
                this.revalidate(); // por si acaso
                this.repaint();
            });
        };
    }
    
    public void setCapa(Capa capa) {
        this.capaPrincipal = capa;
        repaint();
    }
    public Capa getCapa() {
        return capaPrincipal;
    }
    @Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;

    // Opcional: fondo blanco
    g2.setColor(Color.WHITE);
    g2.fillRect(0, 0, getWidth(), getHeight());

    // =========================
    // DIBUJAR LA CAPA PRINCIPAL
    // =========================
    if (capaPrincipal != null) {
        capaPrincipal.ejecutar(g2);  // aquí se dibujan todos los nodos y comandos
    }
}
public void refrescar() {
    repaint();
}

}

