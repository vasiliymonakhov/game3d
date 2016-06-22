package org.freeware.monakhov.game3d.objects;

import org.freeware.monakhov.game3d.objects.movable.Hero;
import org.freeware.monakhov.game3d.SpecialMath;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Room;
import org.freeware.monakhov.game3d.map.Sprite;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.misc.GridFence;
import org.freeware.monakhov.game3d.objects.misc.PrisonWall;
import org.freeware.monakhov.game3d.objects.nonmovable.Barrel;
import org.freeware.monakhov.game3d.objects.nonmovable.Fire;
import org.freeware.monakhov.game3d.objects.nonmovable.Key;
import org.freeware.monakhov.game3d.objects.nonmovable.Lamp;
import org.freeware.monakhov.game3d.objects.nonmovable.Milton;
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

    public void turnSpriteToHero(Hero hero) {
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
            case "barrel" :
                return new Barrel(world, new Point(attr));
            case "fire" :
                return new Fire(world, new Point(attr));
            case "milton" :
                return new Milton(world, new Point(attr));
            case "tree" :
                return new Tree(world, new Point(attr));
            case "lamp" :
                return new Lamp(world, new Point(attr));
            case "key" :
                return new Key(world, new Point(attr));
            case "grid_fence" :
                return new GridFence(world, new Point(Double.parseDouble(attr.getValue("start_x")), Double.parseDouble(attr.getValue("start_y"))),
                    new Point(Double.parseDouble(attr.getValue("end_x")), Double.parseDouble(attr.getValue("end_y"))));                
            case "prison_wall" :
                return new PrisonWall(world, new Point(Double.parseDouble(attr.getValue("start_x")), Double.parseDouble(attr.getValue("start_y"))),
                    new Point(Double.parseDouble(attr.getValue("end_x")), Double.parseDouble(attr.getValue("end_y"))));                                
        }
        return null;        
    }
    
}
