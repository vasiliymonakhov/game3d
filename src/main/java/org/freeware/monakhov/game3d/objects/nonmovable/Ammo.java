package org.freeware.monakhov.game3d.objects.nonmovable;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.World;

/**
 *
 * @author Vasily Monakhov
 */
public abstract class Ammo extends CrossableObject {

    public Ammo(World world, Point position) {
        super(world, position);
    }

    public abstract int getAmmo();

    @Override
    public void onGetDamage(double d) {
        world.deleteObject(this);
    }

}
