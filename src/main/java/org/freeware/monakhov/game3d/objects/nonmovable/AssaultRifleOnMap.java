package org.freeware.monakhov.game3d.objects.nonmovable;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.movable.MovableObject;
import org.freeware.monakhov.game3d.resources.Sprite;
import org.freeware.monakhov.game3d.weapons.AssaultRifle;
import org.freeware.monakhov.game3d.weapons.Weapon;

/**
 *
 * @author Vasily Monakhov
 */
public class AssaultRifleOnMap extends WeaponOnMap {

    public AssaultRifleOnMap(World world, Point position, int ammo) {
        super(world, position, ammo);
    }

    public AssaultRifleOnMap(World world, Point position) {
        super(world, position, 30);
    }

    @Override
    public Sprite getSprite() {
        return Sprite.get("assault_rifle");
    }

    @Override
    public double getRadius() {
        return 128;
    }

    @Override
    public double getInteractRadius() {
        return 128;
    }

    @Override
    public int getSlot() {
        return 2;
    }

    @Override
    public Weapon create(MovableObject owner) {
        return new AssaultRifle(world, owner, getAmmo());
    }

}
