package org.freeware.monakhov.game3d.weapons;

import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.movable.MovableObject;
import org.freeware.monakhov.game3d.objects.nonmovable.Ammo;

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

    long timeFromLastShot = getTimeBetweenShots();

    public void fire() {
        if (timeFromLastShot >= getTimeBetweenShots() && ammo > 0) {
            makeFire(world);
            ammo--;
            timeFromLastShot = 0;
        }
    }

    /**
     * Прицеливание - возвращает азимут траектории полёта пули
     * @return  азимут
     */
    double aim() {
        return owner.getAzimuth() + owner.getAimError() + getAimError();
    }

    abstract void makeFire(World world);

    public abstract void pickUpAmmo(Ammo wo);

    public void doSomething(long frameNanoTime) {
        timeFromLastShot += frameNanoTime;
    }

    abstract long getTimeBetweenShots();

    abstract double getAimError();

    public abstract String getName();

    public String getAmmoString() {
        return String.format("%d", ammo);
    }

}
