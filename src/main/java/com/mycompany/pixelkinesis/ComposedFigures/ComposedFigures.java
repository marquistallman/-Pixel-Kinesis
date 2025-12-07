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

        // Crear forma rectangular que envuelve todo (relativa a 0,0)
        this.forma = new Forma(new Rectangle(
            0,
            0,
            (int) area.forma.getBounds().getBounds().getWidth(),
            (int) area.forma.getBounds().getBounds().getHeight()
        ));

        // Inicializar fondo y frente de área compuesta (similar a ComandoDibujar)
        if (this.area.fondo == null) {
            this.area.fondo = new Fondo(new Color(0,0,0,0), 0.0f);
        }

        if (this.area.frente == null) {
            ArrayList<Nodo> lista = new ArrayList<>();
            for (FiguraGeometrica f : figuras) lista.add(f);
            this.area.frente = new Frente(lista);
            this.area.frente.setColor(Color.GRAY);
            this.area.frente.setOpacidad(1.0f);
        }
    }

    // ======================================================
    //          CALCULAR ÁREA TOTAL QUE ENVUELVE TODO
    // ======================================================
    private AreaDeInfluencia calcularAreaCompuesta() {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

        for (FiguraGeometrica f : figuras) {
            Rectangle r = f.forma.getShape().getBounds();
            java.awt.Point pos = new java.awt.Point(0, 0);
            if (f.area != null && f.area.getPosicion() != null) pos = f.area.getPosicion();

            int absX = r.x + pos.x;
            int absY = r.y + pos.y;

            minX = Math.min(minX, absX);
            minY = Math.min(minY, absY);

            maxX = Math.max(maxX, absX + r.width);
            maxY = Math.max(maxY, absY + r.height);
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

    // Permitir acceso a las figuras internas para operaciones externas (ej. mover)
    public ArrayList<FiguraGeometrica> getFiguras() {
        return figuras;
    }

    // Forzar el recálculo del área compuesta después de cambios en las figuras internas
    public void recalcularArea() {
        this.area = calcularAreaCompuesta();
    }
}

