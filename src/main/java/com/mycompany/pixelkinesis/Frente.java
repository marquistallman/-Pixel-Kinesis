package com.mycompany.pixelkinesis;
import java.awt.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Frente {
    public Color color;
    public float opacidad; // 0.0f - 1.0f
    public ArrayList<Nodo> nodos;
    Graphics2D g;
    public Frente(ArrayList<Nodo> nodos) {
        if (nodos == null) {
            throw new IllegalArgumentException("La lista de nodos no puede ser null.");
        }

        this.nodos = nodos;

        this.color = Color.BLACK;  
        this.opacidad = 1.0f;
    }

    // =======================
    //        SETTERS
    // =======================

    public void setColor(Color color) {
        if (color == null) {
            throw new IllegalArgumentException("El color no puede ser null.");
        }
        this.color = color;
    }

    public void setOpacidad(float opacidad) {
        if (opacidad < 0.0f || opacidad > 1.0f) {
            throw new IllegalArgumentException("La opacidad debe estar entre 0.0 y 1.0.");
        }
        this.opacidad = opacidad;
    }

    // =======================
    //        DRAW
    // =======================

    /**
     * Dibuja todos los nodos aplicando el color y opacidad del frente.
     */
    public void draw(Graphics2D g) {
        // Construir color con opacidad incluida
        int alpha = (int) (opacidad * 255);
        Color aplicado = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);

        // Guardar color previo
        Color previo = g.getColor();
        g.setColor(aplicado);

        // Dibujar cada nodo del frente
        for (Nodo n : nodos) {
            if (n != null) {
                n.ejecutar(g);  // Cada nodo se dibuja con el color del frente
            }
        }

        // Restaurar color previo
        g.setColor(previo);
    }
}
