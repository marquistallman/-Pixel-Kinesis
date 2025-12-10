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

                case "escalar":
                // params = ["2.0"] o ["1.5"]
                if (params.isEmpty()) {
                    throw new IllegalArgumentException("Comando escalar requiere un parametro (factor)");
                }
                double factor = Double.parseDouble(params.get(0).trim());
                if (factor <= 0) {
                    throw new IllegalArgumentException("El factor de escala debe ser mayor que 0");
                }
                return new ComandoEscalar(factor);
                
                case "rotar":
                // params = ["45"] o ["90.5"]
                if (params.isEmpty()) {
                    throw new IllegalArgumentException("Comando rotar requiere un parametro (grados)");
                }
                double grados = Double.parseDouble(params.get(0).trim());
                return new ComandoRotar(grados);
                
                case "opacidad":
                // params = ["0.5"] o ["1.0"]
                if (params.isEmpty()) {
                    throw new IllegalArgumentException("Comando opacidad requiere un parametro (valor entre 0.0 y 1.0)");
                }
                float opacidad = Float.parseFloat(params.get(0).trim());
                if (opacidad < 0f || opacidad > 1f) {
                    throw new IllegalArgumentException("La opacidad debe estar entre 0.0 y 1.0");
                }
                return new ComandoCambiarOpacidad(opacidad);
            
                 case "borde":
                // params = ["R,G,B"] o ["R,G,B,grosor"]
                if (params.isEmpty()) {
                    throw new IllegalArgumentException("Comando borde requiere al menos el color (R,G,B)");
                }
                String[] partesBorde = params.get(0).split(",");
                if (partesBorde.length < 3) {
                    throw new IllegalArgumentException("El color debe tener formato R,G,B o R,G,B,grosor");
                }
                int rBorde = Integer.parseInt(partesBorde[0].trim());
                int gBorde = Integer.parseInt(partesBorde[1].trim());
                int bBorde = Integer.parseInt(partesBorde[2].trim());
                Color colorBorde = new Color(rBorde, gBorde, bBorde);
                
                // Grosor opcional (por defecto 2.0)
                float grosorBorde = 2.0f;
                if (partesBorde.length >= 4) {
                    grosorBorde = Float.parseFloat(partesBorde[3].trim());
                }
                return new ComandoBorde(colorBorde, grosorBorde);                
                
                case "animate":
                // delega a CompilerAnimate (igual que tu diseño modular)
                return CompilerAnimate.crearAnimate(params);
                case "recortar":
                // No requiere parámetros
                return new ComandoRecortar();
            // Aquí puedes añadir todos los comandos nuevos que quieras
            // sin tocar la clase Compiler original.


            default:
                throw new IllegalArgumentException(
                    "Comando no reconocido: " + comandoInput
                );
        }
    }
}

