package com.mycompany.pixelkinesis.Compiler;
import java.util.ArrayList;
import java.awt.*;
import java.awt.geom.*;
import com.mycompany.pixelkinesis.Forma;
public class CompilerCreate {

    // ==========================
    //   MÉTODO PRINCIPAL
    // ==========================
    public static Forma crearForma(String comando, ArrayList<String> params) {

        // Convertir params "X,Y" → new Point(X,Y)
        ArrayList<Point> puntos = new ArrayList<>();

        for (String s : params) {
            String[] sp = s.split(",");
            int x = Integer.parseInt(sp[0].trim());
            int y = Integer.parseInt(sp[1].trim());
            puntos.add(new Point(x, y));
        }

        // Igual que el original → siempre llegar a 4 puntos
        while (puntos.size() < 4)
            puntos.add(new Point(0, 0));

        return crearSegunTipo(comando, puntos);
    }


    // ==========================
    //     RUTEO DE FORMAS
    // ==========================
    private static Forma crearSegunTipo(String comando, ArrayList<Point> p) {

        switch (comando) {

            case "cuad":
                return new Forma(
                    new Rectangle2D.Double(p.get(0).x, p.get(0).y, p.get(1).x, p.get(1).y)
                );

            case "circ":
                return new Forma(
                    new Ellipse2D.Double(p.get(0).x, p.get(0).y, p.get(1).x, p.get(1).y)
                );

            case "linea":
                return new Forma(
                    new Line2D.Double(p.get(0).x, p.get(0).y, p.get(1).x, p.get(1).y)
                );

            case "round":
                return new Forma(
                    new RoundRectangle2D.Double(
                        p.get(0).x, p.get(0).y,
                        p.get(1).x, p.get(1).y,
                        p.get(2).x, p.get(2).y
                    )
                );

            case "arco":
                return new Forma(
                    new Arc2D.Double(
                        p.get(0).x, p.get(0).y,
                        p.get(1).x, p.get(1).y,
                        p.get(2).x, p.get(2).y,
                        Arc2D.OPEN
                    )
                );

            case "quad":
                return new Forma(
                    new QuadCurve2D.Double(
                        p.get(0).x, p.get(0).y,
                        p.get(1).x, p.get(1).y,
                        p.get(2).x, p.get(2).y
                    )
                );

            case "cubic":
                return new Forma(
                    new CubicCurve2D.Double(
                        p.get(0).x, p.get(0).y,
                        p.get(1).x, p.get(1).y,
                        p.get(2).x, p.get(2).y,
                        p.get(3).x, p.get(3).y
                    )
                );

            case "poly":
                Path2D path = new Path2D.Double();
                path.moveTo(p.get(0).x, p.get(0).y);

                for (int i = 1; i < p.size(); i++)
                    path.lineTo(p.get(i).x, p.get(i).y);

                return new Forma(path);

            default:
                throw new IllegalArgumentException("Figura desconocida: " + comando);
        }
    }
}
