package org.freeware.monakhov.game3d;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author Vasily Monakhov
 */
public class GraphicsUtils {

    public static void drawString(Graphics2D g, String str, int x, int y, Color faceColor) {
        g.setColor(faceColor);
        g.drawString(str, x, y);
    }

    public static void drawCenteredString(Graphics2D g, String str, int centerX, int centerY, Color faceColor) {
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(str, g);
        g.setColor(faceColor);
        g.drawString(str, (int) (centerX - r.getCenterX()), (int) (centerY - r.getCenterY()));
    }

    public static void drawStringWithShadow(Graphics2D g, String str, int x, int y, Color faceColor, int dx, int dy, Color shadowColor) {
        drawString(g, str, x + dx, y + dy, shadowColor);
        drawString(g, str, x - dx, y - dy, faceColor);
    }

    public static void drawCenteredStringWithShadow(Graphics2D g, String str, int centerX, int centerY, Color faceColor, int dx, int dy, Color shadowColor) {
        drawCenteredString(g, str, centerX + dx / 2, centerY + dy / 2, shadowColor);
        drawCenteredString(g, str, centerX - dx / 2, centerY - dy / 2, faceColor);
    }

    public static void drawCenteredStringWithOutline(Graphics2D g, String str, int centerX, int centerY, Color faceColor, int delta, Color shadowColor) {
        double a = 0;
        int steps = 8 * delta;
        double da = 2 * Math.PI / steps;
        for (int i = 0; i < steps; i++) {
            drawCenteredString(g, str, (int) Math.round(centerX + delta * Math.sin(a)), (int) Math.round(centerY + delta * Math.cos(a)), shadowColor);
            drawCenteredString(g, str, (int) Math.round(centerX + delta / 2 * Math.sin(a)), (int) Math.round(centerY + delta / 2 * Math.cos(a)), shadowColor);
            a += da;
        }
        drawCenteredString(g, str, centerX, centerY, faceColor);
    }

}
