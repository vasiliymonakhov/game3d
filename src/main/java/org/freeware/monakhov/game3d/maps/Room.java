package org.freeware.monakhov.game3d.maps;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.freeware.monakhov.game3d.Hero;
import org.freeware.monakhov.game3d.SpecialMath;

/**
 * A room in a map
 * @author Vasily Monakhov 
 */
public class Room {

    /**
     * Points in a map
     */
    private final Map<String, Point> points = new LinkedHashMap<>();    
    
    void addPoint(String id, Point p) {
        if (points.containsKey(id)) {
            throw new IllegalArgumentException("Point " + id + " already exists"); 
        }
        points.put(id, p);
    }
    
    Point getPoint(String id) {
        return points.get(id);
    }
    
    public Collection<Point>  getAllPoints() {
        return points.values();
    }
    
    public Collection<Line>  getAllLines() {
        return lines.values();
    }    
    
    /**
     * Walls of this room
     */
    private final Map<String, Line> lines = new LinkedHashMap<>();
    
    void addLine(String id, Line w) {
        if (lines.containsKey(id)) {
            throw new IllegalArgumentException("Wall " + id + " already exists"); 
        }
        lines.put(id, w);
    }    
    
    Line getLine(String id) {
        return lines.get(id);
    }

    void clearRoomVisibilityAlreadyChecked() {
        roomVisibilityAlreadyChecked = false;
    }
    
    private boolean roomVisibilityAlreadyChecked;
    
    public boolean checkVisibility(Line[] mapLines, Point viewPoint, Point[] rayPoints, Point[] intersectPoints) {
        if (isRoomVisibilityAlreadyChecked()) return false;
        roomVisibilityAlreadyChecked = true;
        boolean flag = false;
        // сначала надо проверить все видимые линии. Т.к. комната - выпуклый многоугольник, 
        // то в ней одна стена другую закрывать не может
        for (Line l : lines.values()) {
            if (l.isVisible()) flag |=l.checkVisibility(mapLines, viewPoint, rayPoints, intersectPoints);
        }
        // потом проверим служебные линии - порталы например. Вот там уже мы увидим то, что 
        // просматривается через портал
        for (Line l : lines.values()) {
            if (!l.isVisible()) {
                if (l.checkVisibility(mapLines, viewPoint, rayPoints, intersectPoints)) {
                    Room r = l.getRoomFromPortal();
                    if (r != null) {
                        flag |= r.checkVisibility(mapLines, viewPoint, rayPoints, intersectPoints);
                    }
                }
            }
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
        Iterator<Point> it = points.values().iterator();
        Point p0 = it.next();
        Point p1 = it.next();
        while (it.hasNext()) {
            Point p2 = it.next();
            sr += SpecialMath.triangleSquare(p0, p1, p2);
            p1 = p2;
        }
        return Math.abs(sr - sp) < EPSILON;
    }
    
    
}
