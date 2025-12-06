package com.mycompany.pixelkinesis.Compiler;
import java.util.ArrayList;
import com.mycompany.pixelkinesis.FiguraGeometrica;
import com.mycompany.pixelkinesis.ComposedFigures.ComposedFigures;
public class CompilerComposed {

    // Detecta el inicio del bloque compuesto
    public static boolean esInicioComposed(String linea) {
        return linea.trim().equalsIgnoreCase("_Composed");
    }

    // Detecta el final del bloque compuesto
    public static boolean esFinComposed(String linea) {
        return linea.trim().equalsIgnoreCase("_ComposedEnd");
    }

    // Crea la figura compuesta real usando tu clase original
    public static ComposedFigures crearFiguraCompuesta(ArrayList<FiguraGeometrica> internas) {
        return new ComposedFigures(internas);
    }
}

