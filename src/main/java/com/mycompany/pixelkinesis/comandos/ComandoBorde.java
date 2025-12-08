package com.mycompany.pixelkinesis.comandos;
import java.awt.*;
import java.awt.geom.*;
import com.mycompany.pixelkinesis.*;
import com.mycompany.pixelkinesis.ComposedFigures.ComposedFigures;

public class ComandoBorde extends Comando {
    private final Color colorBorde;
    private final float grosorBorde;
    private final ComandoDibujar reload = new ComandoDibujar();
    
    /**
     * Constructor para crear un borde con color y grosor específicos
     * @param color Color del borde
     * @param grosor Grosor del borde en píxeles
     */
    public ComandoBorde(Color color, float grosor) {
        this.colorBorde = color;
        this.grosorBorde = Math.max(0f, grosor); // Asegurar que el grosor no sea negativo
    }
    
    /**
     * Constructor para crear un borde con color (grosor por defecto 2.0)
     * @param color Color del borde
     */
    public ComandoBorde(Color color) {
        this(color, 2.0f);
    }
    
    @Override
    public void ejecutar(Nodo nodo, Graphics2D g) {
        if (nodo == null || g == null) return;
        
        // Asegurar que el nodo tenga area inicializada
        if (nodo.area == null) {
            reload.ejecutar(nodo, g);
        }
        
        // Aplicar borde a figuras compuestas
        if (nodo instanceof ComposedFigures composed) {
            for (FiguraGeometrica f : composed.getFiguras()) {
                aplicarBorde(f, g);
            }
            
            // Forzar repaint completo
            if (ComandoMover.refrescarUI != null) {
                ComandoMover.refrescarUI.run();
            }
            
            return;
        }
        
        // Aplicar borde a figura individual
        if (nodo instanceof FiguraGeometrica fig) {
            aplicarBorde(fig, g);
            
            // Forzar repaint completo
            if (ComandoMover.refrescarUI != null) {
                ComandoMover.refrescarUI.run();
            }
        }
    }
    
    /**
     * Aplica el borde a una figura geométrica específica
     */
    private void aplicarBorde(FiguraGeometrica fig, Graphics2D g) {
        if (fig == null || fig.forma == null) return;
        
        // Guardar estado anterior
        Color prevColor = g.getColor();
        Stroke prevStroke = g.getStroke();
        
        // Configurar el borde
        g.setColor(colorBorde);
        g.setStroke(new BasicStroke(
            grosorBorde,
            BasicStroke.CAP_ROUND,
            BasicStroke.JOIN_ROUND
        ));
        
        // Obtener el shape con todas las transformaciones aplicadas
        Shape shape = fig.forma.shape;
        
        // Aplicar posición si existe y no está ya en el shape
        if (fig.forma.shapeOriginal != null) {
            // El shape ya incluye todas las transformaciones
            shape = fig.forma.shape;
        } else if (fig.area != null && fig.area.getPosicion() != null) {
            Point p = fig.area.getPosicion();
            AffineTransform at = AffineTransform.getTranslateInstance(p.x, p.y);
            shape = at.createTransformedShape(shape);
        }
        
        // Dibujar el borde
        g.draw(shape);
        
        // Restaurar estado anterior
        g.setColor(prevColor);
        g.setStroke(prevStroke);
    }
}
