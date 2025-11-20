package com.mycompany.pixelkinesis.UI;

import javax.swing.*;
import java.awt.*;

public class VentanaEditor extends JFrame {

    public JTextArea consolaSalida;
    public JTextArea consolaEntrada;
    public JPanel panelDibujo;

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
        JPanel panelIzq = new JPanel();
        panelIzq.setLayout(new BorderLayout());
        panelIzq.setPreferredSize(new Dimension(350, 700));

        // Entrada
        consolaEntrada = new JTextArea();
        consolaEntrada.setBorder(BorderFactory.createTitledBorder("Entrada"));
        consolaEntrada.setFont(new Font("Consolas", Font.PLAIN, 14));

        // Salida
        consolaSalida = new JTextArea();
        consolaSalida.setBorder(BorderFactory.createTitledBorder("Salida"));
        consolaSalida.setEditable(false);
        consolaSalida.setFont(new Font("Consolas", Font.PLAIN, 14));

        panelIzq.add(new JScrollPane(consolaEntrada), BorderLayout.NORTH);
        panelIzq.add(new JScrollPane(consolaSalida), BorderLayout.CENTER);

        add(panelIzq, BorderLayout.WEST);

        // ===========================
        // 3. PANEL DERECHO (Dibujo)
        // ===========================
        panelDibujo = new PanelDibujo();
        panelDibujo.setBackground(Color.WHITE);

        add(panelDibujo, BorderLayout.CENTER);
    }
}

