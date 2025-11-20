package com.mycompany.pixelkinesis.UI;

import javax.swing.*;
import java.awt.*;
import com.mycompany.pixelkinesis.*;
public class PanelDibujo extends JPanel {
    private Capa capaPrincipal;
    public PanelDibujo() {
        setBackground(Color.WHITE);
        setDoubleBuffered(true);
    }
    public void setCapa(Capa capa) {
        this.capaPrincipal = capa;
        repaint();
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
}

