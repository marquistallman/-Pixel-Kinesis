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
    
        // ---- FLAGS ----
        boolean enComposed = false;
    
        // ---- BUFFERS ----
        ArrayList<FiguraGeometrica> bufferComposed = new ArrayList<>();
        ArrayList<Comando> bufferAnimate = new ArrayList<>();
    
    
        // ============================================================
        // RECORRER LÍNEAS
        // ============================================================
        int i = 0;
        while (i < lineas.length) {
            String raw = lineas[i];
            String linea = raw.trim();
            
            if (linea.isEmpty()) {
                i++;
                continue;
            }
    
    
            // ============================================================
            // BLOQUE COMPOSED
            // ============================================================
            if (linea.equalsIgnoreCase("_Composed")) {
                enComposed = true;
                bufferComposed = new ArrayList<>();
                i++;
                continue;
            }
    
            if (linea.equalsIgnoreCase("_ComposedEnd")) {
    
                enComposed = false;
    
                if (!bufferComposed.isEmpty()) {
                    ComposedFigures comp = CompilerComposed.crearFiguraCompuesta(bufferComposed);
                    capa.agregarNodo(comp);
                    ultimoNodoCreado = comp;
                }
    
                i++;
                continue;
            }
    
    
            if (enComposed) {
                // SOLO se permiten líneas de creación de figuras
                if (linea.startsWith("-")) {
                    i++;
                    continue;
                }
    
                String[] partes = linea.split(" ");
                String tipo = partes[0];
    
                ArrayList<String> params = new ArrayList<>();
                for (int j = 1; j < partes.length; j++)
                    params.add(partes[j]);
    
                Forma forma = Compiler.forma(tipo, params);
                AreaDeInfluencia area = new AreaDeInfluencia(forma);
    
                ArrayList<Comando> cmds = new ArrayList<>();
                cmds.add(new com.mycompany.pixelkinesis.comandos.ComandoDibujar());
    
                FiguraGeometrica fig = new FiguraGeometrica(forma, area, cmds);
                bufferComposed.add(fig);
    
                i++;
                continue;
            }
    
    
            // ============================================================
            // BLOQUE ANIMATE
            // ============================================================
            if (linea.equalsIgnoreCase("-animate")) {
                System.out.println("🎬 Bloque -animate iniciado");
                bufferAnimate = new ArrayList<>();
                
                // Procesar todas las líneas hasta encontrar -animate end o -animateEnd
                i++; // Avanzar a la siguiente línea
                while (i < lineas.length) {
                    String rawAnimate = lineas[i];
                    String lineaAnimate = rawAnimate.trim();
                    
                    // Verificar si es el cierre del bloque
                    if (lineaAnimate.equalsIgnoreCase("-animateEnd") || lineaAnimate.equalsIgnoreCase("-animate end")) {
                        System.out.println("🏁 Bloque -animateEnd detectado");
                        
                        if (ultimoNodoCreado == null) {
                            System.err.println("❌ Error: No hay nodo creado para agregar animaciones");
                        } else if (bufferAnimate.isEmpty()) {
                            System.err.println("⚠ Advertencia: El buffer de animaciones está vacío");
                        } else {
                            // agregar todos los comandos del bloque al nodo
                            System.out.println("📦 Agregando " + bufferAnimate.size() + " comandos al nodo");
                            ultimoNodoCreado.comandos.addAll(bufferAnimate);
                            
                            // Ejecutar las animaciones inmediatamente (solo las de tipo Animate)
                            int animacionesEjecutadas = 0;
                            for (Comando cmd : bufferAnimate) {
                                System.out.println("🔍 Revisando comando: " + cmd.getClass().getSimpleName() + " (es Animate? " + (cmd instanceof Animate) + ")");
                                if (cmd instanceof Animate) {
                                    System.out.println("🎬 Ejecutando animación: " + cmd.getClass().getSimpleName());
                                    // Ejecutar la animación inmediatamente con Graphics2D null
                                    // (las animaciones no lo necesitan para iniciar el Timer)
                                    cmd.ejecutar(ultimoNodoCreado, null);
                                    animacionesEjecutadas++;
                                }
                            }
                            System.out.println("✅ Se ejecutaron " + animacionesEjecutadas + " animaciones de " + bufferAnimate.size() + " comandos totales");
                        }
                        
                        i++; // Avanzar después del cierre
                        break; // Salir del while
                    }
                    
                    // Si la línea está vacía, continuar
                    if (lineaAnimate.isEmpty()) {
                        i++;
                        continue;
                    }
                    
                    // Dentro de animate SOLO se aceptan líneas que inician con "-"
                    if (!lineaAnimate.startsWith("-")) {
                        System.out.println("⚠ Línea ignorada en bloque animate (no empieza con -): " + lineaAnimate);
                        i++;
                        continue;
                    }
    
                    String cmdLine = lineaAnimate.substring(1).trim();
                    String[] partes = cmdLine.split(" ");
                    String nombre = partes[0];
    
                    ArrayList<String> params = new ArrayList<>();
                    for (int j = 1; j < partes.length; j++)
                        params.add(partes[j]);
    
                    // Si el comando es "mover" dentro del bloque animate, convertirlo a "animate mover"
                    if (nombre.equalsIgnoreCase("mover")) {
                        System.out.println("🔄 Convirtiendo 'mover' a 'animate mover' dentro del bloque animate");
                        nombre = "animate";
                        // Agregar "mover" como primer parámetro
                        params.add(0, "mover");
                    }
    
                    System.out.println("🔧 Procesando comando en bloque animate: nombre='" + nombre + "', params=" + params);
                    
                    // Procesar parámetros con comas (ej: "100,100" -> ["100", "100"])
                    ArrayList<String> paramsProcesados = new ArrayList<>();
                    for (String param : params) {
                        if (param.contains(",")) {
                            // Dividir por comas y agregar cada parte
                            String[] partesComa = param.split(",");
                            for (String parte : partesComa) {
                                String trimed = parte.trim();
                                if (!trimed.isEmpty()) {
                                    paramsProcesados.add(trimed);
                                }
                            }
                        } else {
                            paramsProcesados.add(param.trim());
                        }
                    }
                    
                    System.out.println("🔧 Parámetros procesados: " + paramsProcesados);
                    
                    // ⚡ AQUI SÍ → el compiler crea el comando correcto (Mover, AnimateMover, lo que sea)
                    Comando c = Compiler.comando(nombre, paramsProcesados);

                    // Verificar que el comando no sea null antes de agregarlo
                    if (c != null) {
                        bufferAnimate.add(c);
                        System.out.println("✅ Comando agregado al buffer: " + c.getClass().getSimpleName());
                    } else {
                        System.err.println("⚠ Advertencia: No se pudo crear el comando '" + nombre + "' con params=" + paramsProcesados + ". Se omite.");
                    }
                    
                    i++; // Avanzar a la siguiente línea
                }
                
                continue; // Continuar con el siguiente elemento del bucle principal
            }
    
    
            // ============================================================
            // COMANDOS NORMALES (fuera de blocks)
            // ============================================================
    
            if (linea.startsWith("-")) {
    
                if (ultimoNodoCreado == null) {
                    i++;
                    continue;
                }
    
                String cmdLine = linea.substring(1).trim();
                String[] partes = cmdLine.split(" ");
                String nombre = partes[0];
    
                ArrayList<String> params = new ArrayList<>();
                for (int j = 1; j < partes.length; j++)
                    params.add(partes[j]);
    
                Comando c = Compiler.comando(nombre, params);
                
                // Verificar que el comando no sea null antes de agregarlo
                if (c != null) {
                    ultimoNodoCreado.comandos.add(c);
                } else {
                    System.err.println("⚠ Advertencia: No se pudo crear el comando '" + nombre + "'. Se omite.");
                }
    
                i++;
                continue;
            }
    
    
            // ============================================================
            // CREACIÓN DE FIGURAS
            // ============================================================
    
            String[] partes = linea.split(" ");
            String tipo = partes[0];
    
            ArrayList<String> params = new ArrayList<>();
            for (int j = 1; j < partes.length; j++)
                params.add(partes[j]);
    
            Forma forma = Compiler.forma(tipo, params);
            AreaDeInfluencia area = new AreaDeInfluencia(forma);
    
            ArrayList<Comando> cmds = new ArrayList<>();
            cmds.add(new com.mycompany.pixelkinesis.comandos.ComandoDibujar());
    
            FiguraGeometrica fig = new FiguraGeometrica(forma, area, cmds);
            capa.agregarNodo(fig);
    
            ultimoNodoCreado = fig;
            i++;
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

