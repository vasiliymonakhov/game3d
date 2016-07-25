package org.freeware.monakhov.game3d.objects.nonmovable;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;
import org.freeware.monakhov.game3d.objects.movable.MovableObject;
import org.freeware.monakhov.game3d.weapons.Weapon;

/**
 *
 * @author Vasily Monakhov
 */
public abstract class WeaponOnMap extends CanGiveAmmo {

    int ammo;

    public WeaponOnMap(World world, Point position, int ammo) {
        super(world, position);
        this.ammo = ammo;
    }

    @Override
    public void onInteractWith(WorldObject wo) {
    }

    @Override
    public void onCollapseWith(WorldObject wo) {
    }

    @Override
    public void doSomething(long frameNanoTime) {
    }

    @Override
    public void onGetDamage(double d, WorldObject source) {
        world.deleteObject(this);
    }

    @Override
    public void onCycleEnd() {
    }

    public abstract int getSlot();

    public abstract Weapon create(MovableObject owner);

    @Override
    public int getAmmo() {
        return ammo;
    }

}
