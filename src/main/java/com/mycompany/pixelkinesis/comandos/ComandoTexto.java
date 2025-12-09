package com.mycompany.pixelkinesis.comandos;
import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import com.mycompany.pixelkinesis.*;
import com.mycompany.pixelkinesis.ComposedFigures.ComposedFigures;

public class ComandoTexto extends Comando {
    private final String texto;
    private final int tamanoFuente;
    private final String nombreFuente;
    private final int estiloFuente;
    private final Point posicion;
    private final ComandoDibujar reload = new ComandoDibujar();
    
    /**
     * Constructor completo con todos los parámetros
     * @param texto El texto a mostrar
     * @param tamanoFuente Tamaño de la fuente
     * @param nombreFuente Nombre de la fuente (por defecto "Arial")
     * @param estiloFuente Estilo: Font.PLAIN, Font.BOLD, Font.ITALIC
     * @param posicion Posición donde dibujar el texto (null para usar posición del nodo)
     */
    public ComandoTexto(String texto, int tamanoFuente, String nombreFuente, int estiloFuente, Point posicion) {
        if (texto == null || texto.isEmpty()) {
            throw new IllegalArgumentException("El texto no puede ser null o vacío");
        }
        this.texto = texto;
        this.tamanoFuente = Math.max(1, tamanoFuente);
        this.nombreFuente = (nombreFuente != null && !nombreFuente.isEmpty()) ? nombreFuente : "Arial";
        this.estiloFuente = estiloFuente;
        this.posicion = posicion;
    }
    
    /**
     * Constructor simplificado con texto y tamaño
     * @param texto El texto a mostrar
     * @param tamanoFuente Tamaño de la fuente
     */
    public ComandoTexto(String texto, int tamanoFuente) {
        this(texto, tamanoFuente, "Arial", Font.PLAIN, null);
    }
    
    /**
     * Constructor con texto, tamaño y posición
     * @param texto El texto a mostrar
     * @param tamanoFuente Tamaño de la fuente
     * @param posicion Posición donde dibujar
     */
    public ComandoTexto(String texto, int tamanoFuente, Point posicion) {
        this(texto, tamanoFuente, "Arial", Font.PLAIN, posicion);
    }
    
    @Override
    public void ejecutar(Nodo nodo, Graphics2D g) {
        if (nodo == null || g == null) return;
        
        // Asegurar que el nodo tenga area inicializada
        if (nodo.area == null) {
            reload.ejecutar(nodo, g);
        }
        
        // Crear la fuente
        Font fuente = new Font(nombreFuente, estiloFuente, tamanoFuente);
        
        // Determinar la posición del texto
        Point pos = posicion;
        if (pos == null && nodo.area != null) {
            pos = nodo.area.getPosicion();
        }
        if (pos == null) {
            pos = new Point(0, 0);
        }
        
        // Si es una FiguraGeometrica, convertir el texto en un shape
        if (nodo instanceof FiguraGeometrica fig) {
            // Crear el outline del texto como Shape
            FontRenderContext frc = g.getFontRenderContext();
            GlyphVector gv = fuente.createGlyphVector(frc, texto);
            Shape shapeTexto = gv.getOutline(pos.x, pos.y);
            
            // Guardar el shape original si no existe
            if (fig.forma.shapeOriginal == null) {
                fig.forma.shapeOriginal = fig.forma.shape;
            }
            
            // Actualizar el shape de la figura con el texto
            fig.forma.shape = shapeTexto;
            
            // Actualizar el área de influencia
            if (fig.area != null) {
                fig.area.setPosicion(pos);
            }
            
            // Forzar repaint completo
            if (ComandoMover.refrescarUI != null) {
                ComandoMover.refrescarUI.run();
            }
            
            reload.ejecutar(nodo, g);
            return;
        }
        
        // Para otros tipos de nodos, dibujar el texto directamente
        if (nodo instanceof ComposedFigures composed) {
            // Aplicar texto a cada figura del composed
            for (FiguraGeometrica f : composed.getFiguras()) {
                aplicarTextoAFigura(f, g, fuente, pos);
            }
            
            composed.recalcularArea();
            
            // Forzar repaint completo
            if (ComandoMover.refrescarUI != null) {
                ComandoMover.refrescarUI.run();
            }
            
            reload.ejecutar(nodo, g);
            return;
        }
        
        // Dibujar texto simple
        dibujarTexto(nodo, g, fuente, pos);
    }
    
    /**
     * Aplica el texto a una figura geométrica específica
     */
    private void aplicarTextoAFigura(FiguraGeometrica fig, Graphics2D g, Font fuente, Point pos) {
        if (fig == null) return;
        
        FontRenderContext frc = g.getFontRenderContext();
        GlyphVector gv = fuente.createGlyphVector(frc, texto);
        
        Point posicionFigura = (fig.area != null && fig.area.getPosicion() != null) 
            ? fig.area.getPosicion() 
            : pos;
            
        Shape shapeTexto = gv.getOutline(posicionFigura.x, posicionFigura.y);
        
        if (fig.forma.shapeOriginal == null) {
            fig.forma.shapeOriginal = fig.forma.shape;
        }
        
        fig.forma.shape = shapeTexto;
    }
    
    /**
     * Dibuja el texto directamente en el Graphics2D
     */
    private void dibujarTexto(Nodo nodo, Graphics2D g, Font fuente, Point pos) {
        // Guardar estado anterior
        Font prevFont = g.getFont();
        Color prevColor = g.getColor();
        
        // Aplicar fuente
        g.setFont(fuente);
        
        // Aplicar color del frente si existe
        if (nodo.area != null && nodo.area.frente != null) {
            Color color = nodo.area.frente.getColor();
            float opacidad = nodo.area.frente.getOpacidad();
            
            Color colorConOpacidad = new Color(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                (int)(255 * opacidad)
            );
            g.setColor(colorConOpacidad);
        }
        
        // Dibujar el texto
        g.drawString(texto, pos.x, pos.y);
        
        // Restaurar estado anterior
        g.setFont(prevFont);
        g.setColor(prevColor);
        
        // Forzar repaint completo
        if (ComandoMover.refrescarUI != null) {
            ComandoMover.refrescarUI.run();
        }
    }
}

