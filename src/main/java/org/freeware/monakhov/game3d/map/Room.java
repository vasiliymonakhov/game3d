package org.freeware.monakhov.game3d.map;

import org.freeware.monakhov.game3d.map.visiblelines.VisibleLine;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.freeware.monakhov.game3d.SpecialMath;

/**
 * Комната на карте. Комната должна быть выпуклым многоугольником.
 *
 * @author Vasily Monakhov
 */
public class Room {

    /**
     * Возвращает список всех линий комнаты
     *
     * @return список всех линий комнаты
     */
    public Collection<Line> getAllLines() {
        return lines.values();
    }

    /**
     * Карта - список всех линий комнаты
     */
    private final Map<String, Line> lines = new LinkedHashMap<>();

    /**
     * Добавляет к комнате новую линию
     *
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
     *
     * @param id идентификатор линии
     * @return линия
     */
    public Line getLine(String id) {
        return lines.get(id);
    }

    /**
     * Трассируем комнату лучом
     * @param mapLines массив линий, попадающих в столбец на экране
     * @param index индекс луча
     * @param viewPoint точка обзора
     * @param rayPoint точка луча
     * @param intersectPoint точека пересечения трассирующего луча и какой-либо стены
     * @param visibleRooms множество видимых комнат
     * @param checkedRooms множество уже проверенных комнат
     * @return true если луч нашел видимую стену
     */
    public boolean traceRoom(VisibleLine[] mapLines, int index, Point viewPoint, Point rayPoint, Point intersectPoint, Set<Room> visibleRooms, Set<Room> checkedRooms) {
        checkedRooms.add(this);
        // сначала надо проверить все видимые линии. Т.к. комната - выпуклый многоугольник,
        // то в ней одна стена другую закрывать не может
        for (Line l : lines.values()) {
            if (l.isVisible()) {
                if (l.traceLine(mapLines, index, viewPoint, rayPoint, intersectPoint, visibleRooms, checkedRooms)) {
                    visibleRooms.add(this);
                    return true;
                }
            }
        }
        // потом проверим служебные линии - порталы например. Вот там уже мы увидим то, что
        // просматривается через портал
        for (Line l : lines.values()) {
            if (!l.isVisible() || l.isPortal()) {
                if (l.traceLine(mapLines, index, viewPoint, rayPoint, intersectPoint, visibleRooms, checkedRooms)) {
                    return true;
                }
            }
        }
        return false;
    }

    final static double EPSILON = 0.1d;

    /**
     * Проверяет, находится ли точка внутри комнаты
     *
     * @param p точка
     * @return true если точка находится внутри комнаты
     */
    public boolean insideThisRoom(Point p) {
        double sp = 0;
        for (Line l : lines.values()) {
            sp += SpecialMath.triangleSquare(p, l.getStart(), l.getEnd());
        }
        double sr = 0;
        Iterator<Line> it = lines.values().iterator();
        Line stl = it.next();
        Point ps = stl.start;
        for (Line nl : lines.values()) {
            sr += SpecialMath.triangleSquare(ps, nl.start, nl.end);
        }
        return Math.abs(sr - sp) < EPSILON;
    }

}
