package com.mycompany.pixelkinesis.comandos;
import java.awt.*;
import java.awt.geom.*;
import com.mycompany.pixelkinesis.*;
import java.util.ArrayList;
public class ComandoDibujar extends Comando {
    @Override
    public void ejecutar(Nodo nodo, Graphics2D g) {
        if (nodo == null || g == null) return;

        // ============================================
        // 1. CLASIFICACIÓN DEL NODO
        // ============================================
        boolean esCapa = nodo instanceof Capa;
        boolean esFigura = nodo instanceof FiguraGeometrica;

        // ============================================
        // 2. CREAR FORMA SEGÚN EL TIPO
        // ============================================
        Forma forma = null;

        if (esCapa) {
            // Capa ocupa toda la pantalla
            Rectangle clip = g.getClipBounds();
            if (clip == null) clip = new Rectangle(0, 0, 800, 600);

            forma = new Forma(
                new Rectangle2D.Double(0, 0, clip.width, clip.height)
            );
        }

        if (esFigura) {
            FiguraGeometrica fig = (FiguraGeometrica) nodo;

            // Si ya tiene forma, la usamos
            if (fig.forma != null) {
                forma = fig.forma;
            } else {
                // Forma por defecto 100x100 en la posición del nodo
                Point pos = new Point(0, 0);
                if (nodo.area != null && nodo.area.getPosicion() != null) {
                    pos = nodo.area.getPosicion();
                }

                forma = new Forma(new Rectangle2D.Double(pos.x, pos.y, 100, 100));

                fig.forma = forma;
            }
        }

        // ============================================
        // 3. CREAR EL ÁREA DE INFLUENCIA SEGÚN LA FORMA
        // ============================================
        if (nodo.area == null) {
            nodo.area = new AreaDeInfluencia(forma);
        } else {
            nodo.area.forma = forma;
        }

        // ============================================
        // 4. CREAR FONDO DEFAULT (transparente)
        // ============================================
        if (nodo.area.fondo == null) {
            nodo.area.fondo = new Fondo(new Color(0,0,0,0), 0.0f);
        }

        // ============================================
        // 5. CREAR FRENTE DEFAULT
        // ============================================
        if (nodo.area.frente == null) {

            if (esCapa) {
                // Frente de capa usa los hijos de la capa
                Capa capa = (Capa) nodo;
                nodo.area.frente = new Frente(capa.hijos);
            } 
            else if (esFigura) {
                // Frente de figura incluye solo a la figura
                ArrayList<Nodo> lista = new ArrayList<>();
                lista.add(nodo);
                nodo.area.frente = new Frente(lista);
            }

            // Valores default
            nodo.area.frente.setColor(Color.GRAY);
            nodo.area.frente.setOpacidad(1.0f);
        }

        // ============================================
        // 6. DIBUJAR (relleno directo)
        // ============================================
        Color prev = g.getColor();
        g.setColor(nodo.area.frente.color);

        Shape shape = forma.getShape();

            if (shape instanceof java.awt.geom.Line2D ||
                shape instanceof java.awt.geom.QuadCurve2D ||
                    shape instanceof java.awt.geom.CubicCurve2D) {

            // Las líneas y curvas NO tienen área → se dibujan con draw()
            g.draw(shape);

            } else {

            // Figuras con área → se dibujan con fill()
            g.fill(shape);
            }


        g.setColor(prev);

        System.out.println("Dibujado nodo=" + nodo + " frente=" + nodo.area.frente);
    }
}

