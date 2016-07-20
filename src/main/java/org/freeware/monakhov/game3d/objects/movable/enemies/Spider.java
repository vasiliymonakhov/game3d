package org.freeware.monakhov.game3d.objects.movable.enemies;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;
import org.freeware.monakhov.game3d.resources.Sprite;
import org.freeware.monakhov.game3d.weapons.Weapon;

/**
 *
 * @author Vasily Monakhov
 */
public class Spider extends MovingEnemy {

    public Spider(World world, Point position, double azimuth) {
        super(world, position, null, azimuth);
    }

    @Override
    double getNearAttackDamage() {
        return 3;
    }

    @Override
    long getActiveTime() {
        return 100000000000l;
    }

    @Override
    long getInactiveTime() {
        return 10000000l;
    }

    @Override
    long getTimeToDamage() {
        return 1000000000l;
    }

    @Override
    double getNormalMoveSpeed() {
        return 384 / 1.0E9;
    }

    @Override
    double getNormalStrafeSpeed() {
        return 128 / 1.0E9;
    }

    @Override
    double getPanicMoveSpeed() {
        return 1024 / 1.0E9;
    }

    @Override
    double getPanicStrafeSpeed() {
        return 512 / 1.0E9;
    }

    @Override
    long getPanicTime() {
        return Math.round(5000000000l * Math.random());
    }

    @Override
    long getFireTime() {
        return 0;
    }

    @Override
    double getFireRange() {
        return 10;
    }

    @Override
    Weapon createWeapon(World world, Enemy owner) {
        return null;
    }

    @Override
    void playDieSound() {
    }

    @Override
    void playNearAttackSound() {
    }

    @Override
    public double getAimError() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void doSomething(long frameNanoTime) {
        super.doSomething(frameNanoTime);
        time += frameNanoTime;
        spriteIndex = (int)(time / 50000000) % sprites.length;
    }

    long time = Math.round(Math.random() * 1.0E9);
    int spriteIndex;

    private final static Sprite[] sprites = {Sprite.get("spider_1"), Sprite.get("spider_2")};
    private final static Sprite DEAD_SPRITE = Sprite.get("spider_3");

    @Override
    public Sprite getSprite() {
        if (state == ALIVE) {
            return sprites[spriteIndex];
        } else {
            return DEAD_SPRITE;
        }
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
    public void onCollapseWith(WorldObject wo) {
        if (wo instanceof MovingEnemy) {
            panic();
        }
    }

    @Override
    public void onCycleEnd() {
    }

    @Override
    public boolean isCrossable() {
        return true;
    }

    @Override
    boolean targetAcceptable(WorldObject wobj) {
        // может атаковать других врагов, но не своих, и конечно же главного героя
        return (wobj instanceof Enemy) && !(wobj instanceof Spider) || wobj == world.getHero();
    }

    @Override
    double getStartHealth() {
        return 10;
    }

    @Override
    void playActivatedSound() {
    }

    @Override
    void playGetDamageSound() {
    }

    @Override
    double getNearAttackProbability(WorldObject wo) {
        if (wo instanceof Spider) return 0.001;
        return 0.1;
    }


}
