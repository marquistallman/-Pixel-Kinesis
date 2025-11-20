package com.mycompany.pixelkinesis;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class Capa extends Nodo {

    public ArrayList<Nodo> hijos;

    public Capa() {
        super();
        this.hijos = new ArrayList<>();
        // OJO: NO inicializamos área, frente, fondo ni forma aquí.
        // Todo eso lo hace ComandoDibujar.
    }

    public void agregarNodo(Nodo n) {
        hijos.add(n);
    }
    @Override
    public void ejecutar(Graphics2D g) {
        // Una capa solo ejecuta sus hijos
        for (Nodo n : hijos) {
            if (n != null) {
                n.ejecutar(g);
            }
        }
    }
}

