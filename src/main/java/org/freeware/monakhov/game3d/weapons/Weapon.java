package org.freeware.monakhov.game3d.weapons;

import java.util.List;
import org.freeware.monakhov.game3d.ScreenBuffer;
import org.freeware.monakhov.game3d.SpecialMath;
import org.freeware.monakhov.game3d.map.Line;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Room;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;
import org.freeware.monakhov.game3d.objects.movable.MovableObject;
import org.freeware.monakhov.game3d.objects.nonmovable.Ammo;
import org.freeware.monakhov.game3d.resources.BigImage;

/**
 *
 * @author Vasily Monakhov
 */
public abstract class Weapon {

    protected final World world;
    protected final MovableObject owner;

    public Weapon(World world, MovableObject owner) {
        this.world = world;
        this.owner = owner;
    }

    protected int ammo;

    public boolean isOutOfAmmo() {
        return ammo == 0;
    }

    long timeFromLastShot = getTimeBetweenShots();

    public void fire() {
        if (timeFromLastShot >= getTimeBetweenShots() && ammo > 0) {
            makeFire();
            playShotSound();
            ammo--;
            timeFromLastShot = 0;
        }
    }

    public abstract boolean isLowAmmo();

    /**
     * Прицеливание - возвращает азимут траектории полёта пули
     *
     * @return азимут
     */
    double aim() {
        return owner.getAzimuth() + owner.getAimError() + getAimError();
    }

    abstract void makeFire();

    public abstract void pickUpAmmo(Ammo wo);

    public void doSomething(long frameNanoTime) {
        timeFromLastShot += frameNanoTime;
    }

    abstract long getTimeBetweenShots();

    abstract double getAimError();

    public abstract String getImageName();

    public String getAmmoString() {
        return String.format("%d", ammo);
    }

    /**
     * Максимальная дистанция стрельбы
     *
     * @return
     */
    public abstract double getFireDistance();

    abstract void playShotSound();

    Point pointInTarget;

    public WorldObject checkFireLine(double azimuth) {
        double distance = getFireDistance();
        // самая дальняя точка, куда может долететь пуля
        pointInTarget = new Point(owner.getPosition().getX() + distance * Math.sin(azimuth),
                owner.getPosition().getY() + distance * Math.cos(azimuth));
        Point pw = findNearestWall(pointInTarget, owner.getRoom(), null);
        if (pw != null) {
            // попали в какую-то стенку
            pointInTarget = pw;
            distance = SpecialMath.lineLength(owner.getPosition(), pointInTarget);
        }
        // проверить попадание в какой-либо объект между стрелком и точкой попадания в стенку или дальней точкой выстрела
        WorldObject candidateToDie = null; // кандидат на получение пули
        for (WorldObject wo : world.getAllObjects()) {
            if (wo == owner) {
                continue; // в себя не стрелять
            }
            // найдём ближайший объект, в который попала пуля
            if (!wo.isCrossable()) { // объект должен быть непроходимым
                if (SpecialMath.lineAndCircleIntersects(owner.getPosition(), pointInTarget, wo.getPosition(), wo.getRadius())) {
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
            // никуда не попали, но владелец оружия враг, а не герой, надо проверить, попал ли он в героя
            if (SpecialMath.lineAndCircleIntersects(owner.getPosition(), pointInTarget, world.getHero().getPosition(), world.getHero().getRadius())) {
                double nd = SpecialMath.lineLength(owner.getPosition(), world.getHero().getPosition());
                if (nd < distance) {
                    // пуля попала в героя
                    candidateToDie = world.getHero();
                    distance = nd;
                }
            }
        }
        pointInTarget.moveTo(owner.getPosition().getX() + (distance - 16) * Math.sin(azimuth),
                owner.getPosition().getY() + (distance - 16) * Math.cos(azimuth));
        return candidateToDie;
    }

    /**
     * Найти точку на стене, куда попала пуля
     *
     * @param pt дальняя точка
     * @param inRoom в какой комнате проверять
     * @param inLine линия, через которую пуля влетела
     * @return точка, в которую попала пуля
     */
    private Point findNearestWall(Point pt, Room inRoom, Line inLine) {
        Point p = new Point();
        for (Line l : inRoom.getAllLines()) {
            if (l == inLine) {
                continue;
            }
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

    abstract public List<BigImage.ImageToDraw> getWeaponView(ScreenBuffer screen);

}
