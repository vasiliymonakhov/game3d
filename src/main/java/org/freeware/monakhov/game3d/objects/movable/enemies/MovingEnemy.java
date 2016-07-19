package org.freeware.monakhov.game3d.objects.movable.enemies;

import org.freeware.monakhov.game3d.SpecialMath;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;
import static org.freeware.monakhov.game3d.objects.movable.enemies.Enemy.ALIVE;

/**
 *
 * @author Vasily Monakhov
 */
public abstract class MovingEnemy extends Enemy {

    public MovingEnemy(World world, Point position, WorldObject creator, double azimuth) {
        super(world, position, creator, azimuth);
    }

    @Override
    public void onInteractWith(WorldObject wo) {
        if (wo == world.getHero() && state == ALIVE && damage != 0) {
            wo.onGetDamage(damage);
            damage = 0;
            playNearAttackSound();
        }
    }

    abstract double getNearAttackDamage();

    double damage = getNearAttackDamage();

    long timeToDamage;

    boolean active;

    long activeTime, inactiveTime;

    abstract long getActiveTime();

    abstract long getInactiveTime();

    abstract long getTimeToDamage();

    @Override
    public void doSomething(long frameNanoTime) {
        super.doSomething(frameNanoTime);
        switch (state) {
            case ALIVE:
                if (active) {
                    timeToDamage -= frameNanoTime;
                    if (timeToDamage < 0) {
                        damage = getNearAttackDamage();
                        timeToDamage = getTimeToDamage();
                    }
                    move(frameNanoTime);
                    fire(frameNanoTime);
                    activeTime += frameNanoTime;
                    if (activeTime > getActiveTime()) {
                        activeTime = 0;
                        active = isCanSeeHero();
                        if (active) {
                            activateFriends();
                        }
                    }
                } else {
                    inactiveTime += frameNanoTime;
                    if (inactiveTime > getInactiveTime()) {
                        inactiveTime = 0;
                        active = isCanSeeHero();
                        if (active) {
                            activateFriends();
                        }
                    }
                }
                break;
        }
    }

    void activateFriends() {
        for (WorldObject wo : world.getAllObjects()) {
            if (wo instanceof MovingEnemy && wo.getRoom() == room) {
                ((MovingEnemy)wo).active = true;
            }
        }
    }

    @Override
    public void onGetDamage(double d) {
        super.onGetDamage(d);
        if (state == ALIVE) {
            panic();
        }
    }

    abstract double getNormalMoveSpeed();

    abstract double getNormalStrafeSpeed();

    abstract double getPanicMoveSpeed();

    abstract double getPanicStrafeSpeed();

    private double moveSpeed = getNormalMoveSpeed();
    ;
    private double strafeSpeed = getNormalStrafeSpeed();

    private long panicTime;

    private void panic() {
        panicTime = getPanicTime();
        setAzimuth(Math.PI * 2 * Math.random());
        moveSpeed = getPanicMoveSpeed();
        strafeSpeed = getPanicStrafeSpeed();
    }

    protected void move(long frameNanoTime) {
        panicTime -= frameNanoTime;
        if (panicTime < 0) {
            setAzimuth(-calcAngleToHero());
            moveSpeed = getNormalMoveSpeed();
            strafeSpeed = getNormalStrafeSpeed();
        } else {
            panic();
        }
        double ms = moveSpeed * frameNanoTime;
        double ss = strafeSpeed * frameNanoTime;
        if (moveByWithCheck(ms, 0)) {
            return;
        }
        if (Math.random() > 0.5) {
            if (moveByWithCheck(ms, ss)) {
                return;
            }
            if (moveByWithCheck(ms, -ss)) {
                return;
            }
        } else {
            if (moveByWithCheck(ms, -ss)) {
                return;
            }
            if (moveByWithCheck(ms, ss)) {
                return;
            }
        }
        if (moveByWithCheck(0, ss)) {
            return;
        }
        if (moveByWithCheck(0, -ss)) {
            return;
        }
        if (Math.random() > 0.5) {
            if (moveByWithCheck(-ms, ss)) {
                return;
            }
            moveByWithCheck(-ms, -ss);
        } else {
            if (moveByWithCheck(-ms, -ss)) {
                return;
            }
            moveByWithCheck(-ms, ss);
        }
    }

    abstract long getPanicTime();

    private long fireTime = getFireTime();

    abstract long getFireTime();

    abstract double getFireRange();

    protected void fire(long frameNanoTime) {
        if (world.getHero().isAlive() && weapon != null) {
            fireTime -= frameNanoTime;
            if (SpecialMath.lineLength(position, world.getHero().getPosition()) > getFireRange()) {
                if (weapon.checkFireLine(azimuth) == world.getHero()) {
                    if (fireTime < 0) {
                        weapon.fire();
                        fireTime = getFireTime();
                    }
                }
            }
        }
    }

}