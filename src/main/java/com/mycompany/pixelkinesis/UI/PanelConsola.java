package com.mycompany.pixelkinesis.UI;

import javax.swing.*;
import java.awt.*;

public class PanelConsola extends JPanel {
    public JTextArea consolaEntrada;
    public JTextArea consolaSalida;

    public PanelConsola() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(350, 700));
        
        
        // Entrada
        consolaEntrada = new JTextArea();
        consolaEntrada.setBorder(BorderFactory.createTitledBorder("Entrada"));
        consolaEntrada.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane scrollEntrada = new JScrollPane(consolaEntrada);

        // Salida
        consolaSalida = new JTextArea();
        consolaSalida.setBorder(BorderFactory.createTitledBorder("Salida"));
        consolaSalida.setEditable(false);
        consolaSalida.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane scrollSalida = new JScrollPane(consolaSalida);

        add(scrollEntrada, BorderLayout.CENTER);
        add(scrollSalida, BorderLayout.SOUTH);
    }
    
}

