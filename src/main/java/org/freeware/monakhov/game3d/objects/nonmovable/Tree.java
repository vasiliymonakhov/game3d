package org.freeware.monakhov.game3d.objects.nonmovable;

import org.freeware.monakhov.game3d.map.Point;
import org.freeware.monakhov.game3d.map.Sprite;
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
        return 0;
    }

}
