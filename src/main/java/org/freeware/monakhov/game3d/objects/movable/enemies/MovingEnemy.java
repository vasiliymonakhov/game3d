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

    abstract double getNearAttackProbability(WorldObject wo);

    @Override
    public void onInteractWith(WorldObject wo) {
        if ((wo == target || Math.random() < getNearAttackProbability(wo)) && (wo.isAlive() || wo == world.getHero()) && this.isAlive() && damage != 0) {
            wo.onGetDamage(damage, this);
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

    abstract void playActivatedSound();

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
                    if (!fire(frameNanoTime)) {
                        move(frameNanoTime);
                    }
                    activeTime += frameNanoTime;
                    if (getTarget() == null) {
                        if (activeTime > getActiveTime()) {
                            deactivate();
                        }
                    }
                } else {
                    inactiveTime += frameNanoTime;
                    if (inactiveTime > getInactiveTime()) {
                        if (getTarget() != null) {
                            activate();
                            activateFriends();
                            playActivatedSound();
                        }
                    }
                }
                break;
        }
    }

    void activateFriends() {
        for (WorldObject wo : world.getAllObjects()) {
            if (wo instanceof MovingEnemy && wo.getRoom() == room) {
                MovingEnemy me = (MovingEnemy)wo;
                me.active = true;
                if (me.target == null) {
                    me.target = target;
                }
            }
        }
    }

    @Override
    public void onGetDamage(double d, WorldObject source) {
        super.onGetDamage(d, source);
        if (state == ALIVE) {
            activate();
            panic();
        }
    }

    void deactivate() {
        active = true;
        activeTime = 0;
        inactiveTime = 0;
    }

    void activate() {
        active = true;
        activeTime = 0;
        inactiveTime = 0;
    }

    abstract double getNormalMoveSpeed();

    abstract double getNormalStrafeSpeed();

    abstract double getPanicMoveSpeed();

    abstract double getPanicStrafeSpeed();

    private double moveSpeed = getNormalMoveSpeed();

    private double strafeSpeed = getNormalStrafeSpeed();

    private long panicTime;

    protected void panic() {
        inPanic = true;
        panicTime = getPanicTime();
        setAzimuth(Math.PI * 2 * Math.random());
        moveSpeed = getPanicMoveSpeed();
        strafeSpeed = getPanicStrafeSpeed();
    }

    boolean inPanic;

    protected void move(long frameNanoTime) {
        if (inPanic) {
            panicTime -= frameNanoTime;
            if (panicTime <= 0) {
                inPanic = false;
            }
        } else {
            if (target != null && (target.isAlive() || target == world.getHero())) {
                setAzimuth(-calcAngleToObject(target));
                moveSpeed = getNormalMoveSpeed();
                strafeSpeed = getNormalStrafeSpeed();
            } else {
                active = false;
                inactiveTime = 0;
                return;
            }
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
        panic();
    }

    abstract long getPanicTime();

    private long fireTime = getFireTime();

    abstract long getFireTime();

    abstract double getFireRange();

    protected boolean fire(long frameNanoTime) {
        fireTime -= frameNanoTime;
        if (fireTime < 0 && target != null && target.isAlive() && weapon != null) {
            if (SpecialMath.lineLength(position, target.getPosition()) > getFireRange()) {
                if (weapon.checkFireLine(azimuth) == target || inPanic) {
                    weapon.fire();
                    fireTime = getFireTime();
                    return true;
                }
            }
        }
        return false;
    }

}
