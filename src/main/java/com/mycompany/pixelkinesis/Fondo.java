package com.mycompany.pixelkinesis;
import java.awt.Color;
public class Fondo {
    public Color color;
    public float opacidad; // 0.0f = totalmente transparente, 1.0f = opaco

    public Fondo(Color color, float opacidad) {
        setColor(color);
        setOpacidad(opacidad);
    }

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

    /**
     * Devuelve el color con la opacidad aplicada usando canal alfa ARGB.
     */
    public Color getColorConOpacidad() {
        int alpha = (int) (opacidad * 255);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
}
