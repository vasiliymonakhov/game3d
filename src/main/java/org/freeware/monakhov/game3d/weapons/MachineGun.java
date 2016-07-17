package org.freeware.monakhov.game3d.weapons;

import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.movable.MovableObject;
import org.freeware.monakhov.game3d.objects.nonmovable.Ammo;

/**
 * Пистолетик
 * @author Vasily Monakhov
 */
public class MachineGun extends InstantBulletWeapon {

    public MachineGun(World world, MovableObject owner) {
        super(world, owner);
        ammo = 200;
    }

    @Override
    public double getAimError() {
         return -0.01 + 0.02 * Math.random();
    }

    @Override
    public double getFireDistance() {
        return 32768;
    }

    @Override
    public double getDamage(double distance) {
        return 8 * getFireDistance() / distance;
    }

    @Override
    public void pickUpAmmo(Ammo wo) {
    }

    @Override
    public long getTimeBetweenShots() {
        return 50000000l;
    }

    @Override
    public String getName() {
        return "MACHINE GUN";
    }

}
