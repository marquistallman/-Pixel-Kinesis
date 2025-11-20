package com.mycompany.pixelkinesis;
import java.awt.Point;
public class AreaDeInfluencia {
    // =======================
    //      ATRIBUTOS
    // =======================

    private Point posicion;  // privada según lo pedido
    public Forma forma;      // clase futura que representa la geometría
    public Fondo fondo;
    public Frente frente;

    public float opacidad;   // opacidad general del área (pública)


    // =======================
    //     CONSTRUCTORES
    // =======================

    public AreaDeInfluencia(Forma forma) {
        if (forma == null) {
            throw new IllegalArgumentException("La forma no puede ser null.");
        }

        this.forma = forma;
        this.posicion = new Point(0, 0); // posición por defecto
        this.fondo = null;   // se asignarán luego
        this.frente = null;
        this.opacidad = 1.0f;
    }

    public AreaDeInfluencia(Forma forma, Point posicion) {
        if (forma == null) {
            throw new IllegalArgumentException("La forma no puede ser null.");
        }
        if (posicion == null) {
            throw new IllegalArgumentException("La posición no puede ser null.");
        }

        this.forma = forma;
        this.posicion = posicion;
        this.fondo = null;
        this.frente = null;
        this.opacidad = 1.0f;
    }


    // =======================
    //       GETTERS
    // =======================

    public Point getPosicion() {
        return posicion;
    }


    // =======================
    //        SETTERS
    // =======================

    public void setPosicion(Point nuevaPosicion) {
        if (nuevaPosicion == null) {
            throw new IllegalArgumentException("La posición no puede ser null.");
        }
        this.posicion = nuevaPosicion;
    }


    // =======================
    //     MÉTODO PRINCIPAL
    // =======================

    /**
     * Aplica transformaciones al área de influencia.
     * La lógica dependerá de qué tipo de transformaciones quieras permitir
     * (traslación, rotación, escala, deformación, etc.)
     */
    public void transform() {
        // Aquí luego definirás la transformación real
        // Por ahora se deja como método placeholder
    }
}
