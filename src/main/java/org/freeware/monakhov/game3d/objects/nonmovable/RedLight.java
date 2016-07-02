package org.freeware.monakhov.game3d.objects.nonmovable;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Sprite;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;

/**
 * Фонарь
 * @author Vasily Monakhov 
 */
public class RedLight extends CrossableObject {

    public RedLight(World world, Point position) {
        super(world, position);
    }


    @Override
    public Sprite getSprite() {
        return Sprite.get("red_light_01");
    }

    @Override
    public double getRadius() {
        return 70;
    }

    @Override
    public void onInteractWith(WorldObject wo) {
    }

    @Override
    public void doSomething(long frameNanoTime) {
    }

    @Override
    public double getInteractRadius() {
        return 0;
    }

}
