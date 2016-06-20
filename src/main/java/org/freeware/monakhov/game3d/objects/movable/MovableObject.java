package org.freeware.monakhov.game3d.objects.movable;

import org.freeware.monakhov.game3d.SpecialMath;
import org.freeware.monakhov.game3d.map.Line;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Room;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;

/**
 * Объект, который может перемещаться
 * @author Vasily Monakhov 
 */
public abstract class MovableObject extends WorldObject {

    private final World world;    
    
    public MovableObject(Point position, World world) {
        super(position);
        this.world = world;
    }

    boolean notTouchAnyObject(Point newPosition) {
        // проверяем, не столкнулись ли мы с каким-то объектом
        for (WorldObject o : world.getAllObjects()) {
            if (!o.isCrossable()) {
                // Через объект нельзя пройти
                double distance = SpecialMath.lineLength(o.getPosition(), newPosition);
                double radiuses = o.getRadius() + getRadius();
                if (distance < radiuses) {
                    // расстояние между объектами слишком мало, пройти нельзя
                    return false;
                }
            }
        }
        return true;
    }

    boolean notTouchWall(Point newPosition) {
        // проверить, не уткнулись ли мы с стенку
        Point p = new Point();
        for (Line l : room.getAllLines()) {
            // проверять только непроходимые линии
            if (!l.isCrossable()) {
                if (SpecialMath.lineIntersection(l.getStart(), l.getEnd(), newPosition, oldPosition, p)
                        && p.between(l.getStart(), l.getEnd()) && p.between(newPosition, oldPosition)) {
                    return false;
                }
            }
        }
        return true;
    }

    void checkMoveToOtherRoom(Point newPosition) {
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
                        room = nr;
                        break;
                    }
                }
            }
        }
    }

    boolean canMoveHere(Point newPosition) {
        return notTouchAnyObject(newPosition) && notTouchWall(newPosition);
    }

    public boolean moveBy(double df, double ds) {
        double deltaX = df * Math.sin(azimuth) + ds * Math.cos(-azimuth);
        double deltaY = df * Math.cos(azimuth) + ds * Math.sin(-azimuth);
        // определяем новые координаты
        Point newPosition = new Point(position.getX(), position.getY());
        newPosition.moveBy(deltaX, deltaY);
        if (!canMoveHere(newPosition)) {
            return false;
        }
        checkMoveToOtherRoom(newPosition);
        oldPosition.moveTo(position.getX(), position.getY());
        position.moveTo(newPosition.getX(), newPosition.getY());
        return true;
    }

}
