package com.mycompany.pixelkinesis.comandos;
import java.awt.*;
import java.awt.geom.*;
import com.mycompany.pixelkinesis.*;
import java.util.ArrayList;
public class ComandoDibujar extends Comando {
    @Override
    public void ejecutar(Nodo nodo, Graphics2D g) {
        if (nodo == null || g == null) return;

        boolean esCapa = nodo instanceof Capa;
        boolean esFigura = nodo instanceof FiguraGeometrica;

        Forma forma = null;

        if (esCapa) {
            Rectangle clip = g.getClipBounds();
            if (clip == null) clip = new Rectangle(0, 0, 800, 600);

            forma = new Forma(
                new Rectangle2D.Double(0, 0, clip.width, clip.height)
            );
        }

        if (esFigura) {
            FiguraGeometrica fig = (FiguraGeometrica) nodo;

            if (fig.forma != null && fig.forma.shape != null) {
                forma = fig.forma;
            } else {
                Point pos = (nodo.area != null && nodo.area.getPosicion() != null)
                        ? nodo.area.getPosicion()
                        : new Point(0, 0);

                forma = new Forma(new Rectangle2D.Double(pos.x, pos.y, 100, 100));
                fig.forma = forma;
            }
        }

        if (nodo.area == null) {
            nodo.area = new AreaDeInfluencia(forma);
        }

        if (nodo.area.fondo == null) {
            nodo.area.fondo = new Fondo(new Color(0, 0, 0, 0), 0.0f);
        }

        if (nodo.area.frente == null) {
            if (esCapa) {
                Capa capa = (Capa) nodo;
                nodo.area.frente = new Frente(capa.hijos);
            } else if (esFigura) {
                ArrayList<Nodo> lista = new ArrayList<>();
                lista.add(nodo);
                nodo.area.frente = new Frente(lista);
            }

            nodo.area.frente.setColor(Color.GRAY);
            nodo.area.frente.setOpacidad(1.0f);
        }

        Color prev = g.getColor();
        g.setColor(nodo.area.frente.color);

        Shape shape = forma.getShape();

        // ============================================
        // 🔥 FIX: Aplicar la posición almacenada en el nodo
        // ============================================
        if (nodo.area.getPosicion() != null) {
            Point p = nodo.area.getPosicion();
            AffineTransform at = AffineTransform.getTranslateInstance(p.x, p.y);
            shape = at.createTransformedShape(shape);
        }
        // ============================================

        if (shape instanceof java.awt.geom.Line2D ||
            shape instanceof java.awt.geom.QuadCurve2D ||
            shape instanceof java.awt.geom.CubicCurve2D) {

            g.draw(shape);

        } else {
            g.fill(shape);
        }

        g.setColor(prev);

        // Comentado temporalmente para reducir ruido en la consola
        // System.out.println("Dibujado nodo=" + nodo + " frente=" + nodo.area.frente);
    }
}


