package org.freeware.monakhov.game3d.objects.movable;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;

/**
 *
 * @author Vasily Monakhov
 */
public abstract class Slug extends Entity {

    protected boolean boom;

    public Slug(World world, Point position, WorldObject creator, double azimuth) {
        super(world, position, creator, azimuth);
    }

    @Override
    public void onInteractWith(WorldObject wo) {
        if (boom) {
            wo.onGetDamage(getDamage());
        }
    }

    @Override
    public boolean isCrossable() {
        return true;
    }

    @Override
    public void onCollapseWith(WorldObject wo) {
        if (wo != creator) {
            boom = true;
        }
    }

    @Override
    public void onGetDamage(double d) {
    }

    @Override
    public void doSomething(long frameNanoTime) {
        if (boom) {
            world.deleteObject(this);
            return;
        }
        if (!moveByWithCheck(getSpeed() * frameNanoTime, 0)) {
            boom = true;
        }
    }

    public abstract double getSpeed();

    public abstract double getDamage();

}
