package org.freeware.monakhov.game3d.objects.movable;

import org.freeware.monakhov.game3d.SpecialMath;
import org.freeware.monakhov.game3d.map.Line;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Room;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;

/**
 * Объект, который может перемещаться
 *
 * @author Vasily Monakhov
 */
public abstract class MovableObject extends WorldObject {

    public MovableObject(World world, Point position) {
        super(world, position);
    }

    boolean touchAnyObject(Point newPosition) {
        // проверяем, не столкнулись ли мы с каким-то объектом
        for (WorldObject o : world.getAllObjects()) {
            if (!o.isCrossable()) {
                // Через объект нельзя пройти
                double distance = SpecialMath.lineLength(o.getPosition(), newPosition);
                double radiuses = o.getRadius() + getRadius();
                if (distance < radiuses) {
                    // расстояние между объектами слишком мало, пройти нельзя
                    return true;
                }
            }
        }
        return false;
    }

    Line touchWall(Point newPosition) {
        // проверить, не уткнулись ли мы с стенку
        Point p1 = new Point();
        Point p2 = new Point();
        for (Line l : room.getAllLines()) {
            // проверять только непроходимые линии
            if (!l.isCrossable()) {
                int n = SpecialMath.lineAndCircleIntersection(l.getStart(), l.getEnd(), newPosition, getRadius(), p1, p2);
                if (n == 1) {
                    if (p1.between(l.getStart(), l.getEnd())) {
                        return l;
                    }
                } else if (n == 2) {
                    if (p1.between(l.getStart(), l.getEnd()) || p2.between(l.getStart(), l.getEnd())) {
                        return l;
                    }
                }
            }
        }
        return null;
    }

    Line crossWall(Point newPosition) {
        // проверить, не уткнулись ли мы с стенку
        Point p = new Point();
        for (Line l : room.getAllLines()) {
            // проверять только непроходимые линии
            if (!l.isCrossable()) {
                if (SpecialMath.lineIntersection(l.getStart(), l.getEnd(), newPosition, position, p)) {
                    if (p.between(l.getStart(), l.getEnd()) && p.between(newPosition, position)) {
                        return l;
                    }
                }
            }
        }
        return null;
    }

    Room checkMoveToOtherRoom(Point newPosition) {
        // проверяем, не пересекли ли мы какую-то линию
        Point p = new Point();
        for (Line l : room.getAllLines()) {
            if (l.isCrossable()
                    && SpecialMath.lineIntersection(l.getStart(), l.getEnd(), newPosition, position, p)
                    && p.between(l.getStart(), l.getEnd()) && p.between(newPosition, position)) {
                // пересекли линию и её можно пересекать
                Room nr = l.getRoomFromPortal();
                if (nr != null) {
                    // возможно, перешли в другую комнату?
                    if (nr.insideThisRoom(newPosition)) {
                       return nr;
                    }
                }
            }
        }
        return null;
    }

    public boolean moveBy(double df, double ds) {
        Point newPosition = new Point(position.getX(), position.getY());
        // определяем новые координаты
        double deltaX = df * Math.sin(azimuth) + ds * Math.cos(-azimuth);
        double deltaY = df * Math.cos(azimuth) + ds * Math.sin(-azimuth);
        newPosition.moveBy(deltaX, deltaY);
        if (touchAnyObject(newPosition)) return false;
        if (touchWall(newPosition) != null || crossWall(newPosition) != null) return false;
        Room nr = checkMoveToOtherRoom(newPosition);
        if (nr != null) {
            room = nr;
        } else {
            if (!room.insideThisRoom(newPosition))  return false;
        }
        oldPosition.moveTo(position.getX(), position.getY());
        position.moveTo(newPosition.getX(), newPosition.getY());
        return true;
    }

}
