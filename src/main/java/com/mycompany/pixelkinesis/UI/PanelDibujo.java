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
        
        setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        ComandoMover.refrescarUI = () -> {
            SwingUtilities.invokeLater(() -> {
                this.revalidate(); 
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

        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, getWidth(), getHeight());

        drawGrid(g2);
        
        if (capaPrincipal != null) {
            capaPrincipal.ejecutar(g2);  
        }
    }
    
    private void drawGrid(Graphics2D g2) {
        int width = getWidth();
        int height = getHeight();
        int gridSize = 50; 

        g2.setColor(new Color(220, 220, 220)); 

        for (int x = gridSize; x < width; x += gridSize) {
            g2.drawLine(x, 0, x, height);
        }

        for (int y = gridSize; y < height; y += gridSize) {
            g2.drawLine(0, y, width, y);
        }
    }
    
    public void refrescar() {
        repaint();
    }
}