package org.freeware.monakhov.game3d.objects.movable.enemies;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Sprite;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;
import org.freeware.monakhov.game3d.objects.movable.Entity;
import org.freeware.monakhov.game3d.objects.movable.slugs.FireBall;

/**
 *
 * @author Vasily Monakhov
 */
public class FireballGun extends Entity {

    public FireballGun(World world, Point position, double azimuth) {
        super(world, position, null, azimuth);
    }

    @Override
    public Sprite getSprite() {
        return Sprite.get("fireball0");
    }

    @Override
    public double getRadius() {
        return 32;
    }

    @Override
    public double getInteractRadius() {
        return 32;
    }

    @Override
    public void onInteractWith(WorldObject wo) {
    }

    private long timeCounter;

    private final static long PAUSE_TIME = 500000000l;

    @Override
    public void doSomething(long frameNanoTime) {
        timeCounter += frameNanoTime;
        if (timeCounter > PAUSE_TIME) {
            timeCounter = 0;
            world.addNewObject(new FireBall(world, new Point(position.getX(), position.getY()), this, azimuth));
        }
    }

    @Override
    public void onCollapseWith(WorldObject wo) {
    }

    @Override
    public void onGetDamage(double d) {
        world.deleteObject(this);
    }

    @Override
    public void onCycleEnd() {
    }

}
