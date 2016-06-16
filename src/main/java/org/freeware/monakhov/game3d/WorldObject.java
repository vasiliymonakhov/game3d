package org.freeware.monakhov.game3d;

import java.awt.Graphics2D;
import org.freeware.monakhov.game3d.maps.Point;
import org.freeware.monakhov.game3d.maps.Room;
import org.freeware.monakhov.game3d.maps.Sprite;

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
    
    double wh = 10;
    
    public void render(Graphics2D g, int screenHeight, Hero hero, Point[] transformedRayPoints, Point[] intersectPoints) {
        Point s = new Point();
        Point e = new Point();
        double deltaX =  5 * Math.cos(-hero.getAsimuth());
        double deltaY = 5 * Math.sin(-hero.getAsimuth());

        s.moveTo(position.getX() - deltaX, position.getY() - deltaY);        
        e.moveTo(position.getX() + deltaX, position.getY() + deltaY);
        Point p = new Point();
        for (int i = 0; i < transformedRayPoints.length; i++) {
            if (SpecialMath.lineIntersection(s, e, hero.getPosition(), intersectPoints[i], p)) {
                if (p.between(s, e) && p.between(hero.getPosition(), intersectPoints[i])) {
                    double dist = SpecialMath.lineLength(hero.getPosition(), p);
                    double k = SpecialMath.lineLength(hero.getPosition(), transformedRayPoints[i]);
                    double h = wh * k / dist;
                    int ch = (int) Math.round((screenHeight - h) / 2);
                    long xOffset = Math.round(SpecialMath.lineLength(s, p) * 25);
                    Sprite sprite = getSprite();
                    int spriteOffset = (int)(xOffset % sprite.getWidth());
                    g.drawImage(sprite.getSubImage(spriteOffset, 0, 1, sprite.getHeight()), i, ch, 1, (int) Math.round(h), null);                    
                }
            }
        }
    }
    
    public double distanceTo(Point p) {
        return SpecialMath.lineLength(p, position);
    }

}
