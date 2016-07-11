package org.freeware.monakhov.game3d.objects.movable.enemies;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Sprite;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;

/**
 *
 * @author Vasily Monakhov
 */
public class Zombie extends Enemy {

    public Zombie(World world, Point position, WorldObject creator, double azimuth) {
        super(world, position, creator, azimuth);
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
    public void onInteractWith(WorldObject wo) {
        if (wo == world.getHero() && state == ALIVE && damage != 0) {
            wo.onGetDamage(damage);
            damage = 0;
        }
    }

    @Override
    public void onCollapseWith(WorldObject wo) {
    }

    @Override
    public void onCycleEnd() {
    }

    double damage = 5;

    long timeToDamage;

    boolean active;

    long activeTime, inactiveTime;

    @Override
    public void doSomething(long frameNanoTime) {
        super.doSomething(frameNanoTime);
        switch (state) {
            case ALIVE:
                if (active) {
                    timeToDamage -= frameNanoTime;
                    if (timeToDamage < 0) {
                        damage = 5;
                        timeToDamage = 1000000000l;
                    }
                    move(frameNanoTime);
//                    fire(frameNanoTime);
                    activeTime += frameNanoTime;
                    if (activeTime > 15000000000l) {
                        activeTime = 0;
                        active = isCanSeeHero();
                    }
                } else {
                    inactiveTime += frameNanoTime;
                    if (inactiveTime > 1000000000l) {
                        inactiveTime = 0;
                       active = isCanSeeHero();
                    }
                }
                break;
        }
    }

    private final static double NORMAL_MOVE_SPEED = 256 / 1.0E9;
    private final static double NORMAL_STRAFE_SPEED = 128 / 1.0E9;
    private final static double PANIC_MOVE_SPEED = 512 / 1.0E9;
    private final static double PANIC_STRAFE_SPEED = 256 / 1.0E9;

    private double moveSpeed = NORMAL_MOVE_SPEED;
    private double strafeSpeed = NORMAL_STRAFE_SPEED;

    private long panicTime;

    protected void move(long frameNanoTime) {
        panicTime -= frameNanoTime;
        if (panicTime < 0) {
            setAzimuth(- calcAngleToHero());
                moveSpeed = NORMAL_MOVE_SPEED;
                strafeSpeed = NORMAL_STRAFE_SPEED;
        }
        double ms = moveSpeed * frameNanoTime;
        double ss = strafeSpeed * frameNanoTime;
        if (moveByWithCheck(ms, 0)) return;
        if (Math.random() > 0.5) {
            if (moveByWithCheck(ms, ss)) return;
            if (moveByWithCheck(ms, -ss)) return;
        } else {
            if (moveByWithCheck(ms, -ss)) return;
            if (moveByWithCheck(ms, ss)) return;
        }
        if (moveByWithCheck(0, ss)) return;
        if (moveByWithCheck(0, -ss)) return;
        if (Math.random() > 0.5) {
            if (moveByWithCheck(-ms, ss)) return;
            if (moveByWithCheck(-ms, -ss)) return;
        } else {
            if (moveByWithCheck(-ms, -ss)) return;
            if (moveByWithCheck(-ms, ss)) return;
        }

        panicTime = Math.round(5000000000l * Math.random());
        setAzimuth(Math.PI * 2 * Math.random());
        moveSpeed = PANIC_MOVE_SPEED;
        strafeSpeed = PANIC_STRAFE_SPEED;
    }

//    private long fireTime = Math.round(30l * Math.random() + 10000000000l);
//
//    protected void fire(long frameNanoTime) {
//        if (SpecialMath.lineLength(position, world.getHero().getPosition()) > 5000) {
//            fireTime -= frameNanoTime;
//            if (fireTime < 0) {
//                world.addNewObject(new FireBall(world, new Point(position.getX(), position.getY()), this, azimuth));
//                fireTime = Math.round(50000000000l * Math.random() + 3000000000l);
//            }
//        }
//    }

}
