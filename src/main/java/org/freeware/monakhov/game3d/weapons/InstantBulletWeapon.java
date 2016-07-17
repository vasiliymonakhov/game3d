package org.freeware.monakhov.game3d.weapons;

import org.freeware.monakhov.game3d.SpecialMath;
import org.freeware.monakhov.game3d.map.Line;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Room;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;
import org.freeware.monakhov.game3d.objects.movable.MovableObject;

/**
 *
 * @author Vasily Monakhov
 */
public abstract class InstantBulletWeapon extends Weapon {

    public InstantBulletWeapon(World world, MovableObject owner) {
        super(world, owner);
    }

    /**
     * Максимальная дистанция стрельбы
     * @return
     */
    public abstract double getFireDistance();

    /**
     * Расчёт повреждений в зависимости от расстояния до цели
     * @param distance расстояние до цели
     * @return величина повреждения в % здоровья
     */
    public abstract double getDamage(double distance);

    @Override
    public void makeFire(World world) {
        double az = aim();
        double distance = getFireDistance();
        // самая дальняя точка, куда может долететь пуля
        Point pt = new Point(owner.getPosition().getX() + distance * Math.sin(az),
            owner.getPosition().getY() + distance * Math.cos(az));
        Point pw = findNearestWall(pt, owner.getRoom(), null);
        if (pw != null) {
            // попали в какую-то стенку
            pt = pw;
        }
        // проверить попадание в какой-либо объект между стрелком и точкой попадания в стенку или дальней точкой выстрела
        WorldObject candidateToDie = null; // кандидат на получение пули
        for (WorldObject wo : world.getAllObjects()) {
            if (wo == owner) continue; // в себя не стрелять
            // найдём ближайший объект, в который попала пуля
            if (!wo.isCrossable()) { // объект должен быть непроходимым
                if (SpecialMath.lineAndCircleIntersects(owner.getPosition(), pt, wo.getPosition(), wo.getRadius())) {
                    // попали
                    double nd = SpecialMath.lineLength(owner.getPosition(), wo.getPosition());
                    if (nd < distance) {
                        // имеем претендента на получение пули
                        candidateToDie = wo;
                        distance = nd;
                    }
                }
            }
        }
        if (candidateToDie == null && owner != world.getHero()) {
            // никуда не попали, но владелец оружния враг, а не герой, надо проверить, попал ли он в героя
            if (SpecialMath.lineAndCircleIntersects(owner.getPosition(), pt, world.getHero().getPosition(), world.getHero().getRadius())) {
                double nd = SpecialMath.lineLength(owner.getPosition(), world.getHero().getPosition());
                if (nd < distance) {
                        // пуля попала в героя
                        candidateToDie = world.getHero();
                        distance = nd;
                    }
            }
        }
        if (candidateToDie != null) {
            // в кого-то попали, нанесём ему урон
            candidateToDie.onGetDamage(getDamage(distance));
        }
    }

    /**
     * Найти точку на стене, куда попала пуля
     * @param pt дальняя точка
     * @param inRoom в какой комнате проверять
     * @param inLine линия, через которую пуля влетела
     * @return точка, в которую попала пуля
     */
    private Point findNearestWall(Point pt, Room inRoom, Line inLine) {
        Point p = new Point();
        for (Line l : inRoom.getAllLines()) {
            if (l == inLine) continue;
            // проверить все стены в комнате
            if (SpecialMath.lineIntersection(l.getStart(), l.getEnd(), pt, owner.getPosition(), p)) {
                if (p.between(l.getStart(), l.getEnd()) && p.between(pt, owner.getPosition())) {
                    // пересекли какую-то линию
                    if (l.pointIsVisible(p)) {
                        // нашли точку на стене
                        return p;
                    } else {
                        // это проход в другую комнату
                        for (Room nr : l.getRoomsFromPortal()) {
                            if (nr != inRoom) {
                                // проверим, во что попали в другой комнате
                                return findNearestWall(pt, nr, l);
                            }
                        }
                    }
                }
            }
        }
        // не попали ни во что!
        return null;
    }

}
