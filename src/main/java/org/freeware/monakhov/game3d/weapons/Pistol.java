package org.freeware.monakhov.game3d.weapons;

import org.freeware.monakhov.game3d.SoundSystem;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.movable.MovableObject;
import org.freeware.monakhov.game3d.objects.nonmovable.Ammo;

/**
 * Пистолетик
 * @author Vasily Monakhov
 */
public class Pistol extends InstantBulletWeapon {

    public Pistol(World world, MovableObject owner) {
        super(world, owner);
        ammo = 17;
    }

    @Override
    public double getFireDistance() {
        return 16384;
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
        return 200000000l;
    }

    @Override
    public double getAimError() {
         return -0.01 + 0.02 * Math.random();
    }

    @Override
    public String getName() {
        return "PISTOL";
    }

    @Override
    void playShotSound() {
        SoundSystem.play("pistol_shot");
    }

}
