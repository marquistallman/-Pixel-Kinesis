package com.mycompany.pixelkinesis.UI; 

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.EventQueue;

public class CustomOutputStream extends OutputStream {
    private JTextArea textArea;
    private Color color;

    public CustomOutputStream(JTextArea textArea, Color color) {
        this.textArea = textArea;
        this.color = color;
    }

    @Override
    public void write(int b) throws IOException {
        EventQueue.invokeLater(() -> {
            try {
                textArea.append(String.valueOf((char) b));
                textArea.setCaretPosition(textArea.getDocument().getLength());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}