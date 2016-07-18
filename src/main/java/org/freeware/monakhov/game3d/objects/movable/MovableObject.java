package org.freeware.monakhov.game3d.objects.movable;

import org.freeware.monakhov.game3d.SpecialMath;
import org.freeware.monakhov.game3d.map.Line;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;

/**
 * Объект, который может перемещаться
 *
 * @author Vasily Monakhov
 */
public abstract class MovableObject extends WorldObject {

    protected final WorldObject creator;
    /**
     * Создаёт объект
     * @param world мир
     * @param position положение
     * @param creator кто создал этот объект
     */
    public MovableObject(World world, Point position, WorldObject creator) {
        super(world, position);
        this.creator = creator;
    }

    /**
     * Проверяет, не столкнулись ли с каким-то объектом
     * @param newPosition новое положение
     * @return true если столкнулись
     */
    public boolean touchAnyObject(Point newPosition) {
        if (this != world.getHero() && creator != world.getHero()) {
            // если не герой, тол проверить на столкновение с героем
            double distance = SpecialMath.lineLength(world.getHero().getPosition(), newPosition);
            double radiuses = world.getHero().getRadius() + getRadius();
            if (distance < radiuses) {
                // расстояние между объектами слишком мало, пройти нельзя
                return true;
            }
        }
        // проверяем, не столкнулись ли мы с каким-то объектом
        for (WorldObject o : world.getAllObjects()) {
            if (o == this) continue;
            if (o == creator) continue;
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
        for (Line l : world.getAllLines()) {
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
        for (Line l : world.getAllLines()) {
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
     * Переместиться в новое место с проверками на допустимость
     * @param df смещение по прямой
     * @param ds смещение в стороны
     * @return true если манёвр удался
     */
    public boolean moveByWithCheck(double df, double ds) {
        Point newPosition = calcNewPosition(df, ds);
        double l = SpecialMath.lineLength(position, newPosition) + 0.5;
        Point tmpPosition = new Point();
        for (long i = 1; i < l; i++) {
            tmpPosition.moveTo(position.getX() + (newPosition.getX() - position.getX()) * i / l, position.getY() + (newPosition.getY() - position.getY()) * i / l);
            if (touchAnyObject(tmpPosition)) {
                return false;
            }
            if (touchWall(tmpPosition) != null) {
                return false;
            }
        }
        if (crossWall(newPosition) != null) {
            return false;
        }
        oldPosition.moveTo(position.getX(), position.getY());
        position.moveTo(newPosition.getX(), newPosition.getY());
        updateRoom();
        return true;
    }

    /**
     * Вычисление новой позиции
     * @param df смещение по прямой
     * @param ds смещение в стороны
     * @return новая позиция
     */
    public Point calcNewPosition(double df, double ds) {
        Point newPosition = new Point(position.getX(), position.getY());
        // определяем новые координаты
        double deltaX = df * Math.sin(azimuth) + ds * Math.cos(-azimuth);
        double deltaY = df * Math.cos(azimuth) + ds * Math.sin(-azimuth);
        newPosition.moveBy(deltaX, deltaY);
        return newPosition;
    }

    public abstract double getAimError();

}
