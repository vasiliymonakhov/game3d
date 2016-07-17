package org.freeware.monakhov.game3d.objects.nonmovable;

import org.freeware.monakhov.game3d.SpecialMath;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Sprite;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;

/**
 *
 * @author Vasily Monakhov
 */
public class Boom extends NonMovableObject {

    public Boom(World world, Point position) {
        super(world, position);
    }

    @Override
    public Sprite getSprite() {
        return Sprite.get("boom");
    }

    @Override
    public boolean isCrossable() {
        return true;
    }

    @Override
    public double getRadius() {
        return 0;
    }

    @Override
    public double getInteractRadius() {
        return 2048;
    }

    protected boolean stopDamage;
    protected  final double damage = 64;

    private void makeDamage(WorldObject wo) {
        if (stopDamage) return;
        wo.onGetDamage(damage * 256 / SpecialMath.lineLength(position, wo.getPosition()));
    }

    @Override
    public void onInteractWith(WorldObject wo) {
        makeDamage(wo);
    }

    @Override
    public void onCollapseWith(WorldObject wo) {
        makeDamage(wo);
    }

    private long aliveTime;
    int counter = 0;

    @Override
    public void doSomething(long frameNanoTime) {
        aliveTime += frameNanoTime;
        if (aliveTime > 1000000000l) {
            world.deleteObject(this);
        }
    }

    @Override
    public void onGetDamage(double d) {
    }

    @Override
    public void onCycleEnd() {
        stopDamage = true;
    }

}
