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
    
        String comandoCrear = null;
        ArrayList<String> paramsCrear = null;
        ArrayList<Comando> comandosNodo = new ArrayList<>();
    
        for (String linea : lineas) {
            linea = linea.trim();
            if (linea.isEmpty()) continue;
    
            if (linea.startsWith("-")) {
                // Línea de modificación → convertir en Comando y añadir al nodo actual
                String comandoLinea = linea.substring(1).trim();
                String[] partes = comandoLinea.split(" ");
                String cmdTexto = partes[0];
    
                ArrayList<String> parametros = new ArrayList<>();
                for (int i = 1; i < partes.length; i++) parametros.add(partes[i]);
    
                Comando cmd = Compiler.comando(cmdTexto, parametros);
                comandosNodo.add(cmd);
    
            } else {
                // Nueva creación → si había un nodo anterior, lo agregamos a la capa
                if (comandoCrear != null) {
                    agregarNodo(comandoCrear, paramsCrear, comandosNodo);
                }
    
                // Preparamos el nuevo nodo
                String[] partes = linea.split(" ");
                comandoCrear = partes[0];
                paramsCrear = new ArrayList<>();
                for (int i = 1; i < partes.length; i++) paramsCrear.add(partes[i]);
    
                comandosNodo = new ArrayList<>();
                comandosNodo.add(new ComandoDibujar()); // siempre dibujar
            }
        }
    
        // Agregar el último nodo
        if (comandoCrear != null) {
            agregarNodo(comandoCrear, paramsCrear, comandosNodo);
        }
    
        panelDibujo.repaint();
    }
    
    // Método auxiliar para crear y añadir un nodo
    private void agregarNodo(String comandoCrear, ArrayList<String> paramsCrear, ArrayList<Comando> comandosNodo) {
        Forma forma = Compiler.forma(comandoCrear, paramsCrear);
        AreaDeInfluencia area = new AreaDeInfluencia(forma);
        FiguraGeometrica nodo = new FiguraGeometrica(forma, area, comandosNodo);
        capa.agregarNodo(nodo);
    }    
}

