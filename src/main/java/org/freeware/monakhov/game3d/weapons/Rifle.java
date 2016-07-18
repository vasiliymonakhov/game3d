package org.freeware.monakhov.game3d.weapons;

import org.freeware.monakhov.game3d.SoundSystem;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.movable.MovableObject;
import org.freeware.monakhov.game3d.objects.nonmovable.Ammo;

/**
 * Винтовка
 * @author Vasily Monakhov
 */
public class Rifle extends InstantBulletWeapon {

    public Rifle(World world, MovableObject owner) {
        super(world, owner);
        ammo = 10;
    }

    @Override
    public double getAimError() {
         return -0.001 + 0.0005 * Math.random();
    }

    @Override
    public double getFireDistance() {
        return 100000;
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
        return 500000000l;
    }

    @Override
    public String getName() {
        return "RIFLE";
    }

    @Override
    void playShotSound() {
            SoundSystem.play("rifle_shot");
    }

}
