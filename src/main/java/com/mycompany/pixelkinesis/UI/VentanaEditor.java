package com.mycompany.pixelkinesis.UI;

import javax.swing.*;
import java.awt.*;
import com.mycompany.pixelkinesis.*;
import java.util.ArrayList;
import com.mycompany.pixelkinesis.comandos.*;
import com.mycompany.pixelkinesis.Compiler.*;
import com.mycompany.pixelkinesis.ComposedFigures.*;
import com.mycompany.pixelkinesis.Animate.*;
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
        System.out.println("═══════════════════════════════════════");
        System.out.println("🚀 INICIANDO ejecutarComandos()");
        System.out.println("═══════════════════════════════════════");

        capa.limpiar();
        String texto = panelConsola.consolaEntrada.getText();
        System.out.println("📝 Texto recibido (" + texto.split("\n").length + " líneas):");
        System.out.println(texto);
        System.out.println("───────────────────────────────────────");

        String[] lineas = texto.split("\n");
        Nodo ultimoNodoCreado = null;

        int i = 0;
        while (i < lineas.length) {
            String raw = lineas[i];
            String linea = raw.trim();

            if (linea.isEmpty()) {
                i++;
                continue;
            }

            if (linea.equalsIgnoreCase("_Composed")) {
                int next = handleComposed(lineas, i);
                i = next;
                if (this.lastBlockCreated != null) { ultimoNodoCreado = this.lastBlockCreated; this.lastBlockCreated = null; }
                continue;
            }

            if (linea.equalsIgnoreCase("-animate")) {
                int next = handleAnimate(lineas, i, ultimoNodoCreado);
                i = next;
                continue;
            }

            if (linea.startsWith("-")) {
                if (ultimoNodoCreado == null) { i++; continue; }
                handleNormalCommand(ultimoNodoCreado, linea);
                i++;
                continue;
            }

            // creación normal de figura
            FiguraGeometrica fig = createFiguraFromLine(linea);
            if (fig != null) {
                capa.agregarNodo(fig);
                ultimoNodoCreado = fig;
            }
            i++;
        }

        panelDibujo.repaint();
    }    

    // ------------------------------ HELPERS ------------------------------
    private Nodo lastBlockCreated = null; // usado para comunicar el nodo creado por un bloque sin crear clases nuevas

    private int handleComposed(String[] lineas, int startIndex) {
        System.out.println("🎨 Bloque _Composed iniciado");
        ArrayList<FiguraGeometrica> bufferComposed = new ArrayList<>();
        FiguraGeometrica ultimoInterno = null;

        int i = startIndex + 1;
        while (i < lineas.length) {
            String lineaComposed = lineas[i].trim();
            if (lineaComposed.equalsIgnoreCase("_ComposedEnd")) {
                System.out.println("🏁 Bloque _ComposedEnd detectado");
                if (!bufferComposed.isEmpty()) {
                    ComposedFigures comp = CompilerComposed.crearFiguraCompuesta(bufferComposed);
                    capa.agregarNodo(comp);
                    System.out.println("✅ ComposedFigures creado con " + bufferComposed.size() + " figuras internas");
                    this.lastBlockCreated = comp;
                    return i + 1;
                } else {
                    System.err.println("⚠ Advertencia: El buffer de figuras compuestas está vacío");
                    this.lastBlockCreated = null;
                    return i + 1;
                }
            }

            if (lineaComposed.isEmpty()) { i++; continue; }

            if (lineaComposed.startsWith("-")) {
                if (ultimoInterno == null) {
                    System.err.println("⚠ Línea de comando dentro de _Composed antes de cualquier figura: " + lineaComposed);
                    i++; continue;
                }

                String cmdLine = lineaComposed.substring(1).trim();
                String[] partesCmd = cmdLine.split(" ");
                String nombreCmd = partesCmd[0];
                ArrayList<String> paramsCmd = collectParams(partesCmd, 1);

                // Nota: NO expandimos comas aquí. Algunos comandos (ej: color)
                // esperan un único parámetro con comas ("R,G,B"). Se mantiene
                // el formato original tal cual lo escribió el usuario.
                Comando c = createCommandSafe(nombreCmd, paramsCmd);
                if (c != null) {
                    ultimoInterno.comandos.add(c);
                    System.out.println("➕ Comando agregado a figura interna: " + nombreCmd + " -> " + c.getClass().getSimpleName());
                }

                i++; continue;
            }

            // definición de figura
            String[] partes = lineaComposed.split(" ");
            String tipo = partes[0];
            ArrayList<String> params = collectParams(partes, 1);
            FiguraGeometrica fig = createFiguraFromParts(tipo, params);
            if (fig != null) {
                bufferComposed.add(fig);
                ultimoInterno = fig;
                System.out.println("➕ Figura agregada al buffer composed: " + tipo);
            }

            i++;
        }

        // reached EOF without _ComposedEnd
        this.lastBlockCreated = null;
        return i;
    }

    private int handleAnimate(String[] lineas, int startIndex, Nodo ultimoNodoCreado) {
        System.out.println("🎬 Bloque -animate iniciado");
        ArrayList<Comando> bufferAnimate = new ArrayList<>();

        int i = startIndex + 1;
        while (i < lineas.length) {
            String lineaAnimate = lineas[i].trim();
            if (lineaAnimate.equalsIgnoreCase("-animateEnd") || lineaAnimate.equalsIgnoreCase("-animate end")) {
                System.out.println("🏁 Bloque -animateEnd detectado");
                if (ultimoNodoCreado == null) {
                    System.err.println("❌ Error: No hay nodo creado para agregar animaciones");
                } else if (bufferAnimate.isEmpty()) {
                    System.err.println("⚠ Advertencia: El buffer de animaciones está vacío");
                } else {
                    System.out.println("📦 Agregando " + bufferAnimate.size() + " comandos al nodo");
                    ultimoNodoCreado.comandos.addAll(bufferAnimate);
                    int exec = 0;
                    for (Comando cmd : bufferAnimate) {
                        System.out.println("🔍 Revisando comando: " + cmd.getClass().getSimpleName() + " (es Animate? " + (cmd instanceof Animate) + ")");
                        if (cmd instanceof Animate) {
                            System.out.println("🎬 Ejecutando animación: " + cmd.getClass().getSimpleName());
                            cmd.ejecutar(ultimoNodoCreado, null);
                            exec++;
                        }
                    }
                    System.out.println("✅ Se ejecutaron " + exec + " animaciones de " + bufferAnimate.size() + " comandos totales");
                }
                return i + 1;
            }

            if (lineaAnimate.isEmpty()) { i++; continue; }
            if (!lineaAnimate.startsWith("-")) { System.out.println("⚠ Línea ignorada en bloque animate (no empieza con -): " + lineaAnimate); i++; continue; }

            String cmdLine = lineaAnimate.substring(1).trim();
            String[] partes = cmdLine.split(" ");
            String nombre = partes[0];
            ArrayList<String> params = collectParams(partes, 1);

            if (nombre.equalsIgnoreCase("mover")) {
                System.out.println("🔄 Convirtiendo 'mover' a 'animate mover' dentro del bloque animate");
                nombre = "animate";
                params.add(0, "mover");
            }

            System.out.println("🔧 Procesando comando en bloque animate: nombre='" + nombre + "', params=" + params);
            ArrayList<String> paramsProcesados = expandCommaParams(params);
            Comando c = createCommandSafe(nombre, paramsProcesados);
            if (c != null) { bufferAnimate.add(c); System.out.println("✅ Comando agregado al buffer: " + c.getClass().getSimpleName()); }

            i++;
        }

        return i;
    }

    private void handleNormalCommand(Nodo target, String linea) {
        String cmdLine = linea.substring(1).trim();
        String[] partes = cmdLine.split(" ");
        String nombre = partes[0];
        ArrayList<String> params = collectParams(partes, 1);
        Comando c = createCommandSafe(nombre, params);
        if (c != null) target.comandos.add(c);
        else System.err.println("⚠ Advertencia: No se pudo crear el comando '" + nombre + "'. Se omite.");
    }

    private ArrayList<String> collectParams(String[] partes, int from) {
        ArrayList<String> params = new ArrayList<>();
        for (int j = from; j < partes.length; j++) params.add(partes[j]);
        return params;
    }

    private ArrayList<String> expandCommaParams(ArrayList<String> params) {
        ArrayList<String> out = new ArrayList<>();
        for (String param : params) {
            if (param.contains(",")) {
                String[] p = param.split(",");
                for (String part : p) { String t = part.trim(); if (!t.isEmpty()) out.add(t); }
            } else { out.add(param.trim()); }
        }
        return out;
    }

    private Comando createCommandSafe(String nombre, ArrayList<String> params) {
        try { return Compiler.comando(nombre, params); }
        catch (Exception ex) { System.err.println("⚠ No se pudo crear comando: " + ex.getMessage()); return null; }
    }

    private FiguraGeometrica createFiguraFromLine(String linea) {
        String[] partes = linea.split(" ");
        if (partes.length == 0) return null;
        String tipo = partes[0];
        ArrayList<String> params = collectParams(partes, 1);
        return createFiguraFromParts(tipo, params);
    }

    private FiguraGeometrica createFiguraFromParts(String tipo, ArrayList<String> params) {
        try {
            Forma forma = Compiler.forma(tipo, params);

            // Normalizar la forma para que su geometría sea relativa al (0,0)
            // y almacenar la posición original en AreaDeInfluencia.
            java.awt.Shape shape = forma.getShape();
            java.awt.geom.Rectangle2D bounds = shape.getBounds2D();
            int baseX = (int) Math.round(bounds.getX());
            int baseY = (int) Math.round(bounds.getY());

            // Trasladar la forma a origen (0,0)
            java.awt.geom.AffineTransform at = java.awt.geom.AffineTransform.getTranslateInstance(-baseX, -baseY);
            java.awt.Shape shifted = at.createTransformedShape(shape);
            Forma nueva = new Forma(shifted);

            AreaDeInfluencia area = new AreaDeInfluencia(nueva, new java.awt.Point(baseX, baseY));

            ArrayList<Comando> cmds = new ArrayList<>();
            cmds.add(new com.mycompany.pixelkinesis.comandos.ComandoDibujar());
            return new FiguraGeometrica(nueva, area, cmds);
        } catch (Exception ex) {
            System.err.println("⚠ Error creando figura '" + tipo + "': " + ex.getMessage());
            return null;
        }
    }
    
    // Método auxiliar para crear y añadir un nodo
    private void agregarNodo(String comandoCrear, ArrayList<String> paramsCrear, ArrayList<Comando> comandosNodo) {
        Forma forma = Compiler.forma(comandoCrear, paramsCrear);
        AreaDeInfluencia area = new AreaDeInfluencia(forma);
        FiguraGeometrica nodo = new FiguraGeometrica(forma, area, comandosNodo);
        capa.agregarNodo(nodo);
    }    
}

