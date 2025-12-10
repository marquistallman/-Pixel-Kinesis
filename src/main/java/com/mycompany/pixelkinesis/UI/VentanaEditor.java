package com.mycompany.pixelkinesis.UI;

import javax.swing.*;
import java.awt.*;
// Necesario para abrir enlaces en el navegador:
import java.net.URI;
import java.awt.Desktop; 
// Importaciones existentes
import com.mycompany.pixelkinesis.*;
import java.util.ArrayList;
import com.mycompany.pixelkinesis.comandos.*;
import com.mycompany.pixelkinesis.Compiler.*;
import com.mycompany.pixelkinesis.ComposedFigures.*;
import com.mycompany.pixelkinesis.Animate.*;
import java.io.PrintStream; 
import java.util.Collections;

public class VentanaEditor extends JFrame {

    private PanelConsola panelConsola;
    public PanelDibujo panelDibujo;
    private Capa capa;
    
    private JMenuItem itemMaximizar;
    private JMenu menuVer; 
    private boolean isMaximized = false;

    // Constante para el enlace de GitHub
    private static final String GITHUB_LINK = "https://github.com/marquistallman/-Pixel-Kinesis/tree/main/src/main/java/com/mycompany/pixelkinesis";

    public VentanaEditor() {

        setTitle("PixelKinesis Editor");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JMenuBar barra = new JMenuBar();

        JMenu menuArchivo = crearMenuArchivo();
        JMenu menuEdicion = crearMenuEdicion();
        menuVer = crearMenuVer();
        JMenu menuComandos = crearMenuComandos();
        
        barra.add(menuArchivo);
        barra.add(menuEdicion);
        barra.add(menuVer);
        barra.add(menuComandos);

        setJMenuBar(barra);

        panelConsola = new PanelConsola();
        add(panelConsola, BorderLayout.WEST);
        
        JButton runButton = panelConsola.getRunButton();
        runButton.addActionListener(e -> ejecutarComandos());
        
        panelDibujo = new PanelDibujo();
        capa = new Capa();
        panelDibujo.setCapa(capa);
        add(panelDibujo, BorderLayout.CENTER);
        
        redirigirSalidaAUI();
    }
    
    private JMenu crearMenuArchivo() {
        JMenu menuArchivo = new JMenu("Archivo");

        JMenuItem itemNuevo = new JMenuItem("Nuevo (Limpiar todo)");
        itemNuevo.addActionListener(e -> {
            panelConsola.consolaEntrada.setText("");
            panelConsola.consolaSalida.setText("");
            capa.limpiar();
            panelDibujo.repaint();
            System.out.println("✅ Editor de comandos reiniciado.");
        });
        
        JMenuItem itemExportar = new JMenuItem("Exportar Dibujo (PNG...)");
        itemExportar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Funcionalidad de exportar no implementada (WIP).", "Advertencia", JOptionPane.WARNING_MESSAGE);
        });

        menuArchivo.add(itemNuevo);
        menuArchivo.addSeparator();
        menuArchivo.add(itemExportar);
        
        return menuArchivo;
    }
    
    private JMenu crearMenuEdicion() {
        JMenu menuEdicion = new JMenu("Edición");

        JMenuItem itemLimpiarEntrada = new JMenuItem("Limpiar Código de Entrada");
        itemLimpiarEntrada.addActionListener(e -> {
            panelConsola.consolaEntrada.setText("");
            System.out.println("✅ Código de entrada limpiado.");
        });
        
        JMenuItem itemLimpiarSalida = new JMenuItem("Limpiar Log de Salida");
        itemLimpiarSalida.addActionListener(e -> {
            panelConsola.consolaSalida.setText("");
            System.out.println("✅ Log de salida limpiado.");
        });

        menuEdicion.add(itemLimpiarEntrada);
        menuEdicion.add(itemLimpiarSalida);
        
        return menuEdicion;
    }

    private JMenu crearMenuVer() {
        JMenu menuVer = new JMenu("Ver");
        
        itemMaximizar = new JMenuItem("Maximizar Área de Dibujo");
        itemMaximizar.addActionListener(e -> alternarVistaDibujo());
        
        // --- INICIO: Nuevo elemento para GitHub ---
        JMenuItem itemGitHub = new JMenuItem("Abrir Proyecto en GitHub");
        itemGitHub.addActionListener(e -> abrirEnlaceGitHub());
        // --- FIN: Nuevo elemento para GitHub ---

        menuVer.add(itemMaximizar);
        menuVer.addSeparator(); // Separador para organizar mejor el menú
        menuVer.add(itemGitHub); 
        
        return menuVer;
    }
    
    /**
     * Intenta abrir el enlace de GitHub en el navegador web predeterminado del sistema.
     */
    private void abrirEnlaceGitHub() {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(GITHUB_LINK));
                System.out.println("🌐 Abriendo enlace de GitHub: " + GITHUB_LINK);
            } else {
                System.err.println("❌ Error: Navegador no soportado en este sistema. Enlace: " + GITHUB_LINK);
                JOptionPane.showMessageDialog(this, "No se pudo abrir el navegador. Copie el siguiente enlace:\n" + GITHUB_LINK, "Error de Navegación", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            System.err.println("❌ Error al intentar abrir el enlace de GitHub: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error al abrir el enlace. Copie el siguiente enlace:\n" + GITHUB_LINK, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void alternarVistaDibujo() {
        if (!isMaximized) { 
            remove(panelConsola);
            itemMaximizar.setText("Restaurar Vista Normal");
            menuVer.setText("Ver (MAXIMIZADO)");
            isMaximized = true;
            System.out.println("🖼️ Vista de dibujo maximizada.");
        } else { 
            add(panelConsola, BorderLayout.WEST); 
            itemMaximizar.setText("Maximizar Área de Dibujo");
            menuVer.setText("Ver");
            isMaximized = false;
            System.out.println("🖼️ Vista restaurada a la normalidad.");
        }
        revalidate();
        repaint();
    }
    
    private JMenu crearMenuComandos() {
        JMenu menuComandos = new JMenu("Ayuda de Comandos (Guía)");
        
        JMenu menuFiguras = new JMenu("🖍️ Figuras Geométricas (Creación)");
        
        JMenuItem itemCuad = new JMenuItem("cuad: Cuadrado/Rectángulo");
        itemCuad.addActionListener(e -> mostrarAyudaFigura("cuad", "cuad (pos_x, pos_y) (ancho, alto)", 
                "Crea un rectángulo/cuadrado. La posición va primero, luego las dimensiones.\\nEjemplo: cuad 100,150 50,80"));
        menuFiguras.add(itemCuad);

        JMenuItem itemCirc = new JMenuItem("circ: Círculo/Elipse");
        itemCirc.addActionListener(e -> mostrarAyudaFigura("circ", "circ (radio_x, radio_y) (pos_x, pos_y)", 
                "Crea una elipse o un círculo (si radio_x = radio_y).\\nEjemplo: circ 40,40 200,200"));
        menuFiguras.add(itemCirc);
        
        JMenuItem itemTri = new JMenuItem("tri: Triángulo");
        itemTri.addActionListener(e -> mostrarAyudaFigura("tri", "tri (x1,y1) (x2,y2) (x3,y3)", 
                "Crea un triángulo usando tres coordenadas absolutas.\\nEjemplo: tri 10,10 50,50 10,50"));
        menuFiguras.add(itemTri);

        JMenuItem itemComp = new JMenuItem("Bloque: _Composed");
        itemComp.addActionListener(e -> mostrarAyudaFigura("_Composed", "_Composed ... _ComposedEnd", 
                "Define una figura compuesta. Las figuras internas se agrupan y se mueven juntas.\\nEjemplo:\\n_Composed\\n  cuad 10,10 0,0\\n  circ 5,5 10,0\\n_ComposedEnd"));
        menuFiguras.add(itemComp);
        
        menuComandos.add(menuFiguras);
        menuComandos.addSeparator();
        
        JMenu menuModificadores = new JMenu("✨ Modificadores (-Comandos)");
        
        JMenuItem itemMover = new JMenuItem("-mover: Traslación");
        itemMover.addActionListener(e -> mostrarAyudaComando("-mover", "-mover (offset_x, offset_y)", 
                "Mueve la última figura creada una cantidad relativa (offset).\\nEjemplo: -mover 20,-10"));
        menuModificadores.add(itemMover);
        
        JMenuItem itemRotar = new JMenuItem("-rotar: Rotación");
        itemRotar.addActionListener(e -> mostrarAyudaComando("-rotar", "-rotar (grados)", 
                "Rota la última figura creada alrededor de su centro.\\nEjemplo: -rotar 45"));
        menuModificadores.add(itemRotar);
        
        JMenuItem itemEscalar = new JMenuItem("-escalar: Escalamiento");
        itemEscalar.addActionListener(e -> mostrarAyudaComando("-escalar", "-escalar (factor_x, factor_y)", 
                "Cambia el tamaño de la última figura creada.\\nEjemplo: -escalar 2,1.5 (Duplica el ancho, 1.5 veces el alto)"));
        menuModificadores.add(itemEscalar);
        
        JMenuItem itemColor = new JMenuItem("-color: Color de relleno");
        itemColor.addActionListener(e -> mostrarAyudaComando("-color", "-color (R,G,B)", 
                "Establece el color de relleno usando valores RGB (0-255).\\nEjemplo: -color 255,0,0 (Rojo)"));
        menuModificadores.add(itemColor);
        
        JMenuItem itemBorde = new JMenuItem("-borde: Color de borde");
        itemBorde.addActionListener(e -> mostrarAyudaComando("-borde", "-borde (R,G,B) [grosor]", 
                "Establece el color y opcionalmente el grosor del borde (línea).\\nEjemplo: -borde 0,0,0 2 (Borde negro de grosor 2)"));
        menuModificadores.add(itemBorde);

        JMenuItem itemAnim = new JMenuItem("-animate: Bloque de animación");
        itemAnim.addActionListener(e -> mostrarAyudaComando("-animate", "-animate ... -animateEnd", 
                "Define un bloque de comandos que deben ser animados a lo largo del tiempo. Contiene comandos especiales (animate mover, animate rotar).\\nEjemplo:\\n-animate\\n  animate mover 50,0 1000\\n-animateEnd"));
        menuModificadores.add(itemAnim);

        menuComandos.add(menuModificadores);
        
        return menuComandos;
    }

    private void mostrarAyudaFigura(String titulo, String sintaxis, String descripcion) {
        String mensaje = "<html>"
                + "<h2>" + titulo.toUpperCase() + "</h2>"
                + "<p><strong>Sintaxis:</strong> <code>" + sintaxis + "</code></p>"
                + "<p><strong>Descripción:</strong> " + descripcion.replace("\\n", "<br>") + "</p>"
                + "</html>";

        JOptionPane.showMessageDialog(this, mensaje, "Ayuda de Figura: " + titulo.toUpperCase(), JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarAyudaComando(String titulo, String sintaxis, String descripcion) {
        String mensaje = "<html>"
                + "<h2>" + titulo.toUpperCase() + "</h2>"
                + "<p><strong>Sintaxis:</strong> <code>" + sintaxis + "</code></p>"
                + "<p><strong>Descripción:</strong> " + descripcion.replace("\\n", "<br>") + "</p>"
                + "</html>";

        JOptionPane.showMessageDialog(this, mensaje, "Ayuda de Comando: " + titulo.toUpperCase(), JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void redirigirSalidaAUI() {
        PrintStream psOut = new PrintStream(new CustomOutputStream(panelConsola.consolaSalida, Color.BLACK));
        System.setOut(psOut);

        PrintStream psErr = new PrintStream(new CustomOutputStream(panelConsola.consolaSalida, new Color(180, 0, 0))); 
        System.setErr(psErr);
    }

    private void ejecutarComandos() {
        panelConsola.consolaSalida.setText(""); 
        
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

            FiguraGeometrica fig = createFiguraFromLine(linea);
            if (fig != null) {
                capa.agregarNodo(fig);
                ultimoNodoCreado = fig;
            }
            i++;
        }

        panelDibujo.repaint();
    }    

    private Nodo lastBlockCreated = null; 

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

                Comando c = createCommandSafe(nombreCmd, paramsCmd);
                if (c != null) {
                    ultimoInterno.comandos.add(c);
                    System.out.println("➕ Comando agregado a figura interna: " + nombreCmd + " -> " + c.getClass().getSimpleName());
                }

                i++; continue;
            }

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

            java.awt.Shape shape = forma.getShape();
            java.awt.geom.Rectangle2D bounds = shape.getBounds2D();
            int baseX = (int) Math.round(bounds.getX());
            int baseY = (int) Math.round(bounds.getY());

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
    
    private void agregarNodo(String comandoCrear, ArrayList<String> paramsCrear, ArrayList<Comando> comandosNodo) {
        Forma forma = Compiler.forma(comandoCrear, paramsCrear);
        AreaDeInfluencia area = new AreaDeInfluencia(forma);
        FiguraGeometrica nodo = new FiguraGeometrica(forma, area, comandosNodo);
        capa.agregarNodo(nodo);
    }    
}