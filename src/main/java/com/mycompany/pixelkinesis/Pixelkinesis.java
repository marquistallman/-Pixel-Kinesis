/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.pixelkinesis;

import javax.swing.JFrame;

import com.mycompany.pixelkinesis.comandos.*;


/**
 *
 * @author davir
 */
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
public class Pixelkinesis {
static Graphics2D g;
    public static void main(String[] args) {
        JFrame ventana = new JFrame("Prueba Sistema de Nodos");
        ventana.setSize(600, 600);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel donde dibujamos
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics gr) {
                super.paintComponent(gr);
                Graphics2D g2 = (Graphics2D) gr;

                // ====== Crear forma ======
                Forma forma = new Forma(
                    new java.awt.geom.Rectangle2D.Double(100, 100, 200, 150)
                );

                // ====== Área ======
                AreaDeInfluencia area = new AreaDeInfluencia(forma);

                // ====== Comandos ======
                ArrayList<Comando> comandos = new ArrayList<>();
                comandos.add(new ComandoDibujar());
                comandos.add(new ComandoCambiarColor(Color.RED));  // dibujar
                // aquí puedes probar mover, etc.

                // ====== Crear nodo ======
                FiguraGeometrica nodo = new FiguraGeometrica(forma, area, comandos);

                // ====== Ejecutar ======
                nodo.ejecutar(g2);
            }
        };

        ventana.add(panel);
        ventana.setVisible(true);
    }
    }

