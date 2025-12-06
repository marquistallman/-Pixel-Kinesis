package com.mycompany.pixelkinesis.ComposedFigures;
import com.mycompany.pixelkinesis.*;
import com.mycompany.pixelkinesis.comandos.*;
import java.util.ArrayList;
import java.awt.*;
public class ComposedFigures extends FiguraGeometrica {

    private ArrayList<FiguraGeometrica> figuras = new ArrayList<>();

    public ComposedFigures(ArrayList<FiguraGeometrica> figuras) {
        super(null, null, new ArrayList<>());

        if (figuras == null || figuras.isEmpty()) {
            throw new IllegalArgumentException("La figura compuesta no puede estar vacía.");
        }

        this.figuras = figuras;

        // Crear área que abarque todas las figuras internas
        this.area = calcularAreaCompuesta();

        // Crear forma rectangular que envuelve todo
        this.forma = new Forma(new Rectangle(
                area.getPosicion().x,
                area.getPosicion().y,
                (int) area.forma.getBounds().getBounds().getWidth(),
                (int) area.forma.getBounds().getBounds().getHeight()
        ));
    }

    // ======================================================
    //          CALCULAR ÁREA TOTAL QUE ENVUELVE TODO
    // ======================================================
    private AreaDeInfluencia calcularAreaCompuesta() {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

        for (FiguraGeometrica f : figuras) {
            Rectangle r = f.forma.getShape().getBounds();

            minX = Math.min(minX, r.x);
            minY = Math.min(minY, r.y);

            maxX = Math.max(maxX, r.x + r.width);
            maxY = Math.max(maxY, r.y + r.height);
        }

        int ancho = maxX - minX;
        int alto = maxY - minY;

        AreaDeInfluencia area = new AreaDeInfluencia(
                new Forma(new Rectangle(minX, minY, ancho, alto)),
                new java.awt.Point(minX, minY)
        );

        return area;
    }

    // ======================================================
    //                     DIBUJAR
    // ======================================================
    @Override
    public void ejecutar(Graphics2D g) {

        g = (Graphics2D) g.create(); // proteger estado

        // 1. Dibujar fondo global
        if (this.area.fondo != null) {
            this.area.fondo.aplicar(g, this.area);
        }

        // 2. Dibujar todas las figuras internas
        for (FiguraGeometrica f : figuras) {
            f.ejecutar(g); // usa el comando dibujar interno
        }

        // 3. Dibujar frente global
        if (this.area.frente != null) {
            this.area.frente.draw(g);
        }

        // 4. Ejecutar comandos propios de la figura compuesta
        for (Comando c : comandos) {
            c.ejecutar(this, g);
        }

        g.dispose();
    }

    // ======================================================
    //              Añadir figuras internas
    // ======================================================
    public void addFigura(FiguraGeometrica f) {
        figuras.add(f);
        this.area = calcularAreaCompuesta();  // recalcular área global
    }
}

