/**
 * This software is free. You can use it without any limitations, but I don't give any kind of warranties!
 */

package org.freeware.monakhov.game3d;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Arrays;
import org.freeware.monakhov.game3d.maps.Line;
import org.freeware.monakhov.game3d.maps.Point;
import org.freeware.monakhov.game3d.maps.Room;
import org.freeware.monakhov.game3d.maps.World;

/**
 * This is a Graphics Engine
 * @author Vasily Monakhov 
 */
public class GraphicsEngine {
    
    private final World world;
    
    private final Hero hero;
    
    private final Screen screen;
    
    private final double perspective;
    
    private final Line[] mapLines;
    private final Point[] rayPoints;
    private final Point[] transformedRayPoints;
    private final Point[] intersectPoints;
    
    GraphicsEngine(World world, Hero hero, Screen screen) {
        this.world = world;
        this.hero = hero;
        this.screen = screen;
        mapLines = new Line[screen.getWidth()];
        perspective = screen.getWidth() / 2;
        rayPoints = new Point[screen.getWidth()];
        int index1 = screen.getWidth() / 2 - 1;
        int index2 = index1 + 1;
        int steps = index2;
        for (int i = 0; i < steps; i++) {
            rayPoints[index1 - i] = new Point(-0.5 - i, perspective);
            rayPoints[index2 + i] = new Point(0.5 + i, perspective);
        }
        transformedRayPoints = new Point[screen.getWidth()];
        intersectPoints = new Point[screen.getWidth()];
        for (int i = 0; i < screen.getWidth(); i++) {
            intersectPoints[i] = new Point();
            transformedRayPoints[i] = new Point();
        }
    }
    
    void clearRender() {
        Arrays.fill(mapLines, null);
    }
    
    void checkVisibleRooms() {
        hero.getRoom().checkVisibility(mapLines, hero.getPosition(), transformedRayPoints, intersectPoints);
    }
    
    final double wh = 10;
    
    void renderWalls() {
        Graphics2D g = (Graphics2D)screen.getDoubleImage().getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        for (int i = 0; i < mapLines.length; i++) {
            Line l = mapLines[i];
            if (l != null) {
                double dist = SpecialMath.lineLength(hero.getPosition(), intersectPoints[i]);
                double k = SpecialMath.lineLength(hero.getPosition(), transformedRayPoints[i]);
                double h = wh * k / dist;
                int ch = (int)Math.round((screen.getHeight() - h) / 2);
                if (h < screen.getHeight()) {
                    if (ch > 0) {
                        g.setColor(Color.LIGHT_GRAY);
                        g.drawRect(i, 0, 1, ch);
                        g.setColor(Color.DARK_GRAY);
                        g.drawRect(i, (int)Math.round(h + ch), 1, ch);                        
                    }
                }
                g.drawImage(l.getSubImage(intersectPoints[i]), i, ch, 1, (int)Math.round(h), null);                    
            }
        }
    }
    
    private boolean mapEnabled;    
    
    void toggleMap() {
        mapEnabled = !mapEnabled;
    }
    
    void render() {
        renderWalls();
        if (mapEnabled) {
            drawMap();        
        }
        screen.doubleBufferToScreen();
    }
    
    void transform() {
        double sin = Math.sin(-hero.getAsimuth());
        double cos = Math.cos(-hero.getAsimuth());
        for (int i = 0; i < screen.getWidth(); i++) {
            double x = rayPoints[i].getX();
            double y = rayPoints[i].getY();
            double new_x = x * cos - y * sin + hero.getPosition().getX();
            double new_y = y * cos + x * sin + hero.getPosition().getY();
            transformedRayPoints[i].moveTo(new_x, new_y);
        }        
    }
    
    void doCycle() {
        world.prepareForVisibilityCheck();
        clearRender();
        transform();
        checkVisibleRooms();
        render();
    }

    /**
     * @return the perspective
     */
    public double getPerspective() {
        return perspective;
    }

    BasicStroke LINE = new BasicStroke(1);
    BasicStroke WALL = new BasicStroke(3);    
    
    void drawMap() {
        Graphics2D g = (Graphics2D)screen.getDoubleImage().getGraphics();
        int dx = screen.getDoubleImage().getWidth() / 2;
        int dy = screen.getDoubleImage().getHeight() / 2;
        g.setColor(Color.red);
        g.fillOval(dx-2, dy-2, 4, 4);  
        dx +=  - (int)hero.getPosition().getX() * 10;
        dy += (int)hero.getPosition().getY() * 10;
        g.setColor(Color.GREEN);
        for (Room r : world.getAllRooms()) {
            for (Line l : r.getAllLines()) {
                if (l.isVisible()) g.setStroke(WALL);
                else g.setStroke(LINE);
                g.drawLine(dx + (int)l.getStart().getX() * 10, dy - (int)l.getStart().getY() * 10, 
                        dx + (int)l.getEnd().getX() * 10, dy - (int)l.getEnd().getY() * 10);
            }
        }

        
    }
    
    
}
