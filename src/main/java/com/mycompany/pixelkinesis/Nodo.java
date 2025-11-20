package com.mycompany.pixelkinesis;
import java.util.ArrayList;
import java.awt.Graphics2D;
import com.mycompany.pixelkinesis.comandos.Comando;
public abstract class Nodo {
    public AreaDeInfluencia area;          // composición
    public ArrayList<Comando> comandos;    // lista de comandos asociados
    public abstract void ejecutar(Graphics2D g);
}
