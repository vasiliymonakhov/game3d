package org.freeware.monakhov.game3d.objects.movable.slugs;

import org.freeware.monakhov.game3d.SoundSystem;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.resources.Sprite;
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
            int idx = (int)((SPRITES.length - 1)* boomTime / getMaxBoomTime());
            if (idx < SPRITES.length) {
                return SPRITES[idx];
            }
        }
        return SPRITES[SPRITES.length - 1];
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
        return 51;
    }

    @Override
    public double getAimError() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    void playDamageSound() {
        SoundSystem.play("fireball_boom");
    }

    @Override
    long getMaxBoomTime() {
         return 200000000l;
    }

}
