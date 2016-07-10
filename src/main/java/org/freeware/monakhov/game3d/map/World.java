package org.freeware.monakhov.game3d.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.freeware.monakhov.game3d.objects.WorldObject;
import org.freeware.monakhov.game3d.objects.movable.Hero;

/**
 * Мир
 * @author Vasily Monakhov
 */
public class World {

    /**
     * Пол
     */
    private String floor;
    /**
     * Потолок
     */
    private String ceiling;
    /**
     * Небо
     */
    private String sky;
    /**
     * Главнй герой
     */
    private Hero hero;

    /**
     * Помещаем главного героя в наш мир
     * @param hero
     */
    public void setHero (Hero hero) {
        this.hero = hero;
    }

    /**
     * Возвращает главного героя
     * @return главный герой
     */
    public Hero getHero() {
        return hero;
    }

    /**
     * Карта с чтоками мира
     */
    private final Map<String, Point> points = new LinkedHashMap<>();

    /**
     * Добавляет точка
     * @param id идентификатор точки
     * @param p точка
     */
    public void addPoint(String id, Point p) {
         if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Point id must be not null or empty");
        }
        if (p == null) {
            throw new IllegalArgumentException("Point must be not null");
        }
        if (points.containsKey(id)) {
            throw new IllegalArgumentException("Point " + id + " already exists");
        }
        points.put(id, p);
    }

    /**
     * Возвращает точку по идентификатору
     * @param id идентификатор
     * @return точка
     */
    public Point getPoint(String id) {
        return points.get(id);
    }

    /**
     * Вовзращает список всех точек
     * @return список всех точек
     */
    public Collection<Point>  getAllPoints() {
        return points.values();
    }

    /**
     * Линии карты
     */
    private final Map<String, Line> lines = new LinkedHashMap<>();

    /**
     * Добавляет линию на карту
     * @param id идентификатор линии
     * @param w линия
     */
    public void addLine(String id, Line w) {
         if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Line id must be not null or empty");
        }
        if (w == null) {
            throw new IllegalArgumentException("Line must be not null");
        }
        if (lines.containsKey(id)) {
            throw new IllegalArgumentException("Line " + id + " already exists");
        }
        lines.put(id, w);
    }

    /**
     * Возвращает линию по идентификатору
     * @param id идентификатор линии
     * @return линия
     */
    public Line getLine(String id) {
        return lines.get(id);
    }

    /**
     * Возвращает список всех линий
     * @return список всех линий
     */
    public Collection<Line> getAllLines() {
        return lines.values();
    }

    /**
     * Комнаты
     */
    private final Map<String, Room> rooms = new HashMap<>();

    /**
     * Добавляет комнату
     * @param id идентификатор комнаты
     * @param r комната
     */
    public void addRoom(String id, Room r) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Room id must be not null or empty");
        }
        if (r == null) {
            throw new IllegalArgumentException("Room must be not null");
        }
        if (rooms.containsKey(id)) {
            throw new IllegalArgumentException("Room " + id + " already exists");
        }
        rooms.put(id, r);
    }

    /**
     * Возвращает комнату по идентификатору
     * @param id идентификатор комнаты
     * @return комната
     */
    public Room getRoom(String id) {
        return rooms.get(id);
    }

    /**
     * Возвращает список всех комнат
     * @return список всех комнат
     */
    public Collection<Room> getAllRooms() {
        return rooms.values();
    }

    /**
     * Объекты мира
     */
    private final Map<String, WorldObject> identifiedObjects = new HashMap<>();

    private final Set<WorldObject> objects = new HashSet<>();

    /**
     * Вовзращает список объектов мира
     * @return
     */
    public Collection<WorldObject> getAllObjects() {
        return objects;
    }

    /**
     * Добавляет объект в мир
     * @param id идентификатор объекта
     * @param o объект
     */
    public void addObject(String id, WorldObject o) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Object id must be not null or empty");
        }
        if (o == null) {
            throw new IllegalArgumentException("Object must be not null");
        }
        if (identifiedObjects.containsKey(id)) {
            throw new IllegalArgumentException("Object " + id + " already exists");
        }
        identifiedObjects.put(id, o);
        objects.add(o);
    }

    /**
     * Возвращает объект по идентификатору
     * @param id идентификатор объекта
     * @return объект
     */
    public WorldObject getObject(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Object id must be not null or empty");
        }
        if (!identifiedObjects.containsKey(id)) {
            throw new IllegalArgumentException("Object " + id + " not exists");
        }
        return identifiedObjects.get(id);
    }

    /**
     * @return the floor
     */
    public String getFloor() {
        return floor;
    }

    /**
     * @param floor the floorCeiling to set
     */
    public void setFloor(String floor) {
        this.floor = floor;
    }

    /**
     * @return the ceiling
     */
    public String getCeiling() {
        return ceiling;
    }

    /**
     * @param ceiling the ceiling to set
     */
    public void setCeiling(String ceiling) {
        this.ceiling = ceiling;
    }

    /**
     * @return the sky
     */
    public String getSky() {
        return sky;
    }

    /**
     * @param sky the sky to set
     */
    public void setSky(String sky) {
        this.sky = sky;
    }

    private final List<WorldObject> objectsToAdd = new ArrayList<>();

    public void addNewObject(WorldObject e) {
        objectsToAdd.add(e);
    }

    private final List<WorldObject> objectsToDelete = new ArrayList<>();

    public void deleteObject(WorldObject e) {
        objectsToDelete.add(e);
    }

    public void updateObjects() {
        objects.removeAll(objectsToDelete);
        objectsToDelete.clear();
        objects.addAll(objectsToAdd);
        objectsToAdd.clear();
    }

}
