package org.freeware.monakhov.game3d.objects.nonmovable;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.movable.MovableObject;
import org.freeware.monakhov.game3d.resources.Sprite;
import org.freeware.monakhov.game3d.weapons.MachineGun;
import org.freeware.monakhov.game3d.weapons.Weapon;

/**
 *
 * @author Vasily Monakhov
 */
public class MachineGunOnMap extends WeaponOnMap {

    public MachineGunOnMap(World world, Point position, int ammo) {
        super(world, position, ammo);
    }

    public MachineGunOnMap(World world, Point position) {
        super(world, position, 100);
    }

    @Override
    public Sprite getSprite() {
        return Sprite.get("machine_gun");
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
        return 3;
    }

    @Override
    public Weapon create(MovableObject owner) {
        return new MachineGun(world, owner, getAmmo());
    }

}
