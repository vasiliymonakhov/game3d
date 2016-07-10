package org.freeware.monakhov.game3d.objects.movable.slugs;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Sprite;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;

/**
 *
 * @author Vasily Monakhov
 */
public class FireBall extends Slug {

    private static final Sprite[] SPRITES = {
        Sprite.get("fireball0"),
        Sprite.get("fireball1"),
        Sprite.get("fireball2"),
        Sprite.get("fireball3"),
        Sprite.get("fireball4")};

    public FireBall(World world, Point position, WorldObject creator, double azimuth) {
        super(world, position, creator, azimuth);
    }

    @Override
    public Sprite getSprite() {
        if (state == ALIVE) {
            return SPRITES[0];
        } else if (state == BOOMING) {
            int idx = (int)(boomTime / 40000000);
            if (idx < SPRITES.length) {
                return SPRITES[idx];
            }
        }
        return null;
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
    public double getSpeed() {
        return 2048 / 1.0E9;
    }

    @Override
    public double getDamage() {
        return 25;
    }

}
