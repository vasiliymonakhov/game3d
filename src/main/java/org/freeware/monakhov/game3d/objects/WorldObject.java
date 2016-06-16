package org.freeware.monakhov.game3d.objects;

import org.freeware.monakhov.game3d.objects.movable.Hero;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import org.freeware.monakhov.game3d.GraphicsEngine;
import org.freeware.monakhov.game3d.SpecialMath;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Room;
import org.freeware.monakhov.game3d.map.Sprite;

/**
 * Объект мира
 * @author Vasily Monakhov 
 */
abstract public class WorldObject {
    
    protected final Point position;
    protected final Point oldPosition;
    
    protected double asimuth;
    
    protected Room room;

    /**
     * @param position the position to set
     */
    public WorldObject (Point position) {
        if (position == null) {
            throw new IllegalArgumentException("Position may not be null");
        }        
        this.position = position;
        oldPosition = new Point();
    }

    /**
     * @return the asimuth
     */
    public double getAsimuth() {
        return asimuth;
    }

    /**
     * @param asimuth the asimuth to set
     */
    public void setAsimuth(double asimuth) {
        this.asimuth = asimuth % (2 * Math.PI);
    }

    /**
     * @return the room
     */
    public Room getRoom() {
        return room;
    }

    /**
     * @param room the room to set
     */
    public void setRoom(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("Room may not be null");
        }
        this.room = room;
    }

    /**
     * @return the position
     */
    public Point getPosition() {
        return position;
    }
    
    abstract public Sprite getSprite();
    
    abstract public void moveBy(double df, double ds);
    
    public void render(Graphics2D g, int screenHeight, Hero hero, Point[] transformedRayPoints, Point[] intersectPoints) {
        Point s = new Point();
        Point e = new Point();
        Sprite sprite = getSprite();                    
        int sw2 = sprite.getWidth() / 2;
        double deltaX =  sw2 * Math.cos(-hero.getAsimuth());
        double deltaY = sw2 * Math.sin(-hero.getAsimuth());
        
        s.moveTo(position.getX() - deltaX, position.getY() - deltaY);        
        e.moveTo(position.getX() + deltaX, position.getY() + deltaY);
        Point p = new Point();
        for (int i = 0; i < transformedRayPoints.length; i++) {
            if (SpecialMath.lineIntersection(s, e, hero.getPosition(), intersectPoints[i], p)) {
                if (p.between(s, e) && p.between(hero.getPosition(), intersectPoints[i])) {
                    // точка пересечения трассирующего луча и линии объекта находится внутри луча и границ объекта
                    // значит этот объект виден
                    double dist = SpecialMath.lineLength(hero.getPosition(), p);
                    double k = SpecialMath.lineLength(hero.getPosition(), transformedRayPoints[i]);
                    double h = GraphicsEngine.WALL_SIZE * k / (dist * GraphicsEngine.KORRECTION);
                    double sh = h * sprite.getHeight() / GraphicsEngine.WALL_SIZE;
                    double syo = h * sprite.getYOffset() / GraphicsEngine.WALL_SIZE;
                    double ch = (int) Math.round((screenHeight - h) / 2);
                    long xOffset = Math.round(SpecialMath.lineLength(s, p));
                    int spriteXOffset = (int)(xOffset % sprite.getWidth());
                    g.drawImage(sprite.getSubImage(spriteXOffset, 0), i, (int)(ch + syo), 1, (int)sh, null);                    
                }
            }
        }
    }
    
    public double distanceTo(Point p) {
        return SpecialMath.lineLength(p, position);
    }

    public boolean isCrossable() {
        return false;
    }
    
    /**
     * Возвращает радиус окружности, описывающий объект
     * @return 
     */
    abstract public double getRadius();
    
}
