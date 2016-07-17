package org.freeware.monakhov.game3d.weapons;

import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.movable.MovableObject;
import org.freeware.monakhov.game3d.objects.nonmovable.Ammo;

/**
 * Пистолетик
 * @author Vasily Monakhov
 */
public class AssaultRifle extends InstantBulletWeapon {

    public AssaultRifle(World world, MovableObject owner) {
        super(world, owner);
        ammo = 30;
    }

    @Override
    public double getAimError() {
         return -0.01 + 0.02 * Math.random();
    }

    @Override
    public double getFireDistance() {
        return 65535;
    }

    @Override
    public double getDamage(double distance) {
        return 16 * getFireDistance() / distance;
    }

    @Override
    public void pickUpAmmo(Ammo wo) {
    }

    @Override
    public long getTimeBetweenShots() {
        return 100000000l;
    }

    @Override
    public String getName() {
        return "ASSAULT RIFLE";
    }

}
