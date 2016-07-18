package org.freeware.monakhov.game3d.objects.nonmovable;

import org.freeware.monakhov.game3d.SoundSystem;
import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.resources.Sprite;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;

/**
 * Зелёная бочка
 *
 * @author Vasily Monakhov
 */
public class GreenBarrel extends NonMovableObject {

    private final static int ALIVE = 0;
    private final static int BOOMING = 1;
    private final static int DEAD = 2;

    private int state = ALIVE;

    public GreenBarrel(World world, Point position) {
        super(world, position);
    }

    @Override
    public Sprite getSprite() {
        return Sprite.get("green_barrel");
    }

    @Override
    public double getRadius() {
        return 25;
    }

    @Override
    public void onInteractWith(WorldObject wo) {
    }

    long boomTime;
    long maxBoomTime = Math.round(1000 * Math.random()) * 1000000;

    @Override
    public void doSomething(long frameNanoTime) {
        switch (state) {
            case DEAD:
                world.deleteObject(this);
                world.addNewObject(new Boom(world, position));
                break;
            case BOOMING:
                boomTime += frameNanoTime;
                if (boomTime >= maxBoomTime) {
                    state = DEAD;
                }
                break;
        }
    }

    @Override
    public double getInteractRadius() {
        return 25;
    }

    @Override
    public void onCollapseWith(WorldObject wo) {
    }

    double damaged;

    double MAX_DAMAGE = 25;

    @Override
    public void onGetDamage(double d) {
        if (state == ALIVE) {
            damaged += d;
            if (damaged >= MAX_DAMAGE) {
                SoundSystem.play("boom");
                state = BOOMING;
            }
        }
    }

    @Override
    public void onCycleEnd() {
    }

}
