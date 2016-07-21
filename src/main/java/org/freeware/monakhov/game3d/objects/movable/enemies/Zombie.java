package org.freeware.monakhov.game3d.objects.movable.enemies;

import org.freeware.monakhov.game3d.SoundSystem;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.resources.Sprite;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;
import org.freeware.monakhov.game3d.weapons.Weapon;

/**
 *
 * @author Vasily Monakhov
 */
public class Zombie extends MovingEnemy {

    public Zombie(World world, Point position, double azimuth) {
        super(world, position, null, azimuth);
    }

    @Override
    public Sprite getSprite() {
        switch (state) {
            case ALIVE:
                return Sprite.get("zombie_0");
            case DIEING:
                return Sprite.get("zombie_1");
            case DEAD:
                return Sprite.get("zombie_2");
        }
        return null;
    }

    @Override
    public double getRadius() {
        return 64;
    }

    @Override
    public double getInteractRadius() {
        return 96;
    }

    @Override
    public void onCollapseWith(WorldObject wo) {
    }

    @Override
    public void onCycleEnd() {
    }

    @Override
    public double getAimError() {
        return -0.1 + 0.2 * Math.random();
    }

    @Override
    Weapon createWeapon(World world, Enemy owner) {
        return null;
    }

    @Override
    double getNearAttackDamage() {
        return 10;
    }

    @Override
    double getNormalMoveSpeed() {
        return 512 / 1.0E9;
    }

    @Override
    double getNormalStrafeSpeed() {
        return 256 / 1.0E9;
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
        return Math.round(5.0e9 * Math.random());
    }

    @Override
    long getActiveTime() {
        return Math.round(15.0e9 * Math.random());
    }

    @Override
    long getInactiveTime() {
        return Math.round(1.0e10 * Math.random());
    }

    @Override
    long getTimeToDamage() {
        return Math.round(1.0e9* Math.random() + 0.3e9);
    }

    @Override
    long getFireTime() {
        return Math.round(5.0e9 * Math.random() + 3.0e9);
    }

    @Override
    double getFireRange() {
        return 0;
    }

    @Override
    void playDieSound() {
        SoundSystem.play("zombie_die");
    }

    @Override
    void playNearAttackSound() {
       SoundSystem.play("zombie_bite");
    }

    @Override
    boolean targetAcceptable(WorldObject wobj) {
        // может атаковать других врагов и главного героя
        return wobj instanceof Enemy || wobj == world.getHero();
    }

    @Override
    double getStartHealth() {
        return 100;
    }

    @Override
    void playActivatedSound() {
        SoundSystem.play("zombie_activated");
    }

    @Override
    void playGetDamageSound() {
        SoundSystem.play("zombie_get_damage");
    }

    @Override
    double getNearAttackProbability(WorldObject wo) {
        if (wo instanceof Zombie) return 0.001;
        return 0.1;
    }

    @Override
    public boolean needFlashFromBullet() {
        return false;
    }
}
