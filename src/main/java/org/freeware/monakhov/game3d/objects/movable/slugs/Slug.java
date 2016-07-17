package org.freeware.monakhov.game3d.objects.movable.slugs;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;
import org.freeware.monakhov.game3d.objects.movable.Entity;

/**
 *
 * @author Vasily Monakhov
 */
public abstract class Slug extends Entity {

    protected final static int ALIVE = 0;
    protected final static int BOOMING = 1;
    protected final static int DEAD = 2;

    protected int state = ALIVE;

    protected boolean stopDamage;

    public Slug(World world, Point position, WorldObject creator, double azimuth) {
        super(world, position, creator, azimuth);
    }

    @Override
    public void onInteractWith(WorldObject wo) {
        if (state == BOOMING) {
            if (!stopDamage) {
                wo.onGetDamage(getDamage());
            }
        }
    }

    @Override
    public boolean isCrossable() {
        return true;
    }

    @Override
    public void onCollapseWith(WorldObject wo) {
        if (wo == creator) return;
        if (state == ALIVE) {
            state = BOOMING;
        }
    }

    @Override
    public void onGetDamage(double d) {
    }

    protected long boomTime;
    protected final long maxBoomTime = 200000000l;

    @Override
    public void doSomething(long frameNanoTime) {
        switch (state) {
            case DEAD:
                world.deleteObject(this);
                break;
            case BOOMING:
                boomTime += frameNanoTime;
                if (boomTime >= maxBoomTime) {
                    state = DEAD;
                }
                break;
            case ALIVE:
                if (!moveByWithCheck(getSpeed() * frameNanoTime, 0)) {
                    state = BOOMING;
                }
        }
    }

    public abstract double getSpeed();

    public abstract double getDamage();

    @Override
    public void onCycleEnd() {
        if (state == BOOMING)
            stopDamage = true;
    }

}
