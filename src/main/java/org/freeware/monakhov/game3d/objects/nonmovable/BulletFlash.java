package org.freeware.monakhov.game3d.objects.nonmovable;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.resources.Sprite;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;

/**
 *
 * @author Vasily Monakhov
 */
public class BulletFlash extends NonMovableObject {

    private final Sprite flashSprite;

    public BulletFlash(World world, Point position) {
        super(world, position);
        flashSprite = Sprite.getCopy("bullet_flash");
        double r = 32 * Math.random();
        flashSprite.setyOffset(64 + (int) ( - 16 + r));
    }

    public BulletFlash(World world, Point position, WorldObject wo) {
        super(world, position);
        flashSprite = Sprite.getCopy("bullet_flash");
        double r = 32 * Math.random();
        flashSprite.setyOffset(wo.getSprite().getYOffset() + wo.getSprite().getHeight() / 2 - 64  + (int) (-16 + r));
    }

    private final static long FLASH_TIME = 100000000l;

    @Override
    public Sprite getSprite() {
         return flashSprite;
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
        return 0;
    }

    @Override
    public void onInteractWith(WorldObject wo) {
    }

    @Override
    public void onCollapseWith(WorldObject wo) {
    }

    private long aliveTime;
    int counter = 0;

    @Override
    public void doSomething(long frameNanoTime) {
        aliveTime += frameNanoTime;
        if (aliveTime > FLASH_TIME) {
            world.deleteObject(this);
        }
    }

    @Override
    public void onGetDamage(double d, WorldObject source) {
    }

    @Override
    public void onCycleEnd() {
    }

    @Override
    public boolean needFlashFromBullet() {
        return false;
    }

}
