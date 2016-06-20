package org.freeware.monakhov.game3d.objects;

import org.freeware.monakhov.game3d.objects.movable.Hero;
import java.awt.Graphics2D;
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
    
    protected double azimuth;
    
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
        oldPosition.moveTo(position.getX(), position.getY());
    }

    /**
     * @return the azimuth
     */
    public double getAzimuth() {
        return azimuth;
    }

    /**
     * @param azimuth the azimuth to set
     */
    public void setAzimuth(double azimuth) {
        this.azimuth = azimuth % (2 * Math.PI);
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
