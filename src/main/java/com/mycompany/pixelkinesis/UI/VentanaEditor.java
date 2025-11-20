package com.mycompany.pixelkinesis.UI;

import javax.swing.*;
import java.awt.*;
import com.mycompany.pixelkinesis.*;
import java.util.ArrayList;
import com.mycompany.pixelkinesis.comandos.*;
public class VentanaEditor extends JFrame {

    private PanelConsola panelConsola;
    public PanelDibujo panelDibujo;
    private Capa capa;
    public VentanaEditor() {

        setTitle("PixelKinesis Editor");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        // ===========================
        // 1. BARRA SUPERIOR
        // ===========================
        JMenuBar barra = new JMenuBar();

        JMenu menuArchivo = new JMenu("Archivo");
        JMenu menuEdicion = new JMenu("Edición");
        JMenu menuVer = new JMenu("Ver");
        JMenu menuComandos = new JMenu("Comandos");

        barra.add(menuArchivo);
        barra.add(menuEdicion);
        barra.add(menuVer);
        barra.add(menuComandos);

        setJMenuBar(barra);

        // ===========================
        // 2. PANEL IZQUIERDO (console)
        // ===========================
        JButton runButton = new JButton("Run");
        runButton.addActionListener(e -> ejecutarComandos());
        add(runButton, BorderLayout.SOUTH);
        panelConsola = new PanelConsola();
        add(panelConsola, BorderLayout.WEST);

        // ===========================
        // 3. PANEL DERECHO (Dibujo)
        // ===========================
        panelDibujo = new PanelDibujo();
        capa = new Capa();
        panelDibujo.setBackground(Color.WHITE);
        panelDibujo.setCapa(capa);
        add(panelDibujo, BorderLayout.CENTER);
    }
    private void ejecutarComandos() {
        capa.limpiar();
        String texto = panelConsola.consolaEntrada.getText();
        String[] lineas = texto.split("\n");
    
        for (String linea : lineas) {
            linea = linea.trim();
            if (linea.isEmpty()) continue;
    
            String[] partes = linea.split(" ");
            String comando = partes[0];
            ArrayList<String> params = new ArrayList<>();
            for (int i = 1; i < partes.length; i++) {
                params.add(partes[i]);
            }
    
            // Crear nodo usando tu Compiler
            Forma forma = Compiler.forma(comando, params);
            AreaDeInfluencia area = new AreaDeInfluencia(forma);
            ArrayList<Comando> comandosNodo = new ArrayList<>();
            comandosNodo.add(new ComandoDibujar());
            FiguraGeometrica nodo = new FiguraGeometrica(forma, area, comandosNodo);
    
            // Agregar a la capa del panel de dibujo
            capa.agregarNodo(nodo);
        }
    
        panelDibujo.repaint();
    }    
}

