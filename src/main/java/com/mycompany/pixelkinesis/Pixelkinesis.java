/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.pixelkinesis;

import javax.swing.JFrame;

import com.mycompany.pixelkinesis.comandos.*;
import com.mycompany.pixelkinesis.UI.VentanaEditor;

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
    SwingUtilities.invokeLater(() -> {
        new VentanaEditor().setVisible(true);
    });}

    }

