package com.mycompany.pixelkinesis.UI;

import javax.swing.*;
import java.awt.*;

public class PanelDibujo extends JPanel {

    public PanelDibujo() {
        setDoubleBuffered(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Aquí pintarás las figuras después
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.LIGHT_GRAY);
        g2.drawString("Aquí se dibujarán las figuras", 20, 20);
    }
}

