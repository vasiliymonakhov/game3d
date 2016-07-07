package org.freeware.monakhov.game3d.objects.movable;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Sprite;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;

/**
 *
 * @author Vasily Monakhov
 */
public class FireBall extends Slug {

    public FireBall(World world, Point position, WorldObject creator, double azimuth) {
        super(world, position, creator, azimuth);
    }

    @Override
    public Sprite getSprite() {
        return Sprite.get("fireball");
    }

    @Override
    public double getRadius() {
        return 16;
    }

    @Override
    public double getInteractRadius() {
        return 24;
    }

    @Override
    public double getSpeed() {
        return 2048 / 1.0E9;
    }

    @Override
    public double getDamage() {
        return 500;
    }

}
