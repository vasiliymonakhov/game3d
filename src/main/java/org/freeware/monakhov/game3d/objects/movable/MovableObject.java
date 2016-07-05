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

    /**
     * Создаёт объект
     * @param world мир
     * @param position положение
     */
    public MovableObject(World world, Point position) {
        super(world, position);
    }

    /**
     * Проверяет, не столкнулись ли с каким-то объектом
     * @param newPosition новое положение
     * @return true если столкнулись
     */
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

    /**
     * Проверяет, не уткнулись ли в стенку
     * @param newPosition новое положение
     * @return true если уткнулись
     */
    Line touchWall(Point newPosition) {
        // проверить, не уткнулись ли мы с стенку
        for (Line l : room.getAllLines()) {
            // проверять только непроходимые линии
            if (!l.isCrossable()) {
                // площади треугольников, которые образуют точки старой позиции и новой позиции и линия
                double ps = SpecialMath.triangleSquare(position, l.getStart(), l.getEnd());
                double ns = SpecialMath.triangleSquare(newPosition, l.getStart(), l.getEnd());
                // если площадь треугольника со старой позицией больше, чем с новой, то мы приближаемся к линии
                if (ps > ns && SpecialMath.lineAndCircleIntersects(l.getStart(), l.getEnd(), newPosition, getRadius())) return l;
            }
        }
        return null;
    }

    /**
     * Проверяет, не пересекли ли мы стенку
     * @param newPosition новое положение
     * @return стенка или null если не пересекли
     */
    Line crossWall(Point newPosition) {
        // проверить, не пересекли ли мы с стенку
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

    /**
     * Проверяем, не попали ли мы в другую комнату
     * @param newPosition новое положение
     * @return новая комната или null
     */
    Room checkMoveToOtherRoom(Point newPosition) {
        // проверяем, не пересекли ли мы какую-то линию
        Point p = new Point();
        for (Line l : room.getAllLines()) {
            if (l.isCrossable()
                    && SpecialMath.lineIntersection(l.getStart(), l.getEnd(), newPosition, position, p)
                    && p.between(l.getStart(), l.getEnd()) && p.between(newPosition, position)) {
                // пересекли линию и её можно пересекать
                for (Room nr : l.getRoomsFromPortal()) {
                    if (nr != room) {
                        // возможно, перешли в другую комнату?
                        if (nr.insideThisRoom(newPosition)) {
                            return nr;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Переместиться в новое место
     * @param df смещение по прямой
     * @param ds смещение в стороны
     * @return true если манёвр удался
     */
    public boolean moveBy(double df, double ds) {
        Point newPosition = new Point(position.getX(), position.getY());
        // определяем новые координаты
        double deltaX = df * Math.sin(azimuth) + ds * Math.cos(-azimuth);
        double deltaY = df * Math.cos(azimuth) + ds * Math.sin(-azimuth);
        newPosition.moveBy(deltaX, deltaY);
        if (touchAnyObject(newPosition)) {
            return false;
        }
        if (touchWall(newPosition) != null || crossWall(newPosition) != null) {
            return false;
        }
        Room nr = checkMoveToOtherRoom(newPosition);
        if (nr != null) {
            room = nr;
        } else {
            if (!room.insideThisRoom(newPosition)) {
                return false;
            }
        }
        oldPosition.moveTo(position.getX(), position.getY());
        position.moveTo(newPosition.getX(), newPosition.getY());
        return true;
    }

}
