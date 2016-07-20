package org.freeware.monakhov.game3d.objects.nonmovable;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.resources.Sprite;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;
import org.freeware.monakhov.game3d.objects.movable.Hero;

/**
 *
 * @author Vasily Monakhov
 */
public class MedKit extends CrossableObject {

    public MedKit(World world, Point position) {
        super(world, position);
    }

    @Override
    public Sprite getSprite() {
        return Sprite.get("medkit");
    }

    @Override
    public double getRadius() {
        return 16;
    }

    @Override
    public double getInteractRadius() {
        return 64;
    }

    @Override
    public void onInteractWith(WorldObject wo) {
        Hero h = world.getHero();
        if (wo == h) {
            if (h.getHealth() < 100) {
                h.addHealth(25);
                world.deleteObject(this);
            }
        }
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

}
