package com.mycompany.pixelkinesis.comandos;

import com.mycompany.pixelkinesis.*;
import com.mycompany.pixelkinesis.ComposedFigures.*;
import java.awt.geom.AffineTransform;
import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;

public class ComandoRecortar extends Comando {

    private boolean ejecutado = false;

    @Override
    public void ejecutar(Nodo nodo, Graphics2D g) {

        if (ejecutado) return; // ⬅⬅⬅ evita repetir recorte

        if (!(nodo instanceof ComposedFigures)) {
            throw new IllegalArgumentException("El comando 'recortar' solo puede usarse en figuras compuestas.");
        }

        ComposedFigures comp = (ComposedFigures) nodo;
        ArrayList<FiguraGeometrica> figs = comp.getFiguras();

        if (figs.size() < 2) {
            return; // ⬅⬅⬅ NO lanzar excepción en render, solo ignorar
        }

        // ============================================================
        // 1. Obtener las áreas transformadas de todas las figuras
        // ============================================================
        ArrayList<Area> areas = new ArrayList<>();

        for (FiguraGeometrica f : figs) {

            Shape shape = f.forma.getShape();

            if (f.area != null && f.area.getPosicion() != null) {
                Point p = f.area.getPosicion();
                AffineTransform at = AffineTransform.getTranslateInstance(p.x, p.y);
                shape = at.createTransformedShape(shape);
            }

            areas.add(new Area(shape));
        }

        // ============================================================
        // 2. Calcular intersección total
        // ============================================================
        Area inter = new Area(areas.get(0));

        for (int i = 1; i < areas.size(); i++) {
            inter.intersect(areas.get(i));
        }

        if (inter.isEmpty()) {
            System.out.println("⚠ No existe intersección entre las figuras.");
            ejecutado = true; // evitar intentos futuros
            return;
        }

        // ============================================================
        // 3. Construir figura resultante
        // ============================================================
        g.setColor(Color.RED);
        g.fill(inter);
        Rectangle bounds = inter.getBounds();

        Forma formaInter = new Forma(inter);

        AreaDeInfluencia areaInter = new AreaDeInfluencia(
            new Forma(new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height)),
            new Point(bounds.x, bounds.y)
        );

        Frente frente = new Frente(new ArrayList<>());
        frente.setColor(Color.RED);
        frente.setOpacidad(1.0f);
        areaInter.frente = frente;

        areaInter.fondo = new Fondo(new Color(0,0,0,0), 0.0f);

        FiguraGeometrica figInter = new FiguraGeometrica(
            formaInter,
            areaInter,
            new ArrayList<>()
        );

        // ============================================================
        // 4. Reemplazar todas las figuras por la intersección
        // ============================================================
        comp.getFiguras().clear();
        comp.getFiguras().add(figInter);

        comp.recalcularArea();

        ejecutado = true; // ⬅⬅⬅ importantísimo
    }
}

