package org.freeware.monakhov.game3d.objects.nonmovable;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.resources.Sprite;
import org.freeware.monakhov.game3d.map.World;
import org.freeware.monakhov.game3d.objects.WorldObject;

/**
 * Дерево
 * @author Vasily Monakhov
 */
public class Tree extends NonMovableObject {

    public Tree(World world, Point position) {
        super(world, position);
    }


    @Override
    public Sprite getSprite() {
        return Sprite.get("tree");
    }

    @Override
    public double getRadius() {
        return 60;
    }

    @Override
    public void onInteractWith(WorldObject wo) {
    }

    @Override
    public void doSomething(long frameNanoTime) {
    }

    @Override
    public double getInteractRadius() {
        return 60;
    }

    @Override
    public void onCollapseWith(WorldObject wo) {
    }

    double damage = 0;

    @Override
    public void onGetDamage(double d, WorldObject source) {
        damage += d;
        if (damage > 1000) {
            world.deleteObject(this);
        }
    }

    @Override
    public void onCycleEnd() {
    }

    @Override
    public boolean needFlashFromBullet() {
        return true;
    }

}
