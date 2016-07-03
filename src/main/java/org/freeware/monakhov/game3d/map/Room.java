package org.freeware.monakhov.game3d.map;

import org.freeware.monakhov.game3d.map.visiblelines.VisibleLine;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.freeware.monakhov.game3d.SpecialMath;

/**
 * A room in a map
 *
 * @author Vasily Monakhov
 */
public class Room {

    public Collection<Line> getAllLines() {
        return lines.values();
    }

    /**
     * Walls of this room
     */
    private final Map<String, Line> lines = new LinkedHashMap<>();

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

    public Line getLine(String id) {
        return lines.get(id);
    }

    void clearRoomVisibilityAlreadyChecked() {
        roomVisibilityAlreadyChecked = false;
    }

    boolean roomVisibilityAlreadyChecked;

    public boolean checkVisibility(VisibleLine[] mapLines, Point viewPoint, Point[] rayPoints, Point[] intersectPoints, List<Room> visibleRooms) {
        if (isRoomVisibilityAlreadyChecked()) {
            return false;
        }
        roomVisibilityAlreadyChecked = true;
        boolean flag = false;
        // сначала надо проверить все видимые линии. Т.к. комната - выпуклый многоугольник, 
        // то в ней одна стена другую закрывать не может
        for (Line l : lines.values()) {
            if (l.isVisible()) {
                flag |= l.checkVisibility(mapLines, viewPoint, rayPoints, intersectPoints);
            }
        }
        // потом проверим служебные линии - порталы например. Вот там уже мы увидим то, что 
        // просматривается через портал
        for (Line l : lines.values()) {
            if (!l.isVisible() || l.isPortal()) {
                if (l.checkVisibility(mapLines, viewPoint, rayPoints, intersectPoints)) {
                    for (Room r : l.getRoomsFromPortal()) {
                        if (r != this) {
                            flag |= r.checkVisibility(mapLines, viewPoint, rayPoints, intersectPoints, visibleRooms);
                        }
                    }
                }
            }
        }
        if (flag) {
            visibleRooms.add(this);
        }
        return flag;
    }

    /**
     * @return the roomVisibilityAlreadyChecked
     */
    public boolean isRoomVisibilityAlreadyChecked() {
        return roomVisibilityAlreadyChecked;
    }

    final static double EPSILON = 0.1d;

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
