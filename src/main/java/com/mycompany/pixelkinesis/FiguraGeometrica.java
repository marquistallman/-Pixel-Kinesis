package com.mycompany.pixelkinesis;

import com.mycompany.pixelkinesis.comandos.Comando;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class FiguraGeometrica extends Nodo {

    public Forma forma;

    public FiguraGeometrica(Forma forma, AreaDeInfluencia area, ArrayList<Comando> comandos) {
        this.forma = forma;
        this.area = area;
        this.comandos = comandos;
    }

    /**
     * No hace override de ejecutar() sin parámetros.
     * Hereda el método ejecutar(Graphics2D g) de Nodo,
     * que ya recorre la lista de comandos y les pasa el Graphics2D.
     *
     * Si necesitas lógica extra antes/después de los comandos,
     * sobrescribe ejecutar(Graphics2D g) y llama super.ejecutar(g).
     */
    @Override
    public void ejecutar(Graphics2D g) {
        // ejemplo: lógica previa
        // aplicar transformaciones del area si deseas:
        // forma = area.transform(forma);

        // ejecutar los comandos (Nodo.ejecutar(g) hace esto), pero si quieres:
        for (Comando c : comandos) {
            c.ejecutar(this, g);
        }

        // ejemplo: lógica posterior (opcional)
    }
}

