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
public class Armor extends CrossableObject {

    public Armor(World world, Point position) {
        super(world, position);
    }

    @Override
    public Sprite getSprite() {
        return Sprite.get("armor");
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
    public void onInteractWith(WorldObject wo) {
        Hero h = world.getHero();
        if (wo == h) {
            if (h.getArmor() < 200) {
                h.addArmor(100);
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
