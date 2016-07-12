package org.freeware.monakhov.game3d.weapons;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;
import org.freeware.monakhov.game3d.objects.movable.Hero;
import org.freeware.monakhov.game3d.objects.movable.slugs.FireBall;
import org.freeware.monakhov.game3d.objects.nonmovable.Ammo;
import org.freeware.monakhov.game3d.objects.nonmovable.FireBallAmmo;

/**
 *
 * @author Vasily Monakhov
 */
public class FireBallGun extends Weapon {

    public FireBallGun(World world) {
        super(world);
        ammo = 100;
    }

    @Override
    public void makeFire(World world) {
        Hero h = world.getHero();
        double azdelta = -0.01 + 0.02 * Math.random();
        world.addNewObject(new FireBall(world, new Point(h.getPosition().getX(), h.getPosition().getY()), h, world.getHero().getAzimuth() + azdelta));
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
    public String toString() {
        return String.format("FIREBALLGUN AMMO : %d", ammo);
    }

}
