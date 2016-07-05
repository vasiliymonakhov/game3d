package org.freeware.monakhov.game3d.map;

import org.freeware.monakhov.game3d.map.visiblelines.VisibleLine;
import java.util.LinkedHashSet;
import java.util.Set;
import org.freeware.monakhov.game3d.SpecialMath;
import org.freeware.monakhov.game3d.objects.WorldObject;

/**
 * Линия комнаты на карте
 *
 * @author Vasily Monakhov
 */
public class Line {

    /**
     * Начальная точка линии, должна находиться слева от линии взгляда
     * наблюдателя
     */
    protected final Point start;

    /**
     * Конечная точка линии, должна находиться слева от линии взгляда
     * наблюдателя
     */
    protected final Point end;

    /**
     * Эта линия может быть порталом в другую комнату
     */
    private Set<Room> portalToRooms;

    /**
     * ссылка на мир
     */
    protected final World world;

    /**
     * Создаёт линию
     *
     * @param start начальная точка
     * @param end конечная точка
     * @param world
     */
    public Line(Point start, Point end, World world) {
        this.start = start;
        this.end = end;
        this.world = world;
    }

    /**
     * Возвращает начало линии
     *
     * @return начало линии
     */
    public Point getStart() {
        return start;
    }

    /**
     * Возвращает конец линии
     *
     * @return конец линии
     */
    public Point getEnd() {
        return end;
    }

    /**
     * Устанавливает комнату, превращая линию в портал
     *
     * @param portalTo в какую комнату ведёт портал
     */
    public void setPortal(Room portalTo) {
        if (portalToRooms == null) {
            portalToRooms = new LinkedHashSet<>();
        }
        portalToRooms.add(portalTo);
    }

    /**
     * Возвращает комнату, в которую ведёт этот портал
     *
     * @return комната, в которую ведёт этот портал
     */
    public Set<Room> getRoomsFromPortal() {
        return portalToRooms;
    }

    /**
     * Сообщает, что эта линия является порталом между комнатами
     *
     * @return true если линия является порталом
     */
    public boolean isPortal() {
        return portalToRooms != null;
    }

    /**
     * Сообщает, видима ли эта линия
     *
     * @return true - линия видима
     */
    public boolean isVisible() {
        return false;
    }

    /**
     * Флажок, сообщающий что линия была увидена хотя бы раз
     */
    protected boolean everSeen = false;

    /**
     * Сообщает, что линия была увидена хотя бы раз
     *
     * @return true если линия была увидена хотя бы раз
     */
    public boolean isEverSeen() {
        return everSeen;
    }

    /**
     * Сообщает, что линию можно пересекать
     *
     * @return можно ли пересекать через линию
     */
    public boolean isCrossable() {
        return true;
    }

    /**
     * Трассируем линию лучом
     * @param mapLines массив линий, попадающих в столбец на экране
     * @param index индекс луча
     * @param viewPoint точка обзора
     * @param rayPoint точка луча
     * @param intersectPoint точека пересечения трассирующего луча и какой-либо стены
     * @param visibleRooms множество видимых комнат
     * @param checkedRooms множество уже проверенных комнат
     * @return true если луч нашел видимую стену
     */
    public boolean traceLine(VisibleLine[] mapLines, int index, Point viewPoint, Point rayPoint, Point intersectPoint, Set<Room> visibleRooms, Set<Room> checkedRooms) {
        Point lcp = new Point();
        if (SpecialMath.lineIntersection(start, end, rayPoint, viewPoint, lcp) && lcp.between(start, end) && lcp.between(viewPoint, rayPoint)) {
            everSeen = true;
            if (isPortal()) {
                for (Room r : getRoomsFromPortal()) {
                    if (checkedRooms.contains(r)) {
                        continue;
                    }
                    if (r.traceRoom(mapLines, index, viewPoint, rayPoint, intersectPoint, visibleRooms, checkedRooms)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Что нужно сделать при взаимодействии с объектом мира
     *
     * @param wo
     */
    public void onInteractWith(WorldObject wo) {
    }

    /**
     * Сделать что-нибудь
     *
     * @param frameNanoTime текущее время
     */
    public void doSomething(long frameNanoTime) {
    }

}
