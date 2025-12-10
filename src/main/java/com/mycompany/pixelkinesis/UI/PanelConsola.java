package com.mycompany.pixelkinesis.UI;

import javax.swing.*;
import java.awt.*;

public class PanelConsola extends JPanel {
    public JTextArea consolaEntrada;
    public JTextArea consolaSalida;
    
    private JButton runButton; 

    public PanelConsola() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(350, 700));

        // Entrada
        consolaEntrada = new JTextArea();
        consolaEntrada.setBorder(BorderFactory.createTitledBorder("📝 Código de Entrada"));
        consolaEntrada.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane scrollEntrada = new JScrollPane(consolaEntrada);

        // Salida
        consolaSalida = new JTextArea();
        consolaSalida.setBorder(BorderFactory.createTitledBorder("⚙️ Log / Salida"));
        consolaSalida.setBackground(new Color(240, 240, 240)); 
        consolaSalida.setEditable(false);
        consolaSalida.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane scrollSalida = new JScrollPane(consolaSalida);
        
        // JSplitPane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollEntrada, scrollSalida);
        splitPane.setResizeWeight(0.8); 
        splitPane.setDividerLocation(500); 
        splitPane.setBorder(BorderFactory.createEmptyBorder()); 

        // Panel de Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        
        runButton = new JButton("🚀 Ejecutar Comandos (Run)");
        runButton.setFont(new Font("Arial", Font.BOLD, 14));
        runButton.setBackground(new Color(50, 150, 255)); 
        runButton.setForeground(Color.WHITE);
        runButton.setOpaque(true);
        runButton.setBorderPainted(false);
        
        buttonPanel.add(runButton);

        // Ensamblaje
        add(splitPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    public JButton getRunButton() {
        return runButton;
    }
}

