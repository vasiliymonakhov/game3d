package org.freeware.monakhov.game3d.objects;

import org.freeware.monakhov.game3d.objects.movable.ViewPoint;
import org.freeware.monakhov.game3d.SpecialMath;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Room;
import org.freeware.monakhov.game3d.map.Sprite;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.nonmovable.GreenBarrel;
import org.freeware.monakhov.game3d.objects.nonmovable.Tree;
import org.xml.sax.Attributes;

/**
 * Объект мира
 *
 * @author Vasily Monakhov
 */
abstract public class WorldObject {

    /**
     * Позиция на карте
     */
    protected final Point position;
    /**
     * Предыдущая позиция на карте
     */
    protected final Point oldPosition;
    /**
     * Левая точка
     */
    private final Point left = new Point();
    /**
     * Правая точка
     */
    private final Point right = new Point();
    /**
     * Левая передняя точка
     */
    private final Point frontLeft = new Point();
    /**
     * Правая передняя точка
     */
    private final Point frontRight = new Point();
    /**
     * Левая задняя точка
     */
    private final Point rearLeft = new Point();
    /**
     * Правая задняя точка
     */
    private final Point rearRight = new Point();

    /**
     * мир
     */
    protected final World world;
    /**
     * Азимут
     */
    protected double azimuth;
    /**
     * Комната, в которой находится объект
     */
    protected Room room;

    /**
     * Создаёт объект
     * @param world мир
     * @param position позийия объекта
     */
    public WorldObject(World world, Point position) {
        if (position == null) {
            throw new IllegalArgumentException("Position may not be null");
        }
        this.position = position;
        this.world = world;
        oldPosition = new Point();
        oldPosition.moveTo(position.getX(), position.getY());
        updateRoom();
    }

    /**
     * Обновляет сведения о комнате
     */
    public final void updateRoom() {
        for (Room r : world.getAllRooms()) {
            if (r.insideThisRoom(position)) {
                room = r;
                break;
            }
        }
    }

    /**
     * Поворачивает крайние точки объекта к главному герою
     * @param hero главный герой
     */
    public void turnSpriteToViewPoint(ViewPoint hero) {
        int sw2 = getSprite().getWidth() / 2;
        double cos = sw2 * Math.cos(-hero.getAzimuth());
        double sin = sw2 * Math.sin(-hero.getAzimuth());
        left.moveTo(position.getX() - cos, position.getY() - sin);
        right.moveTo(position.getX() + cos, position.getY() + sin);
        frontLeft.moveTo(position.getX() - cos + sin, position.getY() - cos - sin);
        frontRight.moveTo(position.getX() + cos + sin, position.getY() - cos + sin);
        rearLeft.moveTo(position.getX() - cos - sin, position.getY() + cos - sin);
        rearRight.moveTo(position.getX() + cos - sin, position.getY() + cos + sin);
    }

    /**
     * Возвращает азимут
     * @return the azimuth
     */
    public double getAzimuth() {
        return azimuth;
    }

    /**
     * Устанавливает азимут
     * @param azimuth the azimuth to set
     */
    public void setAzimuth(double azimuth) {
        this.azimuth = (azimuth + 2 * Math.PI) % (2 * Math.PI);
    }

    /**
     * Возвращает комнату, в которой находится объект
     * @return the room
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Устанавливает комнату, в которой находится объект
     * @param room the room to set
     */
    public void setRoom(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("Room may not be null");
        }
        this.room = room;
    }

    /**
     * Возвращает позицию объекта
     * @return the position
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Возвращает спрайт, которым нужно отрисовать объект
     * @return  спрайт
     */
    abstract public Sprite getSprite();

    /**
     * Вовзращает расстояние от объекта до заданной точки
     * @param p точка
     * @return расстояние
     */
    public double distanceTo(Point p) {
        return SpecialMath.lineLength(p, position);
    }

    /**
     * Можно ли пройти сквозь объект
     * @return
     */
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
     * Вовзращает радиус окружности, на каоторую распространяется действие объекта
     * @return
     */
    abstract public double getInteractRadius();

    /**
     * Вовзращает левую точку
     * @return the left
     */
    public Point getLeft() {
        return left;
    }

    /**
     * Возвращает правую точку
     * @return the right
     */
    public Point getRight() {
        return right;
    }

    /**
     * Создаёт объекты из XML
     * @param world мир
     * @param attr запись файла
     * @return созданный объект
     */
    public static WorldObject createFromXML(World world, Attributes attr) {
        String clasz = attr.getValue("class");
        switch (clasz) {
            case "green_barrel" :
                return new GreenBarrel(world, new Point(attr));
            case "tree" :
                return new Tree(world, new Point(attr));
        }
        return null;
    }

    /**
     * Действие при взаимодействии с другим объектом мира
     * @param wo другой объект
     */
    public abstract void onInteractWith(WorldObject wo);

    /**
     * Сделать что-нибудь
     * @param frameNanoTime текущее время
     */
    public abstract void doSomething(long frameNanoTime);

    /**
     * @return the frontLeft
     */
    public Point getFrontLeft() {
        return frontLeft;
    }

    /**
     * @return the frontRight
     */
    public Point getFrontRight() {
        return frontRight;
    }

    /**
     * @return the rearLeft
     */
    public Point getRearLeft() {
        return rearLeft;
    }

    /**
     * @return the rearRight
     */
    public Point getRearRight() {
        return rearRight;
    }

}
