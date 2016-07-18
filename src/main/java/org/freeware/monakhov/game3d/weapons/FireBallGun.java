package org.freeware.monakhov.game3d.weapons;

import org.freeware.monakhov.game3d.SoundSystem;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.movable.MovableObject;
import org.freeware.monakhov.game3d.objects.movable.slugs.FireBall;
import org.freeware.monakhov.game3d.objects.nonmovable.Ammo;
import org.freeware.monakhov.game3d.objects.nonmovable.FireBallAmmo;

/**
 *
 * @author Vasily Monakhov
 */
public class FireBallGun extends Weapon {

    public FireBallGun(World world, MovableObject owner) {
        super(world, owner);
        ammo = 100;
    }

    @Override
    public void makeFire() {
        world.addNewObject(new FireBall(world, new Point(owner.getPosition().getX(), owner.getPosition().getY()), owner, aim()));
    }

    @Override
    public void pickUpAmmo(Ammo wo) {
        if (ammo == 1000) return;
        if (wo instanceof FireBallAmmo) {
            ammo += wo.getAmmo();
            if (ammo > 1000) ammo = 1000;
            world.deleteObject(wo);
        }
    }

    @Override
    public long getTimeBetweenShots() {
        return 100000000l;
    }

    @Override
    public double getAimError() {
        return -0.01 + 0.02 * Math.random();
    }

    @Override
    public String getName() {
        return "FIREBALL GUN";
    }

    @Override
    void playShotSound() {
        SoundSystem.play("fireball_shot");
    }

    @Override
    public double getFireDistance() {
        return 1000000000;
    }

}
