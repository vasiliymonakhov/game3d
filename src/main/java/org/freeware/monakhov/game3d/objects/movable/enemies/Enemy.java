package org.freeware.monakhov.game3d.objects.movable.enemies;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;
import org.freeware.monakhov.game3d.objects.movable.Entity;
import org.freeware.monakhov.game3d.weapons.Weapon;

/**
 * Абстрактный враг
 *
 * @author Vasily Monakhov
 */
public abstract class Enemy extends Entity {

    protected final static int ALIVE = 0;
    protected final static int DIEING = 1;
    protected final static int DEAD = 2;

    protected int state;

    public Enemy(World world, Point position, WorldObject creator, double azimuth) {
        super(world, position, creator, azimuth);
        weapon = createWeapon(world, this);
    }

    abstract Weapon createWeapon(World world, Enemy owner);

    protected final Weapon weapon;

    protected double health = getStartHealth();

    abstract double getStartHealth();

    @Override
    public void onGetDamage(double d, WorldObject source) {
        if (state == ALIVE) {
            health -= d;
            if (health <= 0) {
                state = DIEING;
                playDieSound();
            } else {
                playGetDamageSound();
                if (targetAcceptable(source)) {
                    target = source;
                }
            }

        }
    }

    abstract void playDieSound();
    abstract void playGetDamageSound();
    abstract void playNearAttackSound();

    /**
     * Можно ли пройти сквозь объект
     *
     * @return
     */
    @Override
    public boolean isCrossable() {
        return state == DEAD;
    }

    protected double dieingTime = 1000000000l;

    @Override
    public void doSomething(long frameNanoTime) {
        switch (state) {
            case DEAD:
                break;
            case DIEING:
                dieingTime -= frameNanoTime;
                if (dieingTime <= 0) {
                    state = DEAD;
                }
                break;
            case ALIVE:
                if (weapon != null) {
                    weapon.doSomething(frameNanoTime);
                }
                break;
        }
    }

    @Override
    public boolean isAlive() {
        return state == ALIVE;
    }

    protected double calcAngleToObject(WorldObject wo) {
        // TODO по возможность протабулировать значения
        double dx = this.position.getX() - wo.getPosition().getX();
        double dy = this.position.getY() - wo.getPosition().getY();
        if (dx == 0) {
            if (dy >= 0) {
                return Math.PI;
            } else {
                return 0;
            }
        } else {
            if (dx > 0) {
                return Math.atan(dy / dx) + Math.PI * 0.5;
            } else if (dy >= 0) {
                return Math.atan(dy / dx) + Math.PI * 1.5;
            } else {
                return Math.atan(dy / dx) - Math.PI * 0.5;
            }
        }
    }

    protected WorldObject target;

    WorldObject getTarget() {
        if (!world.getAllObjects().contains(target) || target != null && !target.isAlive()) {
            // цели нет, её надо найти и уничтожить
            if (canSeeHero()) {
                // видит героя - вот он и цель
                target = world.getHero();
            } else {
                target = null;
            }
        }
        return target;
    }

    abstract boolean targetAcceptable(WorldObject wobj);

}
