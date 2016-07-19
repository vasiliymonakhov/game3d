package org.freeware.monakhov.game3d.objects.movable.enemies;

import org.freeware.monakhov.game3d.SoundSystem;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.resources.Sprite;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;
import org.freeware.monakhov.game3d.weapons.Pistol;
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
        return new Pistol(world, owner);
    }

    @Override
    double getNearAttackDamage() {
        return 5;
    }

    @Override
    double getNormalMoveSpeed() {
        return 256 / 1.0E9;
    }

    @Override
    double getNormalStrafeSpeed() {
        return 128 / 1.0E9;
    }

    @Override
    double getPanicMoveSpeed() {
        return 512 / 1.0E9;
    }

    @Override
    double getPanicStrafeSpeed() {
        return 256 / 1.0E9;
    }

    @Override
    long getPanicTime() {
        return Math.round(5000000000l * Math.random());
    }

    @Override
    long getActiveTime() {
        return 15000000000l;
    }

    @Override
    long getInactiveTime() {
        return 1000000000l;
    }

    @Override
    long getTimeToDamage() {
        return 1000000000l;
    }

    @Override
    long getFireTime() {
        return Math.round(5000000000l * Math.random() + 3000000000l);
    }

    @Override
    double getFireRange() {
        return 256;
    }

    @Override
    void playDieSound() {
        SoundSystem.play("zombie_die");
    }

    @Override
    void playNearAttackSound() {
       SoundSystem.play("zombie_bite");
    }

}
