package com.mycompany.pixelkinesis.Compiler;

import java.awt.*;
import java.util.ArrayList;
import com.mycompany.pixelkinesis.comandos.*;
import javax.swing.JColorChooser;
public class CompilerCommands {

    public static Comando crearComando(String comandoInput, ArrayList<String> params) {

        switch (comandoInput) {

            case "dibujar":
                return new ComandoDibujar();

            case "color":
                if (params.isEmpty()) {
                    Color colorSeleccionado = JColorChooser.showDialog(null, "Selecciona un color", Color.WHITE);
                    if (colorSeleccionado == null) {
                        throw new IllegalArgumentException("Selección de color cancelada");
                    }
                    return new ComandoCambiarColor(colorSeleccionado);
                }
                // params[0] = "R,G,B"  ó  "R G B A"
                String[] parts = params.get(0).split(",");

                int r = Integer.parseInt(parts[0].trim());
                int g = Integer.parseInt(parts[1].trim());
                int b = Integer.parseInt(parts[2].trim());

                // Soporte opcional para alpha
                int a = 255;
                if (parts.length == 4) {
                    a = (int)(255 * Float.parseFloat(parts[3].trim())); // ejemplo: 0.5
                }

                return new ComandoCambiarColor(new Color(r, g, b, a));


            case "mover":
                // params = ["200","200"] o ["200,200"]
                int dx, dy;

                // Verificar si el primer parámetro contiene coma (formato "x,y")
                if (params.size() >= 1 && params.get(0).contains(",")) {
                    // Formato: "x,y"
                    String[] xy = params.get(0).split(",");
                    if (xy.length < 2) {
                        throw new IllegalArgumentException("Formato inválido para mover: se espera 'x,y' o 'x y'");
                    }
                    dx = Integer.parseInt(xy[0].trim());
                    dy = Integer.parseInt(xy[1].trim());
                } else if (params.size() >= 2) {
                    // Formato: "x" "y"
                    dx = Integer.parseInt(params.get(0).trim());
                    dy = Integer.parseInt(params.get(1).trim());
                } else {
                    throw new IllegalArgumentException("Comando mover requiere 2 parámetros (x y) o formato 'x,y'");
                }

                return new ComandoMover(new Point(dx, dy));


            case "animate":
                // delega a CompilerAnimate (igual que tu diseño modular)
                return CompilerAnimate.crearAnimate(params);


            // Aquí puedes añadir todos los comandos nuevos que quieras
            // sin tocar la clase Compiler original.


            default:
                throw new IllegalArgumentException(
                    "Comando no reconocido: " + comandoInput
                );
        }
    }
}

