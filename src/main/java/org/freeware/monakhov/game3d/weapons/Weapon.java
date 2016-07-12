package org.freeware.monakhov.game3d.weapons;

import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;
import org.freeware.monakhov.game3d.objects.nonmovable.Ammo;

/**
 *
 * @author Vasily Monakhov
 */
public abstract class Weapon {

    protected final World world;

    public Weapon(World world) {
        this.world = world;
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

    public abstract void makeFire(World world);

    public abstract void pickUpAmmo(Ammo wo);

    public void doSomething(long frameNanoTime) {
        timeFromLastShot += frameNanoTime;
    }

    public abstract long getTimeBetweenShots();

}
