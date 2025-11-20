package com.mycompany.pixelkinesis.comandos;

import com.mycompany.pixelkinesis.*;
import java.awt.Graphics2D;
public abstract class Comando {

    public abstract void ejecutar(Nodo nodo, Graphics2D g);

}
