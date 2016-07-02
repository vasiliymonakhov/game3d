package org.freeware.monakhov.game3d.objects;

import org.freeware.monakhov.game3d.objects.movable.ViewPoint;
import org.freeware.monakhov.game3d.SpecialMath;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Room;
import org.freeware.monakhov.game3d.map.Sprite;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.nonmovable.GreenBarrel;
import org.freeware.monakhov.game3d.objects.nonmovable.RedLight;
import org.freeware.monakhov.game3d.objects.nonmovable.Tree;
import org.xml.sax.Attributes;

/**
 * Объект мира
 *
 * @author Vasily Monakhov
 */
abstract public class WorldObject {

    protected final Point position;
    protected final Point oldPosition;
    private final Point left = new Point();
    private final Point right = new Point();
    protected final World world;    

    protected double azimuth;

    protected Room room;

    /**
     * @param world
     * @param position the position to set
     */
    public WorldObject(World world, Point position) {
        if (position == null) {
            throw new IllegalArgumentException("Position may not be null");
        }
        this.position = position;
        this.world = world;
        oldPosition = new Point();
        oldPosition.moveTo(position.getX(), position.getY());
        for (Room r : world.getAllRooms()) {
            if (r.insideThisRoom(position)) {
                room = r;
                break;
            }
        }        
    }

    public void turnSpriteToViewPoint(ViewPoint hero) {
        int sw2 = getSprite().getWidth() / 2;
        double deltaX = sw2 * Math.cos(-hero.getAzimuth());
        double deltaY = sw2 * Math.sin(-hero.getAzimuth());
        left.moveTo(position.getX() - deltaX, position.getY() - deltaY);
        right.moveTo(position.getX() + deltaX, position.getY() + deltaY);
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
     *
     * @return
     */
    abstract public double getRadius();
    
    abstract public double getInteractRadius();

    /**
     * @return the left
     */
    public Point getLeft() {
        return left;
    }

    /**
     * @return the right
     */
    public Point getRight() {
        return right;
    }

    public static WorldObject createFromXML(World world, Attributes attr) {
        String clasz = attr.getValue("class");
        switch (clasz) {
            case "green_barrel" :
                return new GreenBarrel(world, new Point(attr));
            case "tree" :
                return new Tree(world, new Point(attr));
            case "red_light" :
                return new RedLight(world, new Point(attr));                
        }
        return null;        
    }
    
    public abstract void onInteractWith(WorldObject wo);
    
    public abstract void doSomething(long frameNanoTime);
    
}
