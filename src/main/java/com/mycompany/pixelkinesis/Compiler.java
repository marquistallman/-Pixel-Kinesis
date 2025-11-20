package com.mycompany.pixelkinesis;
import java.util.ArrayList;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.*;
public class Compiler {
    private ArrayList<Forma>formas=new ArrayList<Forma>();
    Compiler(Point centerPoint, Point sizePoint ){
        Forma cuadradoForma = new Forma(
            new java.awt.geom.Rectangle2D.Double(centerPoint.x, centerPoint.y, sizePoint.x, sizePoint.y)
        );
        formas.add(cuadradoForma);
        Forma circuloForma = new Forma(
            new Ellipse2D.Double(centerPoint.x, centerPoint.y, sizePoint.x, sizePoint.y)
        );
        formas.add(circuloForma);
        formas.add(new Forma(
            new Line2D.Double(centerPoint.x, centerPoint.y, sizePoint.x, sizePoint.y)
        ));
    }
    Compiler(Point centerPoint, Point sizePoint, Point arcPoint) {
        formas.add(new Forma(
            new RoundRectangle2D.Double(
                centerPoint.x,
                centerPoint.y,
                sizePoint.x,
                sizePoint.y,
                arcPoint.x,
                arcPoint.y
            )
        ));
        formas.add(new Forma(
            new Arc2D.Double(
                centerPoint.x,
                centerPoint.y,
                sizePoint.x,
                sizePoint.y,
                arcPoint.x,   // startAngle
                arcPoint.y,   // arcAngle
                Arc2D.OPEN
            )
        ));
        formas.add(new Forma(
            new QuadCurve2D.Double(
                centerPoint.x, centerPoint.y,
                sizePoint.x, sizePoint.y,
                arcPoint.x, arcPoint.y
            )
        ));
    }
    Compiler(Point p0, Point p1, Point p2, Point p3) {
        formas.add(new Forma(
            new CubicCurve2D.Double(
                p0.x, p0.y,
                p1.x, p1.y,
                p2.x, p2.y,
                p3.x, p3.y
            )
        ));
    }        
    Compiler(ArrayList<Point> puntos) {
        Path2D path = new Path2D.Double();
        path.moveTo(puntos.get(0).x, puntos.get(0).y);
    
        for (int i = 1; i < puntos.size(); i++)
            path.lineTo(puntos.get(i).x, puntos.get(i).y);
    
        formas.add(new Forma(path));
    }
    public static Forma forma(String comandoInputs, ArrayList<String> params) {

        // 1. Convertimos los Strings de params a Points
        ArrayList<Point> puntos = new ArrayList<>();
        for (String s : params) {
            String[] split = s.split(",");
            int x = Integer.parseInt(split[0].trim());
            int y = Integer.parseInt(split[1].trim());
            puntos.add(new Point(x, y));
        }
        while (puntos.size() < 4) {
            puntos.add(new Point(0, 0));   // default simple
        }
        Compiler compiler;
    
        // 2. Seleccionar constructor según el comando
        switch (comandoInputs) {
    
            case "cuad":
                compiler = new Compiler(puntos.get(0), puntos.get(1));
                return compiler.formas.get(0);
    
            case "circ":
                compiler = new Compiler(puntos.get(0), puntos.get(1));
                return compiler.formas.get(1);  // círculo está en la posición 1
    
            case "linea":
                compiler = new Compiler(puntos.get(0), puntos.get(1));
                return compiler.formas.get(2);  // línea está en la posición 2
    
            case "round":
                compiler = new Compiler(puntos.get(0), puntos.get(1), puntos.get(2));
                return compiler.formas.get(0);  // RoundRectangle
    
            case "arco":
                compiler = new Compiler(puntos.get(0), puntos.get(1), puntos.get(2));
                return compiler.formas.get(1);  // Arc2D
    
            case "quad":
                compiler = new Compiler(puntos.get(0), puntos.get(1), puntos.get(2));
                return compiler.formas.get(2);  // QuadCurve2D
    
            case "cubic":
                compiler = new Compiler(puntos.get(0), puntos.get(1), puntos.get(2), puntos.get(3));
                return compiler.formas.get(0);  // CubicCurve única
    
            case "poly":
                compiler = new Compiler(puntos);
                return compiler.formas.get(0);  // Path2D única
    
            default:
                throw new IllegalArgumentException("Comando no reconocido: " + comandoInputs);
        }
    }            
}
