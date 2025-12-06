package com.mycompany.pixelkinesis.Compiler;

import com.mycompany.pixelkinesis.comandos.Comando;
import com.mycompany.pixelkinesis.Pixelkinesis;
import com.mycompany.pixelkinesis.Animate.*;
import java.awt.Point;
import java.util.ArrayList;
import com.mycompany.pixelkinesis.UI.*;;
public class CompilerAnimate {

    /**
     * Crea un comando tipo Animate basado en parámetros.
     *
     * Formato esperado:
     * -animate mover x y tiempo speed
     *
     * Ejemplo:
     * -animate mover 200 300 1000 16
     */
    public static Comando crearAnimate(ArrayList<String> params) {

        if (params == null || params.isEmpty()) return null;

        String tipoAnim = params.get(0).toLowerCase();

        switch (tipoAnim) {

            // =====================================
            //           ANIMATE MOVER
            // =====================================
            case "mover": {

                // Aceptar dos formatos:
                // 1. mover x y tiempo speed (4 parámetros después de "mover")
                // 2. mover x_origen y_origen x_destino y_destino tiempo speed (6 parámetros, ignoramos origen)
                int x, y, time, speed;
                
                if (params.size() >= 7) {
                    // Formato: mover x_origen y_origen x_destino y_destino tiempo speed
                    // Ignoramos origen, usamos destino
                    try {
                        x = Integer.parseInt(params.get(3).trim());
                        y = Integer.parseInt(params.get(4).trim());
                        time = Integer.parseInt(params.get(5).trim());
                        speed = Integer.parseInt(params.get(6).trim());
                        System.out.println("📌 Formato con origen detectado, usando destino: (" + x + "," + y + ")");
                    } catch (NumberFormatException ex) {
                        System.err.println("❌ Error: parámetros inválidos en animate mover (formato con origen)");
                        return null;
                    }
                } else if (params.size() >= 5) {
                    // Formato: mover x y tiempo speed
                    try {
                        x = Integer.parseInt(params.get(1).trim());
                        y = Integer.parseInt(params.get(2).trim());
                        time = Integer.parseInt(params.get(3).trim());
                        speed = Integer.parseInt(params.get(4).trim());
                    } catch (NumberFormatException ex) {
                        System.err.println("❌ Error: parámetros inválidos en animate mover");
                        return null;
                    }
                } else {
                    System.err.println("❌ Error: animate mover requiere: mover x y tiempo speed (o mover x_origen y_origen x_destino y_destino tiempo speed)");
                    return null;
                }

                // El inicio se define dinámicamente en ejecutar()
                return new AnimateMover(null, new Point(x, y), time, speed, Pixelkinesis.programita.panelDibujo);

            }

            // =====================================
            //     Aquí puedes agregar más:
            //     - "color"
            //     - "rotar"
            //     - "escalar"
            // =====================================

        }

        System.err.println("⚠ Advertencia: tipo de animación no reconocido: " + tipoAnim);
        return null;
    }
}

