package com.mycompany.pixelkinesis.Compiler;
import javax.swing.*;
import java.awt.*;
import com.mycompany.pixelkinesis.*;
import java.util.ArrayList;
import com.mycompany.pixelkinesis.comandos.*;
import com.mycompany.pixelkinesis.ComposedFigures.ComposedFigures;
public class Compiler {
    // ==========================
    // CREACIÓN DE FIGURAS
    // ==========================
    public static Forma forma(String comando, ArrayList<String> params) {
        return CompilerCreate.crearForma(comando, params);
    }

    // ==========================
    // CREACIÓN DE COMANDOS
    // ==========================
    public static Comando comando(String comando, ArrayList<String> params) {
        return CompilerCommands.crearComando(comando, params);
    }

    // ==========================
    // BLOQUES COMPUESTOS
    // ==========================
    public static boolean esInicioComposed(String linea) {
        return CompilerComposed.esInicioComposed(linea);
    }

    public static boolean esFinComposed(String linea) {
        return CompilerComposed.esFinComposed(linea);
    }

    public static ComposedFigures crearCompuesto(ArrayList<FiguraGeometrica> internas) {
        return CompilerComposed.crearFiguraCompuesta(internas);
    }

    // ==========================
    // ANIMACIONES
    // ==========================
    public static Comando crearComandoAnimate(ArrayList<String> params) {
        return CompilerAnimate.crearAnimate(params);
    }
}
