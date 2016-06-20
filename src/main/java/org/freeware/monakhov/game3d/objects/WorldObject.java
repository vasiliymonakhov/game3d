package org.freeware.monakhov.game3d.objects;

import org.freeware.monakhov.game3d.objects.movable.Hero;
import org.freeware.monakhov.game3d.SpecialMath;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Room;
import org.freeware.monakhov.game3d.map.Sprite;

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

    protected double azimuth;

    protected Room room;

    /**
     * @param position the position to set
     */
    public WorldObject(Point position) {
        if (position == null) {
            throw new IllegalArgumentException("Position may not be null");
        }
        this.position = position;
        oldPosition = new Point();
        oldPosition.moveTo(position.getX(), position.getY());
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

}
