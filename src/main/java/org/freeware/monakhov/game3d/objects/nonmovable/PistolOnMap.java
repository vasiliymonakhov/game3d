package org.freeware.monakhov.game3d.objects.nonmovable;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;
import org.freeware.monakhov.game3d.objects.movable.MovableObject;
import org.freeware.monakhov.game3d.resources.Sprite;
import org.freeware.monakhov.game3d.weapons.Pistol;
import org.freeware.monakhov.game3d.weapons.Weapon;

/**
 *
 * @author Vasily Monakhov
 */
public class PistolOnMap extends WeaponOnMap {

    public PistolOnMap(World world, Point position, int ammo) {
        super(world, position, ammo);
    }

    public PistolOnMap(World world, Point position) {
        super(world, position, 10);
    }    

    @Override
    public Sprite getSprite() {
        return Sprite.get("pistol");
    }

    @Override
    public double getRadius() {
        return 32;
    }

    @Override
    public double getInteractRadius() {
        return 64;
    }

    @Override
    public int getSlot() {
        return 1;
    }

    @Override
    public Weapon create(MovableObject owner) {
        return new Pistol(world, owner, getAmmo());
    }

}
