package com.mycompany.pixelkinesis.Animate;
import java.awt.Graphics2D;
import java.awt.Point;

import com.mycompany.pixelkinesis.Nodo;
import com.mycompany.pixelkinesis.comandos.*;
import com.mycompany.pixelkinesis.comandos.Comando;

public abstract class Animate extends Comando {

    protected int timeStart = 0;
    protected int timeEnd = 0;
    protected int speed = 1;

    /**
     * Ejecuta la animación sobre el nodo.
     */
    @Override
    public abstract void ejecutar(Nodo nodo, Graphics2D g);

    public void setTimeRange(int start, int end) {
        this.timeStart = start;
        this.timeEnd = end;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}

